package it.polimi.ingsw.Model;


import it.polimi.ingsw.ModelExceptions.NotPickableException;
import it.polimi.ingsw.ModelExceptions.NotToRefillException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Board{
    private Map<Tile, Integer> tilesRemaining;
    private Tile[][] tiles;
    private int numPlayers;

    /**
     * Class' constructor
     * @author Pasquale Gioia
     * @param numPlayers is the number of players that joined the game
     * @throws IOException Error while reading "Board.txt"
     */
    public Board(int numPlayers) throws IOException, NotToRefillException {

        this.numPlayers = numPlayers;
        tilesRemaining = new HashMap<>();

        //Board initialization
        int dimR, dimC;
        BufferedReader reader = new BufferedReader(new FileReader("src/config/Board.txt"));
        String line;
        int[] tmpArr;
        int i,j;
        line = reader.readLine();
        dimR = Integer.parseInt(line);
        line = reader.readLine();
        dimC = Integer.parseInt(line);
        tiles = new Tile[dimR][dimC];
        i=0;
        line = reader.readLine();
        while(i<dimR && line!=null){
            tmpArr= Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();
            for(j=0; j<dimC; j++){
                tiles[i][j] = tmpArr[j]>0 && tmpArr[j] <= numPlayers ? Tile.EMPTY : Tile.BLOCKED;
            }
            i++;
            line = reader.readLine();
        }

        //Bag initialization
        for(Tile t: Tile.values()){
            if     (t == Tile.GREEN1 || t == Tile.BLUE1 ||
                    t == Tile.WHITE1 || t == Tile.PINK1 ||
                    t == Tile.ORANGE1 || t == Tile.CYAN1)
                tilesRemaining.put(t, 8);

            else if(!t.isFree())
                tilesRemaining.put(t, 7);
        }

        //First board refill
        refill();
    }

    /**
     * Refills the board and decreases the amount of tiles left in the bag
     * @author Pasquale Gioia
     * @throws NotToRefillException when the board doesn't really need to be refilled
     */
    public void refill() throws NotToRefillException{
        if(!checkFill())
            throw new NotToRefillException();

        Random rTile = new Random();
        int rTileIndex;
        List<Tile> tileRem = new ArrayList<>();
        for(Tile t : tilesRemaining.keySet()){
            if(tilesRemaining.get(t)>0)
                tileRem.add(t);
        }


        for(int i=0; i<9 && tileRem.size()>0;i++)
            for(int j=0; j<9 && tileRem.size()>0; j++)
                if(tiles[i][j] == Tile.EMPTY) {
                    rTileIndex = rTile.nextInt(tileRem.size());
                    tiles[i][j] = tileRem.get(rTileIndex);
                    tilesRemaining.put(tileRem.get(rTileIndex), tilesRemaining.get(tileRem.get(rTileIndex)) -1);
                    if(tilesRemaining.get(tileRem.get(rTileIndex))<=0)
                        tileRem.remove(rTileIndex);
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
                if(     checkPickable[i][j] && (
                        (i+1<9 && checkPickable[i+1][j]) ||
                        (j+1<9 && checkPickable[i][j+1]) ||
                        (i-1>=0 && checkPickable[i-1][j]) ||
                        (j-1>=0 && checkPickable[i][j-1])))
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
     * @throws NotPickableException when the selected tile is empty or blocked
     * @return A specific selected tile
     *
     */
    public Tile pickTile(int x_Tile, int y_Tile) throws NotPickableException {
        Tile pickedTile = tiles[x_Tile][y_Tile];
        if(pickedTile.isFree())
            throw new NotPickableException();

        tiles[x_Tile][y_Tile] = Tile.EMPTY;

        return pickedTile;
    }

    public Map<Tile, Integer> getTilesRemaining() {
        return tilesRemaining;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public BoardBean toBean(){
        return new BoardBean(tilesRemaining, tiles);
    }
}