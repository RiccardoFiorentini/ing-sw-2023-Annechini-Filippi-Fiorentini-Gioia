package main.java.it.polimi.ingsw.Model;

public class CommonGoal10 extends CommonGoal{
    public CommonGoal10(int numPlayers){
        super(numPlayers);
    }
    public boolean check(Shelf shelf) {
        int i,j;

        /*
        This method checks for cross-shaped blocks of the same color
         */

        for(i=0;i<4;i++){
            for(j=0;j<3;j++){
                if(shelf.getTile(i,j)!=Tile.EMPTY &&
                        shelf.getTile(i,j+2)!=Tile.EMPTY &&
                        shelf.getTile(i+1,j+1)!=Tile.EMPTY &&
                        shelf.getTile(i+2,j)!=Tile.EMPTY &&
                        shelf.getTile(i+2,j+2)!=Tile.EMPTY &&
                        Model.equalsTiles(shelf.getTile(i,j),shelf.getTile(i,j+2)) &&
                        Model.equalsTiles(shelf.getTile(i,j),shelf.getTile(i+1,j+1)) &&
                        Model.equalsTiles(shelf.getTile(i,j),shelf.getTile(i+2,j)) &&
                        Model.equalsTiles(shelf.getTile(i,j),shelf.getTile(i+2,j+2)))
                    return true;
            }
        }
        return false;
    }
}
