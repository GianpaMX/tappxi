<?php

namespace Tappxi\Bundle\ApiBundle\Entity;


use Doctrine\ORM\Mapping as ORM;

/**
 * Tappxi\Bundle\ApiBundle\Entity\Request
 *
 * @ORM\Table(name="request")
 * @ORM\Entity
 */
class Request
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
     * @var integer $status
     *
     * @ORM\Column(name="status", type="smallint", nullable=false)
     */
    private $status;

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
     * @var Address
     *
     * @ORM\ManyToOne(targetEntity="Address")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="address_start_id", referencedColumnName="id")
     * })
     */
    private $addressStart;

    /**
     * @var Address
     *
     * @ORM\ManyToOne(targetEntity="Address")
     * @ORM\JoinColumns({
     *   @ORM\JoinColumn(name="address_end_id", referencedColumnName="id")
     * })
     */
    private $addressEnd;



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
     * Set status
     *
     * @param integer $status
     * @return Request
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
     * Set user
     *
     * @param Tappxi\Bundle\ApiBundle\Entity\User $user
     * @return Request
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

    /**
     * Set addressStart
     *
     * @param Tappxi\Bundle\ApiBundle\Entity\Address $addressStart
     * @return Request
     */
    public function setAddressStart(\Tappxi\Bundle\ApiBundle\Entity\Address $addressStart = null)
    {
        $this->addressStart = $addressStart;
        return $this;
    }

    public function setAddressStartFromArray($array){
        $this->setAddressStart(Address::fromArray(array(
            'street' => $array['start_address_street'],
            'settlement' => $array['start_address_settlement'],
            'city' => array_key_exists('start_address_city', $array) ? $array['start_address_city']: '',
            'state' => array_key_exists('start_address_state', $array) ? $array['start_address_state']: '',
            'zip_code' => $array['start_address_zip_code'],
            'latitude' => $array['start_address_latitude'],
            'longitude' => $array['start_address_longitude'],
        )));
    }

    public function setAddressEndFromArray($array){
        $this->setAddressEnd(Address::fromArray(array(
            'street' => $array['end_address_street'],
            'settlement' => $array['end_address_settlement'],
            'city' => array_key_exists('end_address_city', $array) ? $array['end_address_city']: '',
            'state' => array_key_exists('end_address_state', $array) ? $array['end_address_state']: '',
            'zip_code' => $array['end_address_zip_code'],
            'latitude' => $array['end_address_latitude'],
            'longitude' => $array['end_address_longitude'],
        )));
    }

    /**
     * Get addressStart
     *
     * @return Tappxi\Bundle\ApiBundle\Entity\Address
     */
    public function getAddressStart()
    {
        return $this->addressStart;
    }

    /**
     * Set addressEnd
     *
     * @param Tappxi\Bundle\ApiBundle\Entity\Address $addressEnd
     * @return Request
     */
    public function setAddressEnd(\Tappxi\Bundle\ApiBundle\Entity\Address $addressEnd = null)
    {
        $this->addressEnd = $addressEnd;
        return $this;
    }

    /**
     * Get addressEnd
     *
     * @return Tappxi\Bundle\ApiBundle\Entity\Address
     */
    public function getAddressEnd()
    {
        return $this->addressEnd;
    }

    public function toArray(){
        return array(
            'id' => $this->getId(),
            'status' => $this->getStatus(),
            'user' => $this->getUser()->toArray(),
            'address_start' => $this->getAddressStart()->toArray(),
            'address_end' => $this->getAddressEnd()->toArray(),
        );
    }

    public function toJson(){
        return json_encode($this->toArray());
    }

    const STATUS_ACTIVE = 1;
    const STATUS_CANCELLED = 2;
    //
}