<?php

namespace Tappxi\Bundle\ApiBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Tappxi\Bundle\ApiBundle\Entity\Session
 *
 * @ORM\Table(name="session")
 * @ORM\Entity
 */
class Session
{
    /**
     * @var integer $id
     *
     * @ORM\Column(name="id", type="integer", nullable=false)
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="IDENTITY")
     */
    private $id;

    /**
     * @var string $fbToken
     *
     * @ORM\Column(name="fb_token", type="string", length=255, nullable=false)
     */
    private $fbToken;

    /**
     * @var string $token
     *
     * @ORM\Column(name="token", type="string", length=255, nullable=false)
     */
    private $token;

    /**
     * @var User
     *
     * @ORM\ManyToOne(targetEntity="User")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="user_id", referencedColumnName="id")
     * })
     */
    private $user;



    /**
     * Get id
     *
     * @return integer
     */
    public function getId()
    {
        return $this->id;
    }

    /**
     * Set fbToken
     *
     * @param string $fbToken
     * @return Session
     */
    public function setFbToken($fbToken)
    {
        $this->fbToken = $fbToken;

        return $this;
    }

    /**
     * Get fbToken
     *
     * @return string
     */
    public function getFbToken()
    {
        return $this->fbToken;
    }

    /**
     * Set token
     *
     * @param string $token
     * @return Session
     */
    public function setToken($token)
    {
        $this->token = $token;

        return $this;
    }

    /**
     * Get token
     *
     * @return string
     */
    public function getToken()
    {
        return $this->token;
    }

    /**
     * Set user
     *
     * @param Tappxi\Bundle\ApiBundle\Entity\User $user
     * @return Session
     */
    public function setUser(\Tappxi\Bundle\ApiBundle\Entity\User $user = null)
    {
        $this->user = $user;

        return $this;
    }

    /**
     * Get user
     *
     * @return Tappxi\Bundle\ApiBundle\Entity\User
     */
    public function getUser()
    {
        return $this->user;
    }

    public function toArray(){
        return array(
            'fb_token' => $this->getFbToken(),
            'token' => $this->getToken(),
        );
    }

    public function toJson(){
        return json_encode($this->toArray());
    }
}