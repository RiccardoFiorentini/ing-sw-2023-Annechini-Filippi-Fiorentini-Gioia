package main.java.it.polimi.ingsw.Model;

import java.util.HashSet;
import java.util.Set;

public class CommonGoal5 extends CommonGoal{
    public CommonGoal5(int numPlayers){
        super(numPlayers);
    }
    public boolean check(Shelf shelf) {
        int colFound=0;
        Set<TileColor> typesFound;

        /*
        This method has to find 3 columns with at most 3 different colours
        It does so by counting the number of unique tile colours in every full column
         */

        int i,j;
        for(j=0;j<5;j++){
            if(shelf.spaceInCol(j)==0){
                typesFound = new HashSet<>();
                for(i=0;i<6;i++){
                    typesFound.add(shelf.getTile(i,j).getColor());
                }
                if(typesFound.size() <= 3){
                    colFound++;
                    if(colFound>=3)
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getIndex() {
        return 5;
    }
}
