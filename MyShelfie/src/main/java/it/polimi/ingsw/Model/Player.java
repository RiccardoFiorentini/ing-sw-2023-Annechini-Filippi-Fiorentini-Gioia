package main.java.it.polimi.ingsw.Model;

/**
 **/
public class Player {
    private int numTurn;
    private String nickame;
    private boolean connected;
    private int[] pointsCommonGoal;
    private Tile[] pickedTiles;
    private int selectedColumn;
    private Shelf shelf;
    private Model model;
    private PersonalGoal personalGoal;

    public void Player(String nick){
        pickedTiles = new Tile[3];
        pointsCommonGoal = new int[2];
    }

    public void pickFromBoard(int x, int y){

    }

    public void putInColumn(int column){

    }

    public void endTurn(){

    }

    public void writeMessage(Player receiver, String text){

    }

    public int getNumTurn(){

    }

    public String getNickame() {

    }

    public boolean isConnected() {

    }

    public int[] getPointsCommonGoal() {

    }

    public Tile[] getPickedTiles() {

    }

    public int getSelectedColumn() {

    }

    public Shelf getShelf() {

    }

    public Model getModel() {

    }

    public PersonalGoal getPersonalGoal() {

    }
}
