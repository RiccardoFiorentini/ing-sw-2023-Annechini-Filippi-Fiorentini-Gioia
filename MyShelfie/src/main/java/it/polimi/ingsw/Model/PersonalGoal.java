package main.java.it.polimi.ingsw.Model;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class PersonalGoal {
    private int index;
    private int[] xGoals;
    private int[] yGoals;
    private final TileColor[] colorGoals =
            {TileColor.GREEN, TileColor.BLUE, TileColor.PINK, TileColor.WHITE, TileColor.ORANGE, TileColor.CYAN};

    /**
     * Class' constructor
     * @author Pasquale Gioia, Nicole Filippi
     * @param index The index of the personal goal
     * @throws IOException Error while reading "PersonalGoals.txt"
     */
    public PersonalGoal(int index) throws IOException {
        this.index=index;
        BufferedReader reader = new BufferedReader(new FileReader("MyShelfie/config/PersonalGoal.txt"));
        String line = "";
        for (int i = 0; line != null && i < 2 * (index - 1); i++) {
            line = reader.readLine();
        }
        try {
            xGoals = Arrays.stream(reader.readLine().split(",")).mapToInt(Integer::parseInt).toArray();
            yGoals = Arrays.stream(reader.readLine().split(",")).mapToInt(Integer::parseInt).toArray();
        } catch(NumberFormatException e) {
            System.out.println("Error while reading PersonalGoals.txt");
        }

    }


    /**
     * Calculates points given by personal goal
     * @author Pasquale Gioia
     * @param shelf is a player's shelf
     * @return scored points through personal goal
     */
    public int getPoints(Shelf shelf){
        int points=0, result;
        for(int i=0; i<6; i++)
            if(shelf.getTile(xGoals[i],yGoals[i]).getColor() == colorGoals[i])
                points++;

        switch (points) {
            case 3:
                result = 4;
                break;
            case 4:
                result = 6;
                break;
            case 5:
                result = 9;
                break;
            case 6:
                result = 12;
                break;
            default:
                result = points;

        }
        return result;
    }

    public int getIndex() {
        return index;
    }
}

