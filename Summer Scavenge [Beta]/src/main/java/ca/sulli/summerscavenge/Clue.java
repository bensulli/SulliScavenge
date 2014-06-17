package ca.sulli.summerscavenge;

/**
 * Created by Sullivan on 3/23/2014.
 */
public class Clue {

    public int id;
    public int locationID;
    public String easyClue;
    public String mediumClue;
    public String hardClue;
    public boolean picked;

    public Clue(int id, int locationID, String clue)
    {
        this.id = id;
        this.locationID = locationID;
        this.mediumClue = clue;

        picked = false;

    }
}

/*
public Bicycle(int startCadence, int startSpeed, int startGear) {
    gear = startGear;
    cadence = startCadence;
    speed = startSpeed;
}
 */