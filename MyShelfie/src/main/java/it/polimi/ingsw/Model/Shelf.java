package main.java.it.polimi.ingsw.Model;

import main.java.it.polimi.ingsw.ModelExceptions.FullColumnException;

import java.io.Serializable;

public class Shelf implements Serializable {
    private Tile[][] tiles;
    private int[] spacesForCol;
    private int spacesLeft;

    /**
     * Class' constructor
     * @author Alessandro Annechini
     */
    public Shelf(){
        tiles = new Tile[6][5];
        spacesForCol = new int[5];
        spacesLeft = 30;
        for(int j = 0; j < 5; j++){
            spacesForCol[j] = 6;
            for(int i = 0; i < 6; i++){
                tiles[i][j] = Tile.EMPTY;
            }
        }
    }

    /**
     * This method inserts a tile into the first empty space in the selected column
     * @author Alessandro Annechini
     * @param tile the tile to be inserted
     * @param col the selected column
     * @throws FullColumnException when the player tries to put a tile in a full column
     */
    public void putTile(Tile tile, int col) throws FullColumnException {
        if(spacesForCol[col]<=0) throw new FullColumnException();
        spacesForCol[col]--;
        spacesLeft--;
        tiles[spacesForCol[col]][col] = tile;
    }

    /**
     * This method checks if the shelf is full
     * @author Alessandro Annechini
     * @return true if the shelf is full, false otherwise
     */
    public boolean isFull(){
        return spacesLeft == 0;
    }
    public int spaceInCol(int col){
        return spacesForCol[col];
    }

    /**
     * This method returns the points earned by grouping together tiles of the same color
     * @author Alessandro Annechini
     * @return The number of points
     */
    public int getGroupsPoints(){
        int i,j,totPoints,tmpGroup;
        int[][] checked = new int[6][5];

        for(i=0;i<6;i++){
            for(j=0;j<5;j++){
                if(tiles[i][j].isFree()) checked[i][j]=1;
                else checked[i][j]=0;
            }
        }
        totPoints=0;
        for(i=0;i<6;i++){
            for(j=0;j<5;j++){
                if(checked[i][j]==0){
                    tmpGroup = recursiveCheck(i,j,checked);
                    if(tmpGroup==3) totPoints+=2;
                    else if(tmpGroup==4) totPoints+=3;
                    else if(tmpGroup==5) totPoints+=5;
                    else if(tmpGroup>=6) totPoints+=8;
                }
            }
        }
        return totPoints;
    }

    /**
     * This method calculates the dimension of a color group, signaling the group as checked
     * @author Alessandro Annechini
     * @param row The row of the current tile
     * @param col The column of the current tile
     * @param checked The matrix indicating the checked tiles
     * @return The dimension of the current group
     */
    private int recursiveCheck(int row,int col,int[][] checked){
        int groupDim = 1;
        checked[row][col]=1;
        if(row > 0 && checked[row-1][col]==0 && tiles[row][col].equals(tiles[row-1][col]))
            groupDim += recursiveCheck(row-1,col,checked);
        if(row < 5 && checked[row+1][col]==0 && tiles[row][col].equals(tiles[row+1][col]))
            groupDim += recursiveCheck(row+1,col,checked);
        if(col > 0 && checked[row][col-1]==0 && tiles[row][col].equals(tiles[row][col-1]))
            groupDim += recursiveCheck(row,col-1,checked);
        if(col < 4 && checked[row][col+1]==0 && tiles[row][col].equals(tiles[row][col+1]))
            groupDim += recursiveCheck(row,col+1,checked);
        return groupDim;
    }

    public Tile[][] getTiles() {
        return tiles;
    }
    public Tile getTile(int row, int col) {
        return tiles[row][col];
    }

}