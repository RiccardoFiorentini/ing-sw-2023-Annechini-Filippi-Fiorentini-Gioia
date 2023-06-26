package it.polimi.ingsw.Model;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class PersonalGoal {
    private int index;
    private int[] rowGoals;
    private int[] colGoals;
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/config/PersonalGoal.txt")));
        String line = "";
        for (int i = 0; line != null && i < 2 * (index - 1); i++) {
            line = reader.readLine();
        }
        try {
            rowGoals = Arrays.stream(reader.readLine().split(",")).mapToInt(Integer::parseInt).toArray();
            colGoals = Arrays.stream(reader.readLine().split(",")).mapToInt(Integer::parseInt).toArray();
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
            if(shelf.getTile(rowGoals[i],colGoals[i]).getColor() == colorGoals[i])
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

    /**
     * Creates a matrix of tiles for the personal goal
     * @author Nicole Filippi
     * @return the matrix of TileColor
     */
    public TileColor[][] toBean(){
        TileColor[][] shelfPG= new TileColor[6][5];

        for(int i=0; i<6; i++){
            for(int j=0; j<5; j++){
                shelfPG[i][j]=TileColor.EMPTY;
            }
        }
        for(int i=0; i<6; i++){
            shelfPG[rowGoals[i]][colGoals[i]]=colorGoals[i];
        }
        return shelfPG;
    }

    public int getIndex() {
        return index;
    }
}

