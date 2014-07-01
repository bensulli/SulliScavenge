<?php

namespace Entities;

/**
 * @Entity
 */
class User
{
    /**
     * @Id
     * @Column(type="integer")
     * @GeneratedValue
     */
    protected $id;

    /**
     * @Column(type="string", length=255)
     */
    protected $username;

    /**
     * @ManyToOne(targetEntity="Team")
     */
    protected $team;

    /**
     * @Column(type="datetime")
     */
    protected $created;

    /**
     * @Column(type="datetime")
     */
    protected $last_update;

    /**
     * @Column(type="integer")
     */
    protected $codes_found;

    /**
     * @param mixed $id
     * @returns User
     */
    public function setId($id)
    {
        $this->id = $id;
        return $this;
    }

    /**
     * @return mixed
     */
    public function getId()
    {
        return $this->id;
    }

    /**
     * @param mixed $username
     * @returns User
     */
    public function setUsername($username)
    {
        $this->username = $username;
        return $this;
    }

    /**
     * @return mixed
     */
    public function getUsername()
    {
        return $this->username;
    }

    /**
     * @param mixed $team
     * @returns User
     */
    public function setTeam($team)
    {
        $this->team = $team;
        return $this;
    }

    /**
     * @return mixed
     */
    public function getTeam()
    {
        return $this->team;
    }

    /**
     * @param mixed $created
     * @returns User
     */
    public function setCreated($created)
    {
        $this->created = $created;
        return $this;
    }

    /**
     * @return mixed
     */
    public function getCreated()
    {
        return $this->created;
    }

    /**
     * @param mixed $last_update
     * @returns User
     */
    public function setLastUpdate($last_update)
    {
        $this->last_update = $last_update;
        return $this;
    }

    /**
     * @return mixed
     */
    public function getLastUpdate()
    {
        return $this->last_update;
    }

    /**
     * @param mixed $codes_found
     * @returns User
     */
    public function setCodesFound($codes_found)
    {
        $this->codes_found = $codes_found;
        return $this;
    }

    /**
     * @return mixed
     */
    public function getCodesFound()
    {
        return $this->codes_found;
    }

}