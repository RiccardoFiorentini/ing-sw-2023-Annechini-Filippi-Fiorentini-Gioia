package main.java.it.polimi.ingsw.Model;

/**
 **/
public class Player {
    private int turnId;
    private String nickname;
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

    public void writeMessage(Player receiver, String text){

    }

    public boolean isConnected() {

    }

    public int getTurnId(){

    }

    public String getNickname() {

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
