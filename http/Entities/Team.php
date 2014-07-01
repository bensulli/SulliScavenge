<?php

/**
 * @Entity
 */
class Team
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
    protected $team_name;

    /**
     * @Column(type="integer")
     */
    protected $total_members;

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
    protected $total_codes_found;

    /**
     * @param mixed $id
     * @returns Team
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
     * @param mixed $team_name
     * @returns Team
     */
    public function setTeamName($team_name)
    {
        $this->team_name = $team_name;
        return $this;
    }

    /**
     * @return mixed
     */
    public function getTeamName()
    {
        return $this->team_name;
    }

    /**
     * @param mixed $total_members
     * @returns Team
     */
    public function setTotalMembers($total_members)
    {
        $this->total_members = $total_members;
        return $this;
    }

    /**
     * @return mixed
     */
    public function getTotalMembers()
    {
        return $this->total_members;
    }

    /**
     * @param mixed $created
     * @returns Team
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
     * @returns Team
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
     * @param mixed $total_codes_found
     * @returns Team
     */
    public function setTotalCodesFound($total_codes_found)
    {
        $this->total_codes_found = $total_codes_found;
        return $this;
    }

    /**
     * @return mixed
     */
    public function getTotalCodesFound()
    {
        return $this->total_codes_found;
    }


}