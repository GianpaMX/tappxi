<?php

namespace Tappxi\Bundle\ApiBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Tappxi\Bundle\ApiBundle\Entity\Taxi
 *
 * @ORM\Table(name="taxi")
 * @ORM\Entity
 */
class Taxi
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
     * @ORM\Column(name="name", type="string", length=45, nullable=false)
     */
    private $name;

    /**
     * @var string $tagNumber
     *
     * @ORM\Column(name="tag_number", type="string", length=16, nullable=false)
     */
    private $tagNumber;

    /**
     * @var integer $status
     *
     * @ORM\Column(name="status", type="smallint", nullable=false)
     */
    private $status;

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
     * @return Taxi
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
     * Set tagNumber
     *
     * @param string $tagNumber
     * @return Taxi
     */
    public function setTagNumber($tagNumber)
    {
        $this->tagNumber = $tagNumber;
    
        return $this;
    }

    /**
     * Get tagNumber
     *
     * @return string 
     */
    public function getTagNumber()
    {
        return $this->tagNumber;
    }

    /**
     * Set status
     *
     * @param integer $status
     * @return Taxi
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
}