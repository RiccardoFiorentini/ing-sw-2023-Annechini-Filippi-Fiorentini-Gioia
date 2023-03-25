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
        Map<Tile,Integer> foundSquares;

        /*
        This method has to find two 2x2 squares of the same color
        It does so by searching for squares going first from top-left to bottom-right,
        then the other way around, in order to avoid overlays that may lower the count
        */

        //RESET
        foundSquares=new HashMap<>();
        foundSquares.put(Tile.BLUE1,0);
        foundSquares.put(Tile.CYAN1,0);
        foundSquares.put(Tile.GREEN1,0);
        foundSquares.put(Tile.WHITE1,0);
        foundSquares.put(Tile.ORANGE1,0);
        foundSquares.put(Tile.PINK1,0);
        for(i=0;i<6;i++){
            for(j=0;j<5;j++){
                if(shelf.getTile(i,j)==Tile.EMPTY) checked[i][j]=1;
                else checked[i][j]=0;
            }
        }
        //RESEARCH TOP DOWN
        for(i=0;i<5;i++){
            for(j=0;j<4;j++){
                if(checked[i][j]==0 && checked[i+1][j]==0 && checked[i][j+1]==0 && checked[i+1][j+1]==0){
                    if(Model.equalsTiles(shelf.getTile(i,j),shelf.getTile(i+1,j)) &&
                    Model.equalsTiles(shelf.getTile(i,j),shelf.getTile(i,j+1)) &&
                    Model.equalsTiles(shelf.getTile(i,j),shelf.getTile(i+1,j+1))){
                        checked[i][j]=1;
                        checked[i+1][j]=1;
                        checked[i][j+1]=1;
                        checked[i+1][j+1]=1;
                        for(Tile t : foundSquares.keySet()){
                            if(Model.equalsTiles(shelf.getTile(i,j),t)){
                                foundSquares.put(t,foundSquares.get(t)+1);
                                if(foundSquares.get(t)>=2)
                                    return true;
                            }
                        }
                    }
                }
            }
        }

        //RESET
        foundSquares=new HashMap<>();
        foundSquares.put(Tile.BLUE1,0);
        foundSquares.put(Tile.CYAN1,0);
        foundSquares.put(Tile.GREEN1,0);
        foundSquares.put(Tile.WHITE1,0);
        foundSquares.put(Tile.ORANGE1,0);
        foundSquares.put(Tile.PINK1,0);
        for(i=0;i<6;i++){
            for(j=0;j<5;j++){
                if(shelf.getTile(i,j)==Tile.EMPTY) checked[i][j]=1;
                else checked[i][j]=0;
            }
        }

        //RESEARCH BOTTOM UP
        for(i=4;i>=0;i--){
            for(j=3;j>=0;j--){
                if(checked[i][j]==0 && checked[i+1][j]==0 && checked[i][j+1]==0 && checked[i+1][j+1]==0){
                    if(Model.equalsTiles(shelf.getTile(i,j),shelf.getTile(i+1,j)) &&
                    Model.equalsTiles(shelf.getTile(i,j),shelf.getTile(i,j+1)) &&
                    Model.equalsTiles(shelf.getTile(i,j),shelf.getTile(i+1,j+1))){
                        checked[i][j]=1;
                        checked[i+1][j]=1;
                        checked[i][j+1]=1;
                        checked[i+1][j+1]=1;
                        for(Tile t : foundSquares.keySet()){
                            if(Model.equalsTiles(shelf.getTile(i,j),t)){
                                foundSquares.put(t,foundSquares.get(t)+1);
                                if(foundSquares.get(t)>=2)
                                    return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
