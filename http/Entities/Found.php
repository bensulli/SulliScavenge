<?php

namespace Entities;

/**
 * @Entity
 */
class Found
{
    /**
     * @Id
     * @Column(type="integer")
     * @GeneratedValue
     */
    protected $id;

    /**
     * @Column(type="datetime")
     */
    protected $time;

    /**
     * @Column(type="integer")
     */
    protected $code_reported;

    /**
     * @Column(type="boolean")
     */
    protected $code_valid;

    /**
     * @Column(type="boolean")
     */
    protected $code_new;

    /**
     * @ManyToOne(targetEntity="User")
     */
    protected $user;

    /**
     * @ManyToOne(targetEntity="Team")
     */
    protected $team;

    /**
     * @Column(type="string", length=255)
     */
    protected $event;

    /**
     * @return mixed
     */
    public function getId()
    {
        return $this->id;
    }

    /**
     * @param mixed $time
     * @returns Found
     */
    public function setTime($time)
    {
        $this->time = $time;
        return $this;
    }

    /**
     * @return mixed
     */
    public function getTime()
    {
        return $this->time;
    }

    /**
     * @param mixed $code_reported
     * @returns Found
     */
    public function setCodeReported($code_reported)
    {
        $this->code_reported = $code_reported;
        return $this;
    }

    /**
     * @return mixed
     */
    public function getCodeReported()
    {
        return $this->code_reported;
    }

    /**
     * @param mixed $code_valid
     * @returns Found
     */
    public function setCodeValid($code_valid)
    {
        $this->code_valid = $code_valid;
        return $this;
    }

    /**
     * @return mixed
     */
    public function getCodeValid()
    {
        return $this->code_valid;
    }

    /**
     * @param mixed $code_new
     * @returns Found
     */
    public function setCodeNew($code_new)
    {
        $this->code_new = $code_new;
        return $this;
    }

    /**
     * @return mixed
     */
    public function getCodeNew()
    {
        return $this->code_new;
    }

    /**
     * @param mixed $user
     * @returns Found
     */
    public function setUser($user)
    {
        $this->user = $user;
        return $this;
    }

    /**
     * @return mixed
     */
    public function getUser()
    {
        return $this->user;
    }

    /**
     * @param mixed $team
     * @returns Found
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
     * @param mixed $event
     * @returns Found
     */
    public function setEvent($event)
    {
        $this->event = $event;
        return $this;
    }

    /**
     * @return mixed
     */
    public function getEvent()
    {
        return $this->event;
    }




}