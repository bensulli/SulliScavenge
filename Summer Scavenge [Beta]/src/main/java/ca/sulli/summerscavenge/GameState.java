package ca.sulli.summerscavenge;

import java.io.Serializable;

/**
 * Created by Sullivan on 3/23/2014.
 */
public class GameState implements Serializable {

    public String name;
    public String teamName;
    public int teamScore;
    public int topScore;
    public String topScoreTeam;
    Clue[] betaClueSet = {
            new Clue(1,60660775,"This is the clue for #1."),
            new Clue(2,31858634,"This is the clue for #2."),
            new Clue(3,51013409,"This is the clue for #3."),
            new Clue(4,55720161,"This is the clue for #4."),
            new Clue(5,98828642,"This is the clue for #5."),
            new Clue(6,94046376,"This is the clue for #6."),
            new Clue(7,27381797,"This is the clue for #7."),
            new Clue(8,89370771,"This is the clue for #8."),
            new Clue(9,60581440,"This is the clue for #9."),
            new Clue(10,15206641,"This is the clue for #10."),
            new Clue(11,67828057,"This is the clue for #11."),
            new Clue(12,31632278,"This is the clue for #12.")
    };

    Clue[] assignedClues = new Clue[3];

}
