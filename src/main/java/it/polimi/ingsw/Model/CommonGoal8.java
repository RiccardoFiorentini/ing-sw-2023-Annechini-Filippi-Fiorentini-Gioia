package it.polimi.ingsw.Model;

public class CommonGoal8 extends CommonGoal{
    public CommonGoal8(int numPlayers){
        super(numPlayers);
    }
    public boolean check(Shelf shelf) {

        /*
        This method checks if the four corners of the shelf contain
        tiles of the same color
         */

        return !shelf.getTile(0, 0).isFree() &&
                shelf.getTile(0, 0).equals(shelf.getTile(0, 4)) &&
                shelf.getTile(0, 0).equals(shelf.getTile(5, 0)) &&
                shelf.getTile(0, 0).equals(shelf.getTile(5, 4));
    }

    @Override
    public int getIndex() {
        return 8;
    }

    public String getDescription() {
        return "Four tiles of the same type in the four corners of the bookshelf.";
    }
}
