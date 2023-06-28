package it.polimi.ingsw.Model;

public abstract class CommonGoal {
    private int pointsLeft;
    private final int numPlayers;
    /**
     * Class's constructor
     * @author Alessandro Annechini
     * @param numPlayers The number of players
     */
    public CommonGoal(int numPlayers){
        this.numPlayers=numPlayers;
        pointsLeft=8;
    }

    /**
     * This method checks if a shelf satisfies the specific common goal
     * @author Alessandro Annechini
     * @param shelf The shelf to be checked
     * @return True if the shelf satisfies the constraints, false otherwise
     */
    public abstract boolean check(Shelf shelf);

    public int getPointsLeft() {
        return pointsLeft;
    }

    /**
     * This method returns the points obtained by achieving this common goal, decreasing the number of points remaining
     * @author Alessandro Annechini
     * @return The amount of points won by the invoker
     */
    public int pullPoints(){
        int ret=pointsLeft;
        if(numPlayers==4){
            if(pointsLeft>0) pointsLeft-=2;
        }
        else if (numPlayers==3) {
            if(pointsLeft>4) pointsLeft-=2;
            else pointsLeft = 0;
        }
        else if (numPlayers==2) {
            if(pointsLeft>0) pointsLeft-=4;
        }
        return ret;
    }

    /**
     * This method returns the index N in CommonGoalN
     * @author Nicole Filippi
     * @return the index of the Common Goal (from 1 to 12)
     */
    public abstract int getIndex();

    public abstract String getDescription();
}

