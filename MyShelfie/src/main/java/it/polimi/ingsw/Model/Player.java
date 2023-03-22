package main.java.it.polimi.ingsw.Model;

public class Player {
    int numTurn;
    String nickame;
    boolean connected;
    int[] pointsCommonGoal = new int[2];
    Tile[] pickedTiles = new Tile[3];
    int selectedColumn;
    Shelf shelf;
    Model model;
    PersonalGoal personalGoal;

    public void Player(String nick){

    }

    public void pickFromBoard(int x, int y){

    }

    public void putInColumn(int column){

    }

    public void endTurn(){

    }

    public void writeMessage(Player receiver, String text){

    }

}
