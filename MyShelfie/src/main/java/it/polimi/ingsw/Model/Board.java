package main.java.it.polimi.ingsw.Model;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Board {
    private Map<Tile, Integer> tilesRemaining;
    private Tile[][] tiles;
    private int numPlayers;

    /**
     * Class' constructor
     * @author Pasquale Gioia
     * @param numPlayers is the number of players that joined the game
     *
     */
    public Board(int numPlayers) {
        tiles = new Tile[9][9];
        this.numPlayers = numPlayers;
        tilesRemaining = new HashMap<>();

        //Board initialization without considering numPlayers
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if      ((!(i<4 && j<3) || (i==3 && j==2)) &&
                        (!(i<3 && j>4) || (i==2 && j==5)) &&
                        (!(i>4 && j>5) ||  (i==5 && j==6)) &&
                        (!(i>5 && j<4) || (i==6 && j==3)))

                    tiles[i][j] = Tile.EMPTY;
                else
                    tiles[i][j] = Tile.BLOCKED;
            }
        }

        //Board initialization considering numPlayers -- removed redundant initialization
        switch(numPlayers){
            case 4:
                tiles[2][6]=Tile.EMPTY;
                tiles[6][6]=Tile.EMPTY;
                tiles[6][2]=Tile.EMPTY;
                tiles[2][2]=Tile.EMPTY;

                tiles[1][5]=Tile.EMPTY;
                tiles[5][7]=Tile.EMPTY;
                tiles[7][3]=Tile.EMPTY;
                tiles[3][1]=Tile.EMPTY;

            case 3:
                tiles[2][6]=Tile.EMPTY;
                tiles[6][6]=Tile.EMPTY;
                tiles[6][2]=Tile.EMPTY;
                tiles[2][2]=Tile.EMPTY;

                tiles[0][4]=Tile.BLOCKED;
                tiles[4][8]=Tile.BLOCKED;
                tiles[8][4]=Tile.BLOCKED;
                tiles[4][0]=Tile.BLOCKED;
            
            default:
                tiles[0][3]=Tile.BLOCKED;
                tiles[3][8]=Tile.BLOCKED;
                tiles[8][5]=Tile.BLOCKED;
                tiles[5][0]=Tile.BLOCKED;

                tiles[0][4]=Tile.BLOCKED;
                tiles[4][8]=Tile.BLOCKED;
                tiles[8][4]=Tile.BLOCKED;
                tiles[4][0]=Tile.BLOCKED;
        }



        //Bag initialization
        for(Tile t: Tile.values()){
            if     (t == Tile.GREEN1 || t == Tile.BLUE1 ||
                    t == Tile.WHITE1 || t == Tile.PINK1 ||
                    t == Tile.ORANGE1 || t == Tile.CYAN1)
                tilesRemaining.put(t, 8);

            else
                tilesRemaining.put(t, 7);
        }
    }

    /**
     * Refills the board and decreases the amount of tiles left in the bag
     * @author Pasquale Gioia
     *
     */
    public void refill(){
        Random rTile = new Random();
        int rTileIndex;

        for(int i=0; i<9;i++)
            for(int j=0; j<9; j++)
                if(tiles[i][j] == Tile.EMPTY) {
                    //Might be an infinite loop if no tiles are left in the bag to refill?
                    do {
                        rTileIndex = rTile.nextInt(Tile.values().length - 2);
                    } while( tilesRemaining.get(Tile.values()[rTileIndex]) <= 0 );

                    tiles[i][j] = Tile.values()[rTileIndex];
                    tilesRemaining.put(Tile.values()[rTileIndex], tilesRemaining.get(Tile.values()[rTileIndex]) -1);
                }
    }

    /**
     * Checks if the board needs to be refilled
     * @author Pasquale Gioia
     * @return true if the board needs to be refilled
     *
     */
    public boolean checkFill(){
        boolean[][] checkPickable;
        checkPickable = new boolean[9][9];

        //checkPickable[i][j] is true if the tile has at least 1 "free" side
        for(int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                //Empty and Blocked tiles should not be pickable
                if(!tiles[i][j].isFree()) {

                    //Corner tiles (8 tiles, if not blocked)
                    if (i == 0 || j == 0 || i == 8 || j == 8)
                        checkPickable[i][j] = true;

                    else if (tiles[i+1][j].isFree() || tiles[i-1][j].isFree() ||
                            tiles[i][j+1].isFree() || tiles[i][j-1].isFree())
                        checkPickable[i][j] = true;

                    else
                        checkPickable[i][j] = false;

                } else
                    checkPickable[i][j] = false;
            }
        }

        /*The board needs to be refilled if
            none of the tiles that have at least 1 free side
            has any close (up, down, right, left) tile that has at least 1 free side
        */

        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(     checkPickable[i][j] &&
                        (i+1<9 && checkPickable[i+1][j]) ||
                        (j+1<9 && checkPickable[i][j+1]) ||
                        (i-1>=0 && checkPickable[i-1][j]) ||
                        (j-1>=0 && checkPickable[i][j-1]))
                    return false;
            }
        }

        return true;
    }

    /**
     * Method used to select a specific tile from the board
     * @author Pasquale Gioia
     * @param x_Tile is the row of the selected tile from the board
     * @param y_Tile is the column of the selected tile from the board
     * @return A specific selected tile
     *
     */
    public Tile pickTile(int x_Tile, int y_Tile){
        Tile pickedTile = tiles[x_Tile][y_Tile];
        tiles[x_Tile][y_Tile] = Tile.EMPTY;

        return pickedTile;
    }

    public Map<Tile, Integer> getTilesRemaining() {
        return tilesRemaining;
    }

    public Tile[][] getTiles() {
        return tiles;
    }
}