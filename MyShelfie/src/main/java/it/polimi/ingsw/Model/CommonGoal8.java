package main.java.it.polimi.ingsw.Model;

public class CommonGoal8 extends CommonGoal{
    public CommonGoal8(int numPlayers){
        super(numPlayers);
    }
    public boolean check(Shelf shelf) {

        /*
        This method checks if the four corners of the shelf contain
        tiles of the same colour
         */

        return shelf.getTile(0, 0) != Tile.EMPTY && shelf.getTile(0, 4) != Tile.EMPTY &&
                Model.equalsTiles(shelf.getTile(0, 0), shelf.getTile(0, 4)) &&
                Model.equalsTiles(shelf.getTile(0, 0), shelf.getTile(5, 0)) &&
                Model.equalsTiles(shelf.getTile(0, 0), shelf.getTile(5, 4));
    }
}
