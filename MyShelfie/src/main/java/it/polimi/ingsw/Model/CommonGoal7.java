package main.java.it.polimi.ingsw.Model;

import java.util.HashSet;
import java.util.Set;

public class CommonGoal7 extends CommonGoal{
    public CommonGoal7(int numPlayers){
        super(numPlayers);
    }
    public boolean check(Shelf shelf) {
        int rowFound=0;
        Set<Tile> typesFound;
        boolean alreadyFound,notFullRow;
        int i,j;

        /*
        This method has to find 4 rows with at most 3 different colours
        It does so by counting the number of unique tile colours in every full row
        terminating if it finds an empty space
         */

        notFullRow=false;
        for(i=0;i<6 && !notFullRow;i++){
            typesFound = new HashSet<>();
            for(j=0;j<5 && !notFullRow;j++){
                if(shelf.getTile(i,j)==Tile.EMPTY)
                    notFullRow=true;
                else{
                    alreadyFound=false;
                    for(Tile t : typesFound){
                        if(Model.equalsTiles(shelf.getTile(i,j),t))
                            alreadyFound = true;
                    }
                    if(!alreadyFound)
                        typesFound.add(shelf.getTile(i,j));
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
}
