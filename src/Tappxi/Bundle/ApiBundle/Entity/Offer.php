<?php

namespace Tappxi\Bundle\ApiBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Tappxi\Bundle\ApiBundle\Entity\Offer
 *
 * @ORM\Table(name="offer")
 * @ORM\Entity
 */
class Offer
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
     * @var integer $eta
     *
     * @ORM\Column(name="eta", type="integer", nullable=false)
     */
    private $eta;

    /**
     * @var float $aproximateFare
     *
     * @ORM\Column(name="aproximate_fare", type="float", nullable=false)
     */
    private $aproximateFare;

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
     * Set eta
     *
     * @param integer $eta
     * @return Offer
     */
    public function setEta($eta)
    {
        $this->eta = $eta;

        return $this;
    }

    /**
     * Get eta
     *
     * @return integer
     */
    public function getEta()
    {
        return $this->eta;
    }

    /**
     * Set aproximateFare
     *
     * @param float $aproximateFare
     * @return Offer
     */
    public function setAproximateFare($aproximateFare)
    {
        $this->aproximateFare = $aproximateFare;

        return $this;
    }

    /**
     * Get aproximateFare
     *
     * @return float
     */
    public function getAproximateFare()
    {
        return $this->aproximateFare;
    }

    /**
     * Set request
     *
     * @param Tappxi\Bundle\ApiBundle\Entity\Request $request
     * @return Offer
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
     * Set stand
     *
     * @param Tappxi\Bundle\ApiBundle\Entity\Stand $stand
     * @return Offer
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
            'eta' => $this->getEta(),
            'fear' => $this->getAproximateFare(),
            'request' => $this->getRequest()->toArray(),
            'stand' => $this->getStand()->toArray(),
        );
    }

    public function toJson(){
        return json_encode($this->toArray());
    }
}