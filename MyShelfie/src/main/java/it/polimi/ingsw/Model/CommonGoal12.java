package main.java.it.polimi.ingsw.Model;

public class CommonGoal12 extends CommonGoal{
    public CommonGoal12(int numPlayers){
        super(numPlayers);
    }
    public boolean check(Shelf shelf) {
        int step,i;

        /*
        This method checks if, starting either from the left or the right,
        every column has one more tile than the previous, making the shape
        of a ladder
         */

        if(shelf.spaceInCol(0)==6 || shelf.spaceInCol(4)==6)
            return false;
        step = shelf.spaceInCol(1) - shelf.spaceInCol(0);
        if(step!=1 && step!=-1)
            return false;
        for(i=0;i<3;i++){
            if(shelf.spaceInCol(i+1)!=shelf.spaceInCol(i)+step)
                return false;
        }
        return true;
    }

    @Override
    public int getIndex() {
        return 12;
    }
}
