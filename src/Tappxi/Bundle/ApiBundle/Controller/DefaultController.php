<?php

namespace Tappxi\Bundle\ApiBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Route;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Template;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Method;
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
                $em->remove($session);
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
        $this->getManager()->persist($request->getAddressEnd());
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
     * @return \Doctrine\Common\Persistence\ObjectManager
     */
    public function getManager(){
        return $this->getDoctrine()->getManager();
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
            throw new \Symfony\Component\HttpKernel\Exception\HttpException(401, "token invalido");
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
