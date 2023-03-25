package main.java.it.polimi.ingsw.Model;

public interface PersonalGoal {
    /**
     * Calculates points given by personal goal
     * @author Pasquale Gioia
     * @param shelf is a player's shelf
     * @return scored points through personal goal
     */
    int getPoints(Shelf shelf);

}

