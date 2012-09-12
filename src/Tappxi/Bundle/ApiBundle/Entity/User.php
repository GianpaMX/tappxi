<?php

namespace Tappxi\Bundle\ApiBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Tappxi\Bundle\ApiBundle\Entity\User
 *
 * @ORM\Table(name="user")
 * @ORM\Entity
 */
class User
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
     * @var string $name
     *
     * @ORM\Column(name="name", type="string", length=145, nullable=false)
     */
    private $name;

    /**
     * @var string $email
     *
     * @ORM\Column(name="email", type="string", length=120, nullable=false)
     */
    private $email;

    /**
     * @var float $balance
     *
     * @ORM\Column(name="balance", type="float", nullable=false)
     */
    private $balance;

    /**
     * @var integer $status
     *
     * @ORM\Column(name="status", type="smallint", nullable=false)
     */
    private $status;

    /**
     * @var integer $role
     *
     * @ORM\Column(name="role", type="smallint", nullable=true)
     */
    private $role;

    /**
     * @var Stand
     *
     * @ORM\ManyToOne(targetEntity="Stand")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="stand_id", referencedColumnName="id")
     * })
     */
    private $stand;


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
     * Set name
     *
     * @param string $name
     * @return User
     */
    public function setName($name)
    {
        $this->name = $name;

        return $this;
    }

    /**
     * Get name
     *
     * @return string
     */
    public function getName()
    {
        return $this->name;
    }

    /**
     * Set email
     *
     * @param string $email
     * @return User
     */
    public function setEmail($email)
    {
        $this->email = $email;

        return $this;
    }

    /**
     * Get email
     *
     * @return string
     */
    public function getEmail()
    {
        return $this->email;
    }

    /**
     * Set balance
     *
     * @param float $balance
     * @return User
     */
    public function setBalance($balance)
    {
        $this->balance = $balance;

        return $this;
    }

    /**
     * Get balance
     *
     * @return float
     */
    public function getBalance()
    {
        return $this->balance;
    }

    /**
     * Set status
     *
     * @param integer $status
     * @return User
     */
    public function setStatus($status)
    {
        $this->status = $status;

        return $this;
    }

    /**
     * Get status
     *
     * @return integer
     */
    public function getStatus()
    {
        return $this->status;
    }

    /**
     * Set role
     *
     * @param integer $role
     * @return User
     */
    public function setRole($role)
    {
        $this->role = $role;

        return $this;
    }

    /**
     * Get role
     *
     * @return integer
     */
    public function getRole()
    {
        return $this->role;
    }

    /**
     * Set stand
     *
     * @param Tappxi\Bundle\ApiBundle\Entity\Stand $stand
     * @return Taxi
     */
    public function setStand(\Tappxi\Bundle\ApiBundle\Entity\Stand $stand = null)
    {
        $this->stand = $stand;

        return $this;
    }

    /**
     * Get stand
     *
     * @return Tappxi\Bundle\ApiBundle\Entity\Stand
     */
    public function getStand()
    {
        return $this->stand;
    }

    public function toArray(){
        return array(
            'id' => $this->getId(),
            'name' => $this->getName(),
            'role' => $this->getRole(),
            'status' => $this->getStatus(),
            'email' => $this->getEmail(),
            'balance' => $this->getBalance(),
            'stand' => $this->getStand() ? $this->getStand()->toArray() : null,
        );
    }

    public function toJson(){
        return json_encode($this->toArray());
    }


}