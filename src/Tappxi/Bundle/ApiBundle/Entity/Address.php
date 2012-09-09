<?php

namespace Tappxi\Bundle\ApiBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Tappxi\Bundle\ApiBundle\Entity\Address
 *
 * @ORM\Table(name="address")
 * @ORM\Entity
 */
class Address
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
     * @var string $street
     *
     * @ORM\Column(name="street", type="string", length=90, nullable=false)
     */
    private $street;

    /**
     * @var string $settlement
     *
     * @ORM\Column(name="settlement", type="string", length=90, nullable=false)
     */
    private $settlement;

    /**
     * @var string $city
     *
     * @ORM\Column(name="city", type="string", length=90, nullable=false)
     */
    private $city;

    /**
     * @var string $state
     *
     * @ORM\Column(name="state", type="string", length=45, nullable=false)
     */
    private $state;

    /**
     * @var string $zipCode
     *
     * @ORM\Column(name="zip_code", type="string", length=5, nullable=false)
     */
    private $zipCode;

    /**
     * @var float $latitude
     *
     * @ORM\Column(name="latitude", type="float", nullable=false)
     */
    private $latitude;

    /**
     * @var float $longitude
     *
     * @ORM\Column(name="longitude", type="float", nullable=false)
     */
    private $longitude;



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
     * Set street
     *
     * @param string $street
     * @return Address
     */
    public function setStreet($street)
    {
        $this->street = $street;

        return $this;
    }

    /**
     * Get street
     *
     * @return string
     */
    public function getStreet()
    {
        return $this->street;
    }

    /**
     * Set settlement
     *
     * @param string $settlement
     * @return Address
     */
    public function setSettlement($settlement)
    {
        $this->settlement = $settlement;

        return $this;
    }

    /**
     * Get settlement
     *
     * @return string
     */
    public function getSettlement()
    {
        return $this->settlement;
    }

    /**
     * Set city
     *
     * @param string $city
     * @return Address
     */
    public function setCity($city)
    {
        $this->city = $city;

        return $this;
    }

    /**
     * Get city
     *
     * @return string
     */
    public function getCity()
    {
        return $this->city;
    }

    /**
     * Set state
     *
     * @param string $state
     * @return Address
     */
    public function setState($state)
    {
        $this->state = $state;

        return $this;
    }

    /**
     * Get state
     *
     * @return string
     */
    public function getState()
    {
        return $this->state;
    }

    /**
     * Set zipCode
     *
     * @param string $zipCode
     * @return Address
     */
    public function setZipCode($zipCode)
    {
        $this->zipCode = $zipCode;

        return $this;
    }

    /**
     * Get zipCode
     *
     * @return string
     */
    public function getZipCode()
    {
        return $this->zipCode;
    }

    /**
     * Set latitude
     *
     * @param float $latitude
     * @return Address
     */
    public function setLatitude($latitude)
    {
        $this->latitude = $latitude;

        return $this;
    }

    /**
     * Get latitude
     *
     * @return float
     */
    public function getLatitude()
    {
        return $this->latitude;
    }

    /**
     * Set longitude
     *
     * @param float $longitude
     * @return Address
     */
    public function setLongitude($longitude)
    {
        $this->longitude = $longitude;

        return $this;
    }

    /**
     * Get longitude
     *
     * @return float
     */
    public function getLongitude()
    {
        return $this->longitude;
    }

    public function toArray(){
        return array(
            'id' => $this->getId(),
            'street' => $this->getStreet(),
            'settlement' => $this->getSettlement(),
            'city' => $this->getCity(),
            'state' => $this->getState(),
            'zip_code' => $this->getZipCode(),
            'latitude' => $this->getLatitude(),
            'longitude' => $this->getLongitude(),
        );
    }

    public static function fromArray($array){
        $address = new Address();
        $address->setStreet($array['street']);
        $address->setSettlement($array['settlement']);
        $address->setCity($array['city']);
        $address->setState($array['state']);
        $address->setZipCode($array['zip_code']);
        $address->setLatitude($array['latitude']);
        $address->setLongitude($array['longitude']);
        return $address;
    }

    public function toJson(){
        return json_encode($this->toArray());
    }
}