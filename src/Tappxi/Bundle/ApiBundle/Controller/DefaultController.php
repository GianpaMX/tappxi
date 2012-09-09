<?php

namespace Tappxi\Bundle\ApiBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Route;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Template;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Method;
use Symfony\Component\HttpKernel\Exception\HttpException;
use Symfony\Component\HttpFoundation\Response;
use Tappxi\Bundle\ApiBundle\Entity;


class DefaultController extends Controller
{
    /**
     * @Route("")
     * @Template()
     */
    public function indexAction()
    {
        return array();
    }

    /**
     * @Route("/login", defaults={"_format"="json"})
     * @Method({"POST"})
     */
    public function loginAction()
    {
        $fbToken = $this->getRequest()->request->get('fb_token');
        $user = $this->getUserByFbToken($fbToken);
        $em = $this->getManager();
        if ( $user ){
            $token = md5($user->getEmail() . microtime() . $fbToken);

            $sessions = $this->getSessionRepo()->findBy(array('user' => $user->getId()));
            array_walk($sessions, function ($session) use($em){
                //$em->remove($session);
            });

            $session = new Entity\Session();
            $session->setUser($user);
            $session->setFbToken($fbToken);
            $session->setToken($token);

            $em->persist($session);
            $em->flush();
        }else{
        }
        return new Response($session->toJson());
    }

    /**
     * @Route("/taxi/request", defaults={"_format"="json"})
     * @Method({"POST"})
     */
    public function newTaxiRequestAction(){
        $user = $this->getUser();
        $requestActive = $this->getRequestActive($user);
        if($requestActive){
            $this->cancelRequest($requestActive);
        }
        $request = new Entity\Request();
        $params = $this->getRequest()->request->all();
        $request->setUser($user);
        $request->setStatus(Entity\Request::STATUS_ACTIVE);
        $request->setAddressStartFromArray($params);
        $request->setAddressEndFromArray($params);
        $this->getManager()->persist($request->getAddressStart());
        if( $request->getAddressEnd() ){
            $this->getManager()->persist($request->getAddressEnd());
        }
        $this->getManager()->persist($request);
        $this->getManager()->flush();
        return new Response($request->toJson());
    }

    /**
     * @Route("/offers", defaults={"_format"="json"})
     * @Method({"POST"})
     */
    public function getOffersAction(){
        $list = array();
        $user = $this->getUser();
        $requestActive = $this->getRequestActive($user);
        if($requestActive){
            $offers = $this->getOfferRepo()->findBy(array('request'=> $requestActive->getId()));
            $list = array_map(function($offer){
                return $offer->toArray();
            }, $offers);
        }

        return new Response(json_encode($list));
    }

    /**
     * @Route("/trip/confirm", defaults={"_format"="json"})
     * @Method({"POST"})
     */
    public function tripConfirmAction(){
        $user = $this->getUser();
        $requestActive = $this->getRequestActive($user);
        if(!$requestActive){
            throw $this->createNotFoundException("request active not exists");
        }

        $idOffer = $this->getRequest()->request->get('id_offer');
        if( !$idOffer ){
            throw $this->createNotFoundException("The offer not exists");
        }
        $offer = $this->getOfferRepo()->find($idOffer);
        if( !$offer ){
            throw $this->createNotFoundException("The offer not exists");
        }

        if($offer instanceof Entity\Offer){
            if($requestActive->getId() != $offer->getRequest()->getId()){
                throw new HttpException(500, "La cotizacion es de una peticion pasada");
            }
            $tripExistent = $this->getTripRepo()->findBy(array('offer' => $offer->getId()));
            if($tripExistent){
                throw new HttpException(500, "Esta cotizacion ya ha sido confirmada");
            }
            $trip = new Entity\Trip();
            $trip->setFare(0);
            $trip->setMovement(null);
            $trip->setOffer($offer);
            $trip->setRequest($requestActive);
            $trip->setStatus(Entity\Trip::STATUS_ON_WAY);
            $trip->setTaxi(null);
            $this->getManager()->persist($trip);
            $this->getManager()->flush();
        }

        return new Response($trip->toJson());
    }

    /**
     * @Route("/trip/fare", defaults={"_format"="json"})
     * @Method({"POST"})
     */
    public function tripFareAction(){
        $user = $this->getUser();
        $idTrip = $this->getRequest()->request->get('id_trip');
        if( !$idTrip ){
            throw $this->createNotFoundException("The trip not exists");
        }
        $trip = $this->getTripRepo()->find($idTrip);
        if($trip instanceof Entity\Trip){
            if($trip->getRequest()->getUser()->getId() != $user->getId()){
                throw new HttpException(500, "El viaje no pertenece a este usuario");
            }
            return new Response(json_encode(array('fare' => $trip->getFare())));
        }else{
            throw $this->createNotFoundException("The trip not exists");
        }
    }

    /**
     * @Route("/trip/fare", defaults={"_format"="json"})
     * @Method({"PUT"})
     */
    public function updateTripFareAction(){
        $user = $this->getUser();
        $idTrip = $this->getRequest()->request->get('id_trip');
        $fare = $this->getRequest()->request->get('fare');
        if( !$idTrip ){
            throw $this->createNotFoundException("The trip not exists");
        }
        if(!$fare){
            throw new HttpException(500, "El costo no puede ser cero o vacio");
        }
        $trip = $this->getTripRepo()->find($idTrip);
        if($trip instanceof Entity\Trip){
            if($trip->getRequest()->getUser()->getId() != $user->getId()){
                throw new HttpException(500, "El viaje no pertenece a este usuario");
            }
            $trip->setFare($fare);
            $trip->setStatus(Entity\Trip::STATUS_UNCONFIRMED);
            $this->getManager()->persist($trip);
            $this->getManager()->flush();
            return new Response($trip->toJson());
        }else{
            throw $this->createNotFoundException("The trip not exists");
        }
    }

    /**
     * @Route("/trip/pay", defaults={"_format"="json"})
     * @Method({"POST"})
     */
    public function payTripAction(){
        $user = $this->getUser();
        $idTrip = $this->getRequest()->request->get('id_trip');
        if( !$idTrip ){
            throw $this->createNotFoundException("The trip not exists");
        }
        $trip = $this->getTripRepo()->find($idTrip);
        if($trip instanceof Entity\Trip){
            if($trip->getRequest()->getUser()->getId() != $user->getId()){
                throw new HttpException(500, "El viaje no pertenece a este usuario");
            }
            $trip->setStatus(Entity\Trip::STATUS_PAYED);
            $user->setBalance($user->getBalance() - $trip->getFare());
            $this->getManager()->flush();
            $movement = new Entity\Movement();
            $movement->setAmount($trip->getFare());
            $movement->setCreatedAt(new \DateTime());
            $movement->setUser($user);
            $movement->setType(Entity\Movement::TYPE_PAY);
            $this->getManager()->persist($movement);
            $this->getManager()->flush();
            return new Response(json_encode(array('balance' => $user->getBalance())));
        }else{
            throw $this->createNotFoundException("The trip not exists");
        }
    }

    /**
     * @Route("/request", defaults={"_format"="json"})
     * @Method({"DELETE"})
     */
    public function cancelRequestAction(){
        $user = $this->getUser();
        $requestActive = $this->getRequestActive($user);
        $this->cancelRequest($request);
        $this->getManager()->flush();
        return new Response(json_encode(array('success' => true)));
    }

    /**
     * @Route("/trip/{id_trip}", defaults={"_format"="json"})
     * @Method({"DELETE"})
     */
    public function cancelTripAction($id_trip){
        $user = $this->getUser();
        $trip = $this->getTripRepo()->find($id_trip);
        if($trip instanceof Entity\Trip){
            $trip->setStatus(Entity\Trip::STATUS_FAILED);
            $this->getManager()->flush();
        }else{
            throw $this->createNotFoundException("trip not exists");
        }
        return new Response(json_encode(array('success' => true)));
    }

    /**
     * @Route("/balance", defaults={"_format"="json"})
     * @Method({"POST"})
     */
    public function getBalanceAction(){
        $user = $this->getUser();
        return new Response(json_encode(array('balance' => $user->getBalance())));
    }

    /**
     * @Route("/logout", defaults={"_format"="json"})
     * @Method({"POST"})
     */
    public function logoutAction(){
        $user = $this->getUser();
        $sessions = $this->getSessionRepo()->findBy(array('user' => $user->getId()));
        array_walk($sessions, function ($session) use($em){
            //$em->remove($session);
        });
        $this->getManager()->flush();
        return new Response(json_encode(array('success' => true)));
    }

    /**
     * @Route("/trip/not-authorized", defaults={"_format"="json"})
     * @Method({"PUT"})
     */
    public function tripNotAuthorizedAction(){
        $id_trip = $this->getRequest()->request->get('id_trip');
        $user = $this->getUser();
        $trip = $this->getTripRepo()->find($id_trip);
        if($trip instanceof Entity\Trip){
            $trip->setStatus(Entity\Trip::STATUS_IS_WRONG);
            $this->getManager()->flush();
        }else{
            throw $this->createNotFoundException("trip not exists");
        }
        return new Response(json_encode(array('success' => true)));
    }

    /**
     * @Route("/taxi/arrival", defaults={"_format"="json"})
     * @Method({"POST"})
     */
    public function taxiArrivalAction(){
        $user = $this->getUser();
        $idTrip = $this->getRequest()->request->get('id_trip');
        if(!$idTrip){
            throw $this->createNotFoundException("el viaje no existe");
        }
        $trip = $this->getTripRepo()->find($idTrip);
        if(!$trip){
            throw $this->createNotFoundException("el viaje no existe");
        }
        $trip->setArrivalTaxi(true);
        $this->getManager()->flush();
        return new Response($trip->toJson());
    }

    /**
     * @return \Doctrine\Common\Persistence\ObjectManager
     */
    public function getManager(){
        return $this->getDoctrine()->getManager();
    }

    /**
     *
     * @throws HttpException
     * @return Entity\Trip
     */
    public function getTripActive(){
        $user = $this->getUser();
        $requestActive = $this->getRequestActive($user);
        $trip = $this->getTripRepo()->findOneBy(array('request' => $requestActive->getId()));
        if(!$trip){
            throw new HttpException(500, "No existe un viaje activo");
        }
        return $trip;
    }

    /**
     *
     * @param unknown_type $fbToken
     * @return Entity\User
     */
    private function getUserByFbToken($fbToken){
        // TODO facebook
        return $this->getUserRepo()->findOneById(1);
    }

    /**
     *
     * @param unknown_type $token
     * @return Entity\User
     */
    private function getUserByToken($token){
        $session = $this->getSessionRepo()->findOneBy(array('token' => $token));
        $user = null;
        if( $session instanceof Entity\Session ) {
            $user = $session->getUser();
        }
        if(!$user){
            throw new HttpException(401, "token invalido");
        }
        return $user;
    }

    private function cancelRequest(Entity\Request $request){
        $request->setStatus(Entity\Request::STATUS_CANCELLED);
    }

    /**
     *
     * @param unknown_type $token
     * @return Entity\User
     */
    public function getUser(){
        $request = $this->getRequest();
        return $this->getUserByToken($request->request->get('token'));
    }

    /**
     * @return \Doctrine\Common\Persistence\ObjectRepository
     */
    private function getUserRepo(){
        return $this->getDoctrine()->getRepository('TappxiApiBundle:User');
    }

    /**
     * @return \Doctrine\Common\Persistence\ObjectRepository
     */
    private function getSessionRepo(){
        return $this->getDoctrine()->getRepository('TappxiApiBundle:Session');
    }

    /**
     * @return \Doctrine\Common\Persistence\ObjectRepository
     */
    private function getAddressRepo(){
        return $this->getDoctrine()->getRepository('TappxiApiBundle:Address');
    }

    /**
     * @return \Doctrine\Common\Persistence\ObjectRepository
     */
    private function getMovementRepo(){
        return $this->getDoctrine()->getRepository('TappxiApiBundle:Movement');
    }

    /**
     * @return \Doctrine\Common\Persistence\ObjectRepository
     */
    private function getOfferRepo(){
        return $this->getDoctrine()->getRepository('TappxiApiBundle:Offer');
    }

    /**
     * @return \Doctrine\Common\Persistence\ObjectRepository
     */
    private function getRequestRepo(){
        return $this->getDoctrine()->getRepository('TappxiApiBundle:Request');
    }

    /**
     * @return \Doctrine\Common\Persistence\ObjectRepository
     */
    private function getStandRepo(){
        return $this->getDoctrine()->getRepository('TappxiApiBundle:Stand');
    }

    /**
     * @return \Doctrine\Common\Persistence\ObjectRepository
     */
    private function getTaxiRepo(){
        return $this->getDoctrine()->getRepository('TappxiApiBundle:Taxi');
    }

    /**
     * @return \Doctrine\Common\Persistence\ObjectRepository
     */
    private function getTripRepo(){
        return $this->getDoctrine()->getRepository('TappxiApiBundle:Trip');
    }

    /**
     *
     * @param Entity\User $user
     * @return Entity\Request
     */
    private function getRequestActive(Entity\User $user){
        return $this->getRequestRepo()->findOneBy(array(
            'user' => $user->getId(),
            'status' => Entity\Request::STATUS_ACTIVE,
        ));
    }

}
