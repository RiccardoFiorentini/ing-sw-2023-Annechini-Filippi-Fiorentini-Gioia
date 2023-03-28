package main.java.it.polimi.ingsw.Model;

public class CommonGoal4 extends CommonGoal{
    public CommonGoal4(int numPlayers){
        super(numPlayers);
    }
    public boolean check(Shelf shelf) {
        int[][] checked = new int[6][5];
        int foundDuo;

        /*
        This method has to find two 1x2 and 2x1 blocks
        It does so by searching for block going from top-left to bottom-right (and the other way around),
        checking first vertical blocks, then horizontal blocks (and vice versa),
        in all the possible combinations, in order to avoid overlays that may lower the count
        */

        //VERTICAL TOP DOWN - HORIZONTAL TOP DOWN
        resetTable(shelf,checked);
        foundDuo=countDuo(shelf,true,false,checked,0);
        foundDuo=countDuo(shelf,false,false,checked,foundDuo);
        if(foundDuo>=6)
            return true;

        //VERTICAL TOP DOWN - HORIZONTAL BOTTOM UP
        resetTable(shelf,checked);
        foundDuo=countDuo(shelf,true,false,checked,0);
        foundDuo=countDuo(shelf,false,true,checked,foundDuo);
        if(foundDuo>=6)
            return true;

        //VERTICAL BOTTOM UP - HORIZONTAL TOP DOWN
        resetTable(shelf,checked);
        foundDuo=countDuo(shelf,true,true,checked,0);
        foundDuo=countDuo(shelf,false,false,checked,foundDuo);
        if(foundDuo>=6)
            return true;

        //VERTICAL BOTTOM UP - HORIZONTAL BOTTOM UP
        resetTable(shelf,checked);
        foundDuo=countDuo(shelf,true,true,checked,0);
        foundDuo=countDuo(shelf,false,true,checked,foundDuo);
        if(foundDuo>=6)
            return true;

        //HORIZONTAL TOP DOWN - VERTICAL TOP DOWN
        resetTable(shelf,checked);
        foundDuo=countDuo(shelf,false,false,checked,0);
        foundDuo=countDuo(shelf,true,false,checked,foundDuo);
        if(foundDuo>=6)
            return true;

        //HORIZONTAL TOP DOWN - VERTICAL BOTTOM UP
        resetTable(shelf,checked);
        foundDuo=countDuo(shelf,false,false,checked,0);
        foundDuo=countDuo(shelf,true,true,checked,foundDuo);
        if(foundDuo>=6)
            return true;

        //HORIZONTAL BOTTOM UP - VERTICAL TOP DOWN
        resetTable(shelf,checked);
        foundDuo=countDuo(shelf,false,true,checked,0);
        foundDuo=countDuo(shelf,true,false,checked,foundDuo);
        if(foundDuo>=6)
            return true;

        //HORIZONTAL BOTTOM UP - VERTICAL BOTTOM UP
        resetTable(shelf,checked);
        foundDuo=countDuo(shelf,false,true,checked,0);
        foundDuo=countDuo(shelf,true,true,checked,foundDuo);
        if(foundDuo>=6)
            return true;

        return false;
    }

    /**
     * This auxiliary method resets the "checked" matrix
     * @author Alessandro Annechini
     * @param shelf The shelf to be checked
     * @param checked The matrix to be reset
     */

    private void resetTable(Shelf shelf, int[][] checked){
        int i,j;
        for(i=0;i<6;i++){
            for(j=0;j<5;j++){
                if(shelf.getTile(i,j).isFree()) checked[i][j]=1;
                else checked[i][j]=0;
            }
        }
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
    private int countDuo(Shelf shelf,boolean vertical,boolean bottomUp, int[][] checked, int alreadyFound){
        int i,j,foundDuo=alreadyFound;
        int step_row=0,step_col=0;
        if(alreadyFound>=6)
            return foundDuo;
        if(vertical)
            step_row=1;
        else
            step_col=1;
        if(bottomUp){
            for(i=5-step_row;i>=0;i--){
                for(j=4-step_col;j>=0;j--){
                    if(checked[i][j]==0 && checked[i+step_row][j+step_col]==0){
                        if(shelf.getTile(i,j).equals(shelf.getTile(i+step_row,j+step_col))){
                            checked[i][j]=1;
                            checked[i+step_row][j+step_col]=1;
                            foundDuo++;
                            if(foundDuo>=6)
                                return foundDuo;
                        }
                    }
                }
            }
        }
        else{
            for(i=0;i<6-step_row;i++){
                for(j=0;j<5-step_col;j++){
                    if(checked[i][j]==0 && checked[i+step_row][j+step_col]==0){
                        if(shelf.getTile(i,j).equals(shelf.getTile(i+step_row,j+step_col))){
                            checked[i][j]=1;
                            checked[i+step_row][j+step_col]=1;
                            foundDuo++;
                            if(foundDuo>=6)
                                return foundDuo;
                        }
                    }
                }
            }
        }
        return foundDuo;
    }
}
