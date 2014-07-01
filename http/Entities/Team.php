<?php
use Doctrine\ORM\Mapping as ORM;
/**
 * @ORM\Entity
 */
class Team
{
    /**
     * @ORM\Id
     * @ORM\Column(type="integer")
     * @ORM\GeneratedValue
     */
    protected $id;

    /**
     * @ORM\Column(type="string", length=255)
     */
    protected $team_name;

    /**
     * @ORM\Column(type="integer")
     */
    //protected $total_members; // Removed because counting can happen

    /**
     * @ORM\Column(type="datetime")
     */
    protected $created;

    /**
     * @ORM\Column(type="datetime")
     */
    protected $last_update;

    /**
     * @ORM\Column(type="integer")
     */
    //protected $total_codes_found; // Removed because counting can happen

    /**
     * @param mixed $id
     * @return Team
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
     * @return Team
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
     * @return Team
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
     * @return Team
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
     * @return Team
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
     * @return Team
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