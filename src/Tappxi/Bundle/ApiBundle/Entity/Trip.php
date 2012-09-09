<?php

namespace Tappxi\Bundle\ApiBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Tappxi\Bundle\ApiBundle\Entity\Trip
 *
 * @ORM\Table(name="trip")
 * @ORM\Entity
 */
class Trip
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
     * @var float $fare
     *
     * @ORM\Column(name="fare", type="float", nullable=false)
     */
    private $fare;

    /**
     * @var integer $status
     *
     * @ORM\Column(name="status", type="smallint", nullable=false)
     */
    private $status;

    /**
     * @var Request
     *
     * @ORM\ManyToOne(targetEntity="Request")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="request_id", referencedColumnName="id")
     * })
     */
    private $request;

    /**
     * @var Offer
     *
     * @ORM\ManyToOne(targetEntity="Offer")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="offer_id", referencedColumnName="id")
     * })
     */
    private $offer;

    /**
     * @var Taxi
     *
     * @ORM\ManyToOne(targetEntity="Taxi")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="taxi_id", referencedColumnName="id")
     * })
     */
    private $taxi;

    /**
     * @var Movement
     *
     * @ORM\ManyToOne(targetEntity="Movement")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="movement_id", referencedColumnName="id")
     * })
     */
    private $movement;



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
     * Set fare
     *
     * @param float $fare
     * @return Trip
     */
    public function setFare($fare)
    {
        $this->fare = $fare;

        return $this;
    }

    /**
     * Get fare
     *
     * @return float
     */
    public function getFare()
    {
        return $this->fare;
    }

    /**
     * Set status
     *
     * @param integer $status
     * @return Trip
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
     * Set request
     *
     * @param Tappxi\Bundle\ApiBundle\Entity\Request $request
     * @return Trip
     */
    public function setRequest(\Tappxi\Bundle\ApiBundle\Entity\Request $request = null)
    {
        $this->request = $request;

        return $this;
    }

    /**
     * Get request
     *
     * @return Tappxi\Bundle\ApiBundle\Entity\Request
     */
    public function getRequest()
    {
        return $this->request;
    }

    /**
     * Set offer
     *
     * @param Tappxi\Bundle\ApiBundle\Entity\Offer $offer
     * @return Trip
     */
    public function setOffer(\Tappxi\Bundle\ApiBundle\Entity\Offer $offer = null)
    {
        $this->offer = $offer;

        return $this;
    }

    /**
     * Get offer
     *
     * @return Tappxi\Bundle\ApiBundle\Entity\Offer
     */
    public function getOffer()
    {
        return $this->offer;
    }

    /**
     * Set taxi
     *
     * @param Tappxi\Bundle\ApiBundle\Entity\Taxi $taxi
     * @return Trip
     */
    public function setTaxi(\Tappxi\Bundle\ApiBundle\Entity\Taxi $taxi = null)
    {
        $this->taxi = $taxi;

        return $this;
    }

    /**
     * Get taxi
     *
     * @return Tappxi\Bundle\ApiBundle\Entity\Taxi
     */
    public function getTaxi()
    {
        return $this->taxi;
    }

    /**
     * Set movement
     *
     * @param Tappxi\Bundle\ApiBundle\Entity\Movement $movement
     * @return Trip
     */
    public function setMovement(\Tappxi\Bundle\ApiBundle\Entity\Movement $movement = null)
    {
        $this->movement = $movement;

        return $this;
    }

    /**
     * Get movement
     *
     * @return Tappxi\Bundle\ApiBundle\Entity\Movement
     */
    public function getMovement()
    {
        return $this->movement;
    }

    public function toArray(){
        return array(
            'id' => $this->getId(),
            'fare' => $this->getFare(),
            'offer' => $this->getOffer()->toArray(),
            'request' => $this->getRequest()->toArray(),
            'status' => $this->getRequest()->getStatus(),
            'movement' => $this->getMovement() ? $this->getMovement()->toArra() : null,
            'taxi' => $this->getTaxi() ? $this->getTaxi()->toArray(): null,
        );
    }

    public function toJson(){
        return json_encode($this->toArray());
    }

    const STATUS_ON_WAY = 1;
    const STATUS_UNCONFIRMED = 2;
    const STATUS_PAYED = 3;
}