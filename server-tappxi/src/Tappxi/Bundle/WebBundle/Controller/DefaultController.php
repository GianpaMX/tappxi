<?php

namespace Tappxi\Bundle\WebBundle\Controller;

use Symfony\Component\HttpFoundation\Response;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Route;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Template;
use Tappxi\Bundle\ApiBundle\Entity;

class DefaultController extends Controller
{
    /**
     * @Route("/")
     * @Template()
     */
    public function indexAction()
    {
        return array('name' => 'Vicente');
    }

    /**
     * @Route("/login")
     */
    public function loginAction(){
        $em = $this->getDoctrine()->getManager();
        if( $this->getRequest()->isMethod('POST') ){
            $username = $this->getRequest()->request->get('username');
            $user = $this->getUserRepo()->findOneBy(array('email' => $username));
            if( $user instanceof Entity\User ){

                $token = md5($user->getEmail() . microtime() . "user taxi");

                $sessions = $this->getSessionRepo()->findBy(array('user' => $user->getId()));
                array_walk($sessions, function ($session) use($em){
                    //$em->remove($session);
                });

                $session = new Entity\Session();
                $session->setUser($user);
                $session->setFbToken('user_taxi');
                $session->setToken($token);

                $em->persist($session);
                $em->flush();
                return new Response($session->toJson());
            }else{
                throw $this->createNotFoundException("El usuario no existe");
            }
        }
        return $this->render('TappxiWebBundle:Default:login.html.twig', array());
    }

    /**
     *
     * @param unknown_type $token
     * @return Entity\User
     */
    public function getUser(){
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
