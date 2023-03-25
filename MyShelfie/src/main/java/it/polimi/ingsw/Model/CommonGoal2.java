package main.java.it.polimi.ingsw.Model;

public class CommonGoal2 extends CommonGoal{
    public CommonGoal2(int numPlayers){
        super(numPlayers);
    }
    public boolean check(Shelf shelf) {
        int foundCol=0,i,j,k;
        boolean valid;

        /*
        This method looks for two columns with 6 different colours
        The for loops pick every couple of tiles in the column, checking if they have different colours
        */

        for(i=0;i<5;i++){
            if(shelf.spaceInCol(i)==0){
                valid = true;
                for(j=0;j<5 && valid;j++){
                    for(k=j+1; k<6 && valid; k++){
                        valid = !Model.equalsTiles(shelf.getTile(j,i),shelf.getTile(k,i));
                    }
                }
                if(valid){
                    foundCol++;
                    if(foundCol>=2)
                        return true;
                }
            }
        }
        return false;
    }
}
