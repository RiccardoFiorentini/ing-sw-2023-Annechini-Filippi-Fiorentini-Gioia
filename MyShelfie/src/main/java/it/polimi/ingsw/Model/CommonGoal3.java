package main.java.it.polimi.ingsw.Model;

public class CommonGoal3 extends CommonGoal{
    public CommonGoal3(int numPlayers){
        super(numPlayers);
    }
    public boolean check(Shelf shelf) {
        int i,j;
        int[][] checked = new int[6][5];
        int foundQuad;

        /*
        This method has to find two 1x4 and 4x1 blocks
        It does so by searching for block going from top-left to bottom-right (and the other way around),
        checking first vertical blocks, then horizontal blocks (and vice versa),
        in all the possible combinations, in order to avoid overlays that may lower the count
        */

        //VERTICAL TOP DOWN - HORIZONTAL TOP DOWN
        for(i=0;i<6;i++){
            for(j=0;j<5;j++){
                if(shelf.getTile(i,j)==Tile.EMPTY) checked[i][j]=1;
                else checked[i][j]=0;
            }
        }
        foundQuad=countQuad(shelf,true,false,checked,0);
        foundQuad=countQuad(shelf,false,false,checked,foundQuad);
        if(foundQuad>=4)
            return true;

        //VERTICAL TOP DOWN - HORIZONTAL BOTTOM UP
        for(i=0;i<6;i++){
            for(j=0;j<5;j++){
                if(shelf.getTile(i,j)==Tile.EMPTY) checked[i][j]=1;
                else checked[i][j]=0;
            }
        }
        foundQuad=countQuad(shelf,true,false,checked,0);
        foundQuad=countQuad(shelf,false,true,checked,foundQuad);
        if(foundQuad>=4)
            return true;

        //VERTICAL BOTTOM UP - HORIZONTAL TOP DOWN
        for(i=0;i<6;i++){
            for(j=0;j<5;j++){
                if(shelf.getTile(i,j)==Tile.EMPTY) checked[i][j]=1;
                else checked[i][j]=0;
            }
        }
        foundQuad=countQuad(shelf,true,true,checked,0);
        foundQuad=countQuad(shelf,false,false,checked,foundQuad);
        if(foundQuad>=4)
            return true;

        //VERTICAL BOTTOM UP - HORIZONTAL BOTTOM UP
        for(i=0;i<6;i++){
            for(j=0;j<5;j++){
                if(shelf.getTile(i,j)==Tile.EMPTY) checked[i][j]=1;
                else checked[i][j]=0;
            }
        }
        foundQuad=countQuad(shelf,true,true,checked,0);
        foundQuad=countQuad(shelf,false,true,checked,foundQuad);
        if(foundQuad>=4)
            return true;

        //HORIZONTAL TOP DOWN - VERTICAL TOP DOWN
        for(i=0;i<6;i++){
            for(j=0;j<5;j++){
                if(shelf.getTile(i,j)==Tile.EMPTY) checked[i][j]=1;
                else checked[i][j]=0;
            }
        }
        foundQuad=countQuad(shelf,false,false,checked,0);
        foundQuad=countQuad(shelf,true,false,checked,foundQuad);
        if(foundQuad>=4)
            return true;

        //HORIZONTAL TOP DOWN - VERTICAL BOTTOM UP
        for(i=0;i<6;i++){
            for(j=0;j<5;j++){
                if(shelf.getTile(i,j)==Tile.EMPTY) checked[i][j]=1;
                else checked[i][j]=0;
            }
        }
        foundQuad=countQuad(shelf,false,false,checked,0);
        foundQuad=countQuad(shelf,true,true,checked,foundQuad);
        if(foundQuad>=4)
            return true;

        //HORIZONTAL BOTTOM UP - VERTICAL TOP DOWN
        for(i=0;i<6;i++){
            for(j=0;j<5;j++){
                if(shelf.getTile(i,j)==Tile.EMPTY) checked[i][j]=1;
                else checked[i][j]=0;
            }
        }
        foundQuad=countQuad(shelf,false,true,checked,0);
        foundQuad=countQuad(shelf,true,false,checked,foundQuad);
        if(foundQuad>=4)
            return true;

        //HORIZONTAL BOTTOM UP - VERTICAL BOTTOM UP
        for(i=0;i<6;i++){
            for(j=0;j<5;j++){
                if(shelf.getTile(i,j)==Tile.EMPTY) checked[i][j]=1;
                else checked[i][j]=0;
            }
        }
        foundQuad=countQuad(shelf,false,true,checked,0);
        foundQuad=countQuad(shelf,true,true,checked,foundQuad);
        if(foundQuad>=4)
            return true;

        return false;
    }

    /**
     * This auxiliary method counts the group in the given orientation and direction, marking them as checked
     * @author Alessandro Annechini
     * @param shelf The shelf to be checked
     * @param vertical True if the check is for vertical groups, false if horizontal
     * @param bottomUp True if the search has to be bottom up, false if top down
     * @param checked The matrix signaling the tiles that have already been checked
     * @param alreadyFound The number of groups already found in previous researches
     * @return The number of valid groups found
     */
    private int countQuad(Shelf shelf,boolean vertical,boolean bottomUp, int[][] checked, int alreadyFound){
        int i,j,foundQuad=alreadyFound;
        int step_row=0,step_col=0;
        if(alreadyFound>=4)
            return foundQuad;
        if(vertical)
            step_row=1;
        else
            step_col=1;
        if(bottomUp){
            for(i=5-3*step_row;i>=0;i--){
                for(j=4-3*step_col;j>=0;j--){
                    if(checked[i][j]==0 && checked[i+step_row][j+step_col]==0 && checked[i+2*step_row][j+2*step_col]==0 && checked[i+3*step_row][j+3*step_col]==0){
                        if(Model.equalsTiles(shelf.getTile(i,j),shelf.getTile(i+step_row,j+step_col)) &&
                                Model.equalsTiles(shelf.getTile(i,j),shelf.getTile(i+2*step_row,j+2*step_col))&&
                                Model.equalsTiles(shelf.getTile(i,j),shelf.getTile(i+3*step_row,j+3*step_col))){
                            checked[i][j]=1;
                            checked[i+step_row][j+step_col]=1;
                            checked[i+2*step_row][j+2*step_col]=1;
                            checked[i+3*step_row][j+3*step_col]=1;
                            foundQuad++;
                            if(foundQuad>=4)
                                return foundQuad;
                        }
                    }
                }
            }
        }
        else{
            for(i=0;i<6-3*step_row;i++){
                for(j=0;j<5-3*step_col;j++){
                    if(checked[i][j]==0 && checked[i+step_row][j+step_col]==0 && checked[i+2*step_row][j+2*step_col]==0 && checked[i+3*step_row][j+3*step_col]==0){
                        if(Model.equalsTiles(shelf.getTile(i,j),shelf.getTile(i+step_row,j+step_col)) &&
                                Model.equalsTiles(shelf.getTile(i,j),shelf.getTile(i+2*step_row,j+2*step_col))&&
                                Model.equalsTiles(shelf.getTile(i,j),shelf.getTile(i+3*step_row,j+3*step_col))){
                            checked[i][j]=1;
                            checked[i+step_row][j+step_col]=1;
                            checked[i+2*step_row][j+2*step_col]=1;
                            checked[i+3*step_row][j+3*step_col]=1;
                            foundQuad++;
                            if(foundQuad>=4)
                                return foundQuad;
                        }
                    }
                }
            }
        }
        return foundQuad;
    }
}