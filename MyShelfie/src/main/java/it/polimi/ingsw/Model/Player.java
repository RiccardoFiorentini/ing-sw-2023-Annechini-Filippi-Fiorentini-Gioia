package main.java.it.polimi.ingsw.Model;

import java.util.List;

import static main.java.it.polimi.ingsw.Model.Tile.BLOCKED;
import static main.java.it.polimi.ingsw.Model.Tile.EMPTY;

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
    private boolean pickableTiles[][];

    /**
     * Class's constructor
     * @author Fiorentini Riccardo
     * @param nickname
     * @param model
     * @param goal is the personal goal assigned to the player
     * */
    public void Player(String nickname, Model model, PersonalGoal  goal){
        pickedTiles = new Tile[] {EMPTY, EMPTY, EMPTY};
        pointsCommonGoal = new int[2];
        this.nickname = nickname;
        //turnId chi lo stabilisce?
        connected = true;
        shelf = new Shelf();
        this.model = model;
        this.personalGoal = goal;
        pickableTiles = new boolean[9][9];

        Tile tmp[][];
        tmp = model.getBoard().getTiles();
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(tmp[i][j] != BLOCKED ){
                    if((i>0 && tmp[i-1][j] == BLOCKED) || (j>0 && tmp[i][j-1] == BLOCKED)){
                        pickableTiles[i][j] = true;
                    } else if ((i>0 && tmp[i-1][j] == EMPTY) || (j>0 && tmp[i][j-1] == EMPTY)) {
                        pickableTiles[i][j] = true;
                    } else if ((i<8 && tmp[i+1][j] == EMPTY) || (j<8 && tmp[i][j+1] == EMPTY)) {
                        pickableTiles[i][j] = true;
                    }
                }
            }
        }
    }

    /**
     * Method that set the column where the player will put the tiles and initializes the matrix for
     * @author Fiorentini Riccardo
     * @param column
     * */
    public void setSelectedColumn(int column){
        this.selectedColumn = column;
        Tile tmp[][];
        tmp = model.getBoard().getTiles();
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(tmp[i][j] != BLOCKED ){
                    if((i>0 && tmp[i-1][j] == BLOCKED) || (j>0 && tmp[i][j-1] == BLOCKED)){
                        pickableTiles[i][j] = true;
                    } else if ((i>0 && tmp[i-1][j] == EMPTY) || (j>0 && tmp[i][j-1] == EMPTY)) {
                        pickableTiles[i][j] = true;
                    } else if ((i<8 && tmp[i+1][j] == EMPTY) || (j<8 && tmp[i][j+1] == EMPTY)) {
                        pickableTiles[i][j] = true;
                    }else{
                        pickableTiles[i][j] = true;
                    }
                }
            }
        }
        pickedTiles[0] = EMPTY;
        pickedTiles[1] = EMPTY;
        pickedTiles[2] = EMPTY;
    }

    /**
     * Method to check if the selected tiles are pickable,
     * the order in the lists is the order of insertion in the column (lower the index lower the position in the column)
     * @author Fiorentini Riccardo
     * @param x list of x coordinates
     * @param y list of y coordinates
     * */
    private boolean checkSelectedTiles(List<Integer> x, List<Integer> y){
        int numPicked = x.size();
        boolean isCorrect = true;
        if(numPicked <= shelf.spaceInCol(selectedColumn) && numPicked <= 3){
            for(int i = 0; i<numPicked && isCorrect; i++){
                isCorrect = pickableTiles[x.get(i)][y.get(i)];
            }
            if(numPicked == 3 && isCorrect){
                isCorrect = (x.get(0) == x.get(1) && x.get(1) == x.get(2)) || (y.get(0) == y.get(1) && y.get(1) == y.get(2));
            }else if(numPicked == 2 && isCorrect){
                isCorrect = (x.get(0) == x.get(1)) || (y.get(0) == y.get(1));
            }
            return isCorrect;
        }else {
            return false;
        }
    }

    /**
     * Method that actually pick the tiles from the board,
     * @author Fiorentini Riccardo
     * @param x list of x coordinates
     * @param y list of y coordinates
     * */
    public void pickFromBoard(List<Integer> x, List<Integer> y){
        if(checkSelectedTiles(x, y)){
            for(int i = 0; i<x.size(); i++){
                pickedTiles[i] = model.getBoard().pickTile(x.get(i), y.get(i));
            }
        }
    }

    /**
     * Method to put the selected tiles in the shelf
     * @author Fiorentini Riccardo
     * */
    public void putInColumn(){
        int i = 0;
        while(pickedTiles[i] != EMPTY){
            this.shelf.putTile(pickedTiles[i], this.selectedColumn);
            i++;
        }
    }

    /**
     * Method to write a message
     * @author Fiorentini Riccardo
     * @param receiver list of player I am texting to
     * @param text message I am sending
     * */
    public void writeMessage(List<Player> receiver, String text){
        if(receiver.size() == this.model.getNumPlayers()){
            model.getChat().writeMessage(this, text);
        }else{
            for(int i = 0; i < receiver.size(); i++) {
                model.getChat().writeMessage(this, receiver.get(i), text);
            }
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public int getTurnId(){
        return turnId;
    }

    public String getNickname() {
        return nickname;
    }

    public int[] getPointsCommonGoal() {
        return pointsCommonGoal;
    }

    public Tile[] getPickedTiles() {
        return pickedTiles;
    }

    public int getSelectedColumn() {
        return selectedColumn;
    }

    public Shelf getShelf() {
        return shelf;
    }

    public Model getModel() {
        return model;
    }

    public PersonalGoal getPersonalGoal() {
        return personalGoal;
    }
}
