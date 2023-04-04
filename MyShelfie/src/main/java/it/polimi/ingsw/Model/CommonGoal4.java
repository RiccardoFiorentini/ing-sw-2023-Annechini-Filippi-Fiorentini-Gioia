package main.java.it.polimi.ingsw.Model;

public class CommonGoal4 extends CommonGoal{
    public CommonGoal4(int numPlayers){
        super(numPlayers);
    }
    public boolean check(Shelf shelf){
        int[][] checked = new int[6][5];
        boolean[][] valid = new boolean[6][5];
        int i,j,counter,minCount;
        boolean finished;

        /*
        This method checks if there are at least six distinct groups 1x2 or 2x1
        It does so by counting in how many groups every tile is, counting then
        the distinct groups in order of increasing sum of appearances of those tiles
         */

        for(i=0;i<6;i++){
            for(j=0;j<5;j++){
                checked[i][j]=0;
                valid[i][j]=false;
            }
        }

        //Count horizontal groups
        for(i=0;i<6;i++){
            for(j=0;j<4;j++){
                if(!shelf.getTile(i,j).isFree() &&
                        shelf.getTile(i,j).equals(shelf.getTile(i,j+1))){
                    checked[i][j]++;
                    checked[i][j+1]++;
                    valid[i][j]=true;
                    valid[i][j+1]=true;
                }
            }
        }

        //Count vertical groups
        for(i=0;i<5;i++){
            for(j=0;j<5;j++){
                if(!shelf.getTile(i,j).isFree() &&
                        shelf.getTile(i,j).equals(shelf.getTile(i+1,j))){
                    checked[i][j]++;
                    checked[i+1][j]++;
                    valid[i][j]=true;
                    valid[i+1][j]=true;
                }
            }
        }

        counter=0;
        finished=false;

        while(counter<6 && !finished) {
            minCount = -1;
            finished = true;

            //Check horizontal group sums
            for (i = 0; i < 6; i++) {
                for (j = 0; j < 4; j++) {
                    if (valid[i][j] && valid[i][j + 1]) {
                        if (shelf.getTile(i, j).equals(shelf.getTile(i, j + 1))
                        ) {
                            if (minCount < 0 || minCount > checked[i][j] + checked[i][j + 1]) {
                                minCount = checked[i][j] + checked[i][j + 1];
                                finished = false;
                            }
                        }
                    }
                }
            }

            //Check vertical group sums
            for (i = 0; i < 5; i++) {
                for (j = 0; j < 5; j++) {
                    if (valid[i][j] && valid[i + 1][j]) {
                        if (shelf.getTile(i, j).equals(shelf.getTile(i + 1, j))
                        ) {
                            if (minCount < 0 || minCount > checked[i][j] + checked[i + 1][j]) {
                                minCount = checked[i][j] + checked[i + 1][j];
                                finished = false;
                            }
                        }
                    }
                }
            }

            //Count horizontal groups
            for (i = 0; i < 6; i++) {
                for (j = 0; j < 4; j++) {
                    if (valid[i][j] && valid[i][j + 1]) {
                        if (shelf.getTile(i, j).equals(shelf.getTile(i, j + 1))
                        ) {
                            if (minCount >= checked[i][j] + checked[i][j + 1]) {
                                valid[i][j] = false;
                                valid[i][j + 1] = false;
                                counter++;
                            }
                        }
                    }
                }
            }

            //Count vertical groups
            for (i = 0; i < 5; i++) {
                for (j = 0; j < 5; j++) {
                    if (valid[i][j] && valid[i + 1][j]) {
                        if (shelf.getTile(i, j).equals(shelf.getTile(i + 1, j))
                        ) {
                            if (minCount >= checked[i][j] + checked[i + 1][j]) {
                                valid[i][j] = false;
                                valid[i + 1][j] = false;
                                counter++;
                            }
                        }
                    }
                }
            }
        }
        return counter>=6;
    }
}
