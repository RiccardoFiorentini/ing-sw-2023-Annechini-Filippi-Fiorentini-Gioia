package main.java.it.polimi.ingsw.Model;

import java.util.HashMap;
import java.util.Map;

public class CommonGoal9 extends CommonGoal{
    public CommonGoal9(int numPlayers){
        super(numPlayers);
    }
    public boolean check(Shelf shelf) {

        /*
        This method counts the number of tiles of each color, checking
        if there are at least 8 tiles of the same color
         */

        Map<Tile,Integer> occur = new HashMap<>();
        occur.put(Tile.BLUE1,0);
        occur.put(Tile.CYAN1,0);
        occur.put(Tile.GREEN1,0);
        occur.put(Tile.WHITE1,0);
        occur.put(Tile.ORANGE1,0);
        occur.put(Tile.PINK1,0);

        int i,j;
        for(i=0;i<6;i++){
            for(j=0;j<5;j++){
                if(shelf.getTile(i,j)!=Tile.EMPTY){
                    for(Tile t : occur.keySet()){
                        if(Model.equalsTiles(shelf.getTile(i,j),t))
                            occur.put(t,occur.get(t)+1);
                    }
                }
            }
        }
        for(Tile t : occur.keySet()){
            if(occur.get(t)>=8)
                return true;
        }
        return false;
    }
}
