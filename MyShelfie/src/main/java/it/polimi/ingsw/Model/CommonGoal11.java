package main.java.it.polimi.ingsw.Model;

public class CommonGoal11 extends CommonGoal{
    public CommonGoal11(int numPlayers){
        super(numPlayers);
    }
    public boolean check(Shelf shelf) {

        /*
        This method looks for full diagonals, either ascending or descending,
        made of tiles of the same color
         */

        for(int i=0;i<2;i++){
            //DESCENDING
            if(!shelf.getTile(i,0).isFree() &&
                    shelf.getTile(i,0).equals(shelf.getTile(i+1,2)) &&
                    shelf.getTile(i,0).equals(shelf.getTile(i+2,2)) &&
                    shelf.getTile(i,0).equals(shelf.getTile(i+3,3)) &&
                    shelf.getTile(i,0).equals(shelf.getTile(i+4,4)))
                return true;
            //ASCENDING
            if(!shelf.getTile(i,4).isFree() &&
                    shelf.getTile(i,4).equals(shelf.getTile(i+1,3)) &&
                    shelf.getTile(i,4).equals(shelf.getTile(i+2,2)) &&
                    shelf.getTile(i,4).equals(shelf.getTile(i+3,1)) &&
                    shelf.getTile(i,4).equals(shelf.getTile(i+4,0)))
                return true;
        }
        return false;
    }

    @Override
    public int getIndex() {
        return 11;
    }
}
