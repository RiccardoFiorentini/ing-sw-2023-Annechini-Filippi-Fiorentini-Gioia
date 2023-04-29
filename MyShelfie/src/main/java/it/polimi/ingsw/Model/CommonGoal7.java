package main.java.it.polimi.ingsw.Model;

import java.util.HashSet;
import java.util.Set;

public class CommonGoal7 extends CommonGoal{
    public CommonGoal7(int numPlayers){
        super(numPlayers);
    }
    public boolean check(Shelf shelf) {
        int rowFound=0;
        Set<TileColor> typesFound;
        boolean notFullRow;
        int i,j;

        /*
        This method has to find 4 rows with at most 3 different colours
        It does so by counting the number of unique tile colours in every full row
        terminating if it finds an empty space
         */

        notFullRow=false;
        for(i=5;i>=0 && !notFullRow;i--){
            typesFound = new HashSet<>();
            for(j=0;j<5 && !notFullRow;j++){
                if(shelf.getTile(i,j).isFree())
                    notFullRow=true;
                else{
                    typesFound.add(shelf.getTile(i,j).getColor());
                }
            }
            if(typesFound.size() <= 3 && !notFullRow){
                rowFound++;
                if(rowFound>=4)
                    return true;
            }
        }
        return false;
    }

    @Override
    public int getIndex() {
        return 7;
    }

    public String getDescription() {
        return "Four lines each formed by 5 tiles of maximum three different types. " +
                "One line can show the same or a different combination of another line.";
    }
}
