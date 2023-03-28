package main.java.it.polimi.ingsw.Model;



public abstract class PersonalGoal {
    protected int[] xGoals;
    protected int[] yGoals;
    private final TileColor[] colourGoals =
            {TileColor.GREEN, TileColor.BLUE, TileColor.PINK, TileColor.WHITE, TileColor.ORANGE, TileColor.CYAN};



    /**
     * Calculates points given by personal goal
     * @author Pasquale Gioia
     * @param shelf is a player's shelf
     * @return scored points through personal goal
     */
    public int getPoints(Shelf shelf){
        int points=0, result=0;
        for(int i=0; i<6; i++)
            if(shelf.getTile(xGoals[i],yGoals[i]).getColor() == colourGoals[i])
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

}

