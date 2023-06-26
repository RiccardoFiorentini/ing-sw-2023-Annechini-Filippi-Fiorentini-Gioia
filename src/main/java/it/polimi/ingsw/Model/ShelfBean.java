package it.polimi.ingsw.Model;

import java.io.Serializable;

public class ShelfBean implements Serializable {
    private Tile[][] tiles;
    private int[] spacesForCol;
    private int spacesLeft;

    /**
     * Class' constructor
     * @author Pasquale Gioia
     * @param tiles is shelf's matrix
     * @param spacesForCol is the number of spaces left for each column
     * @param spacesLeft is the number of spaces left in the matrix
     */
    public ShelfBean(Tile[][] tiles, int[] spacesForCol, int spacesLeft){
        this.tiles = tiles.clone();
        this.spacesForCol = spacesForCol.clone();
        this.spacesLeft = spacesLeft;
    }

    public Tile[][] getTiles() {
        return tiles;
    }
    public Tile getTile(int row, int col) {
        return tiles[row][col];
    }
}
