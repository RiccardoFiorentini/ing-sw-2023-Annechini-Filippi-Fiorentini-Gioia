package it.polimi.ingsw.Model;

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

        Map<TileColor,Integer> occur = new HashMap<>();
        for(TileColor tc : TileColor.values()){
            if(tc!=TileColor.EMPTY && tc!=TileColor.BLOCKED)
                occur.put(tc,0);
        }

        int i,j;
        for(i=0;i<6;i++){
            for(j=0;j<5;j++){
                if(!shelf.getTile(i,j).isFree()){
                    occur.put(shelf.getTile(i,j).getColor(),occur.get(shelf.getTile(i,j).getColor())+1);
                }
            }
        }
        for(TileColor tc : occur.keySet()){
            if(occur.get(tc)>=8)
                return true;
        }
        return false;
    }

    @Override
    public int getIndex() {
        return 9;
    }

    public String getDescription() {
        return "Eight tiles of the same type. " +
                "There’s no restriction about the position of these tiles.";
    }
}
