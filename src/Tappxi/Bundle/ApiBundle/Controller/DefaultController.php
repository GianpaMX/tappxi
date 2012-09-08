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
            //todo user no valido
        }
        return new Response($session->toJson());
    }

    /**
     * @Route("/taxi/request", defaults={"_format"="json"})
     * @Method({"POST"})
     */
    public function newTaxiRequestAction(){
        $user = $this->getUser();
        $request = new Entity\Request();
        $request->setUser($user);
        $request->setAddressStart(new Entity\Address());
        $request->setAddressEnd(new Entity\Address());
        return new Response($request->toJson());
    }

    /**
     * @Route("/offers", defaults={"_format"="json"})
     * @Method({"GET"})
     */
    public function getOffersAction(){
        $offers = $this->getDoctrine()->getRepository('TappxiApiBundle:Offer')->findAll();
        $map = array('offers' => array_map(function($offer){
            return $offer->toArray();
        }, $offers));
        return new Response(json_encode($map));
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
        $user = $this->getUserRepo()->findOneBy(array('token' => $token));
        if(!$user){
            throw new \Symfony\Component\HttpKernel\Exception\HttpException(501, "token invalido");
        }
        return $user;
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




}
