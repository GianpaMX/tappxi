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
        $token = 123;
        $session = new Entity\Session();
        $session->setFbToken($fbToken);
        $session->setToken($token);
        return new Response($session->toJson());
    }

    /**
     * @Route("/taxi/request", defaults={"_format"="json"})
     * @Method({"POST"})
     */
    public function newTaxiRequestAction(){
        $request = new Entity\Request();
        $request->setUser(new Entity\User());
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


}
