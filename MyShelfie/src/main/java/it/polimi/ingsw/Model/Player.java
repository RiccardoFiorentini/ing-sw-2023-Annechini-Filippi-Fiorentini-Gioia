package main.java.it.polimi.ingsw.Model;

import java.util.List;

import static main.java.it.polimi.ingsw.Model.Tile.BLOCKED;
import static main.java.it.polimi.ingsw.Model.Tile.EMPTY;

public class Player {
    private int turnId;

    //coordinates of first and last tiles selected
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    private String nickname;
    private boolean connected;
    private int[] pointsCommonGoal;
    private Tile[] pickedTiles;
    private int selectedColumn;
    private Shelf shelf;
    private Model model;
    private PersonalGoal personalGoal;
    private int numPickableTiles;
    private boolean pickableTiles[][];

    /**
     * Class's constructor
     * @author Fiorentini Riccardo
     * @param nickname
     * @param model
     * @param goal is the personal goal assigned to the player
     * */
    public void Player(String nickname, Model model, PersonalGoal  goal, int id){
        this.pickedTiles = new Tile[] {EMPTY, EMPTY, EMPTY};
        this.pointsCommonGoal = new int[] {0, 0};
        this.nickname = nickname;
        this.turnId = id;
        this.connected = true;
        this.shelf = new Shelf();
        this.model = model;
        this.personalGoal = goal;
        this.pickableTiles = new boolean[9][9];
        this.numPickableTiles = 0;
        this.selectedColumn = -1;
        this.x1 = -1;
        this.y1 = -1;
        this.x2 = -1;
        this.y2 = -1;
    }

    /**
     * Method to set all the variables when the player starts a new turn
     * @author Fiorentini Riccardo
     * */
    void beginTurn(){
        //Initialization of the boolean matrix describing the pickable tiles on the board
        Tile [][] tmp;
        tmp = model.getBoard().getTiles();
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(tmp[i][j] != BLOCKED ){
                    if((i>0 && tmp[i-1][j] == BLOCKED) || (j>0 && tmp[i][j-1] == BLOCKED)){
                        pickableTiles[i][j] = true;
                    }else if ((i<8 && tmp[i+1][j] == EMPTY) || (j<8 && tmp[i][j+1] == EMPTY)) {
                        pickableTiles[i][j] = true;
                    }else if ((i>0 && tmp[i-1][j] == EMPTY) || (j>0 && tmp[i][j-1] == EMPTY)) {
                        pickableTiles[i][j] = true;
                    }else if ((i<8 && tmp[i+1][j] == EMPTY) || (j<8 && tmp[i][j+1] == EMPTY)) {
                        pickableTiles[i][j] = true;
                    }else if((i == 0 || j == 0) && (tmp[i][j] != EMPTY && tmp[i][j] != BLOCKED)){
                        pickableTiles[i][j] = true;
                    }
                }
            }
        }

        //parameter initialization
        this.numPickableTiles = 0;
        this.selectedColumn = -1;
        pickedTiles[0] = EMPTY;
        pickedTiles[1] = EMPTY;
        pickedTiles[2] = EMPTY;
        this.x1 = -1;
        this.y1 = -1;
        this.x2 = -1;
        this.y2 = -1;
    }

    /**
     * Method that set the column where the player will put the tiles and max number of pickable tiles
     * @author Fiorentini Riccardo
     * @param column
     * */
    public void setSelectedColumn(int column){
        this.selectedColumn = column;
        if(shelf.spaceInCol(this.selectedColumn) < 3){
            this.numPickableTiles = shelf.spaceInCol(this.selectedColumn); //set the max number of tiles that the player can pick
        }else{
            this.numPickableTiles = 3;
        }
    }

    /**
     * Method to select tiles, both first and last. It does the checks and initializes the buffer.
     * @author Fiorentini Riccardo
     * @param x
     * @param y
     * */
    public void selectTile(int x, int y){
        if(this.x1 == -1){ //if it's the firs valid tile selected
            if(pickableTiles[x][y]){ //if it's valid it sets the first tile and update the pickableTiles matrix
                this.x1 = x;
                this.y1 = y;
                for(int i = 0; i<9; i++){
                    for(int j = 0; j<9; j++){
                        if(pickableTiles[i][j] && i!=x && j!=y){
                            pickableTiles[i][j] = false;
                        }else if(pickableTiles[i][j] && i==x && Math.abs(i-x) >= numPickableTiles){
                            pickableTiles[i][j] = false;
                        }else if(pickableTiles[i][j] && j==y && Math.abs(j-y) >= numPickableTiles){
                            pickableTiles[i][j] = false;
                        }else if(pickableTiles[i][j] && i==x && Math.abs(i-x) == 2 && !pickableTiles[(i+x)/2][j]){
                            pickableTiles[i][j] = false;
                        }else if(pickableTiles[i][j] && j==y && Math.abs(j-y) == 2 && !pickableTiles[i][(j+y)/2]){
                            pickableTiles[i][j] = false;
                        }
                    }
                }
            }
        }else{
            if(pickableTiles[x][y]) { //if the last tile selected is valid it set the last tile and put the selected tiles in the buffer
                this.x2 = x;
                this.y2 = y;
                if(this.x1 == this.x2){
                    int min = Math.min(this.y1, this.y2);
                    for(int i = min; i <= Math.max(this.y1, this.y2); i++){
                        pickedTiles[i-min] = this.model.getBoard().pickTile(this.x1, i);
                    }
                }else{
                    int min = Math.min(this.x1, this.x2);
                    for(int i = min; i <= Math.max(this.x1, this.x2); i++){
                        pickedTiles[i-min] = this.model.getBoard().pickTile(i, this.y1);
                    }
                }
            }
        }
    }


    /**
     * Method to put the selected tiles in the shelf from the buffer
     * @author Fiorentini Riccardo
     * @param index
     * */
    public void putInColumn(int index){
        if(index>=0 && index <= 2 && pickedTiles[index] != EMPTY){
            this.shelf.putTile(pickedTiles[index], this.selectedColumn);
            pickedTiles[index] = EMPTY;
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

    public void setPointsCommonGoal(int index, int point){
        this.pointsCommonGoal[index] = point;
    }

    public void setPersonalGoal(PersonalGoal goal){
        this.personalGoal = goal;
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

    public int getX1(){
        return x1;
    }

    public int getX2(){
        return x2;
    }

    public int getY1(){
        return y1;
    }
    public int getY2(){
        return y2;
    }

    public int getNumPickableTiles(){
        return numPickableTiles;
    }

    public boolean[][] getPickableTiles() {
        return pickableTiles;
    }
}
