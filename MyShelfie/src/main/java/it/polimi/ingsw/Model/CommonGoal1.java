package main.java.it.polimi.ingsw.Model;

import java.util.HashMap;
import java.util.Map;

public class CommonGoal1 extends CommonGoal{
    public CommonGoal1(int numPlayers){
        super(numPlayers);
    }

    public boolean check(Shelf shelf){
        int i,j;
        int[][] checked = new int[6][5];
        Map<TileColor,Integer> foundSquares;

        /*
        This method has to find two 2x2 squares of the same color
        It does so by searching for squares going first from top-left to bottom-right,
        then the other way around, in order to avoid overlays that may lower the count
        */

        //RESET
        foundSquares=new HashMap<>();
        for(TileColor tc : TileColor.values()){
            if(tc!=TileColor.EMPTY && tc!=TileColor.BLOCKED)
                foundSquares.put(tc,0);
        }

        for(i=0;i<6;i++){
            for(j=0;j<5;j++){
                if(shelf.getTile(i,j).isFree()) checked[i][j]=1;
                else checked[i][j]=0;
            }
        }
        //RESEARCH TOP DOWN
        for(i=0;i<5;i++){
            for(j=0;j<4;j++){
                if(checked[i][j]==0 && checked[i+1][j]==0 && checked[i][j+1]==0 && checked[i+1][j+1]==0){
                    if(shelf.getTile(i,j).equals(shelf.getTile(i+1,j)) &&
                    shelf.getTile(i,j).equals(shelf.getTile(i,j+1)) &&
                    shelf.getTile(i,j).equals(shelf.getTile(i+1,j+1))){
                        checked[i][j]=1;
                        checked[i+1][j]=1;
                        checked[i][j+1]=1;
                        checked[i+1][j+1]=1;
                        foundSquares.put(shelf.getTile(i,j).getColor(),foundSquares.get(shelf.getTile(i,j).getColor())+1);
                        if(foundSquares.get(shelf.getTile(i,j).getColor())>=2)
                            return true;
                    }
                }
            }
        }

        //RESET
        foundSquares=new HashMap<>();
        for(TileColor tc : TileColor.values()){
            if(tc!=TileColor.EMPTY && tc!=TileColor.BLOCKED)
                foundSquares.put(tc,0);
        }

        for(i=0;i<6;i++){
            for(j=0;j<5;j++){
                if(shelf.getTile(i,j).isFree()) checked[i][j]=1;
                else checked[i][j]=0;
            }
        }

        //RESEARCH BOTTOM UP
        for(i=4;i>=0;i--){
            for(j=3;j>=0;j--){
                if(checked[i][j]==0 && checked[i+1][j]==0 && checked[i][j+1]==0 && checked[i+1][j+1]==0){
                    if(shelf.getTile(i,j).equals(shelf.getTile(i+1,j)) &&
                            shelf.getTile(i,j).equals(shelf.getTile(i,j+1)) &&
                            shelf.getTile(i,j).equals(shelf.getTile(i+1,j+1))){
                        checked[i][j]=1;
                        checked[i+1][j]=1;
                        checked[i][j+1]=1;
                        checked[i+1][j+1]=1;
                        foundSquares.put(shelf.getTile(i,j).getColor(),foundSquares.get(shelf.getTile(i,j).getColor())+1);
                        if(foundSquares.get(shelf.getTile(i,j).getColor())>=2)
                            return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int getIndex() {
        return 1;
    }

    public String getDescription(){
        return "Two groups each containing 4 tiles of the same type in a 2x2 square. "
                +"The tiles of one square have to be the same type of those of the other square.";
    }
}
