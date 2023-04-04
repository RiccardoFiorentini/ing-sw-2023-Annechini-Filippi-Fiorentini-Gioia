package main.java.it.polimi.ingsw.Model;

public class CommonGoal3 extends CommonGoal{
    public CommonGoal3(int numPlayers){
        super(numPlayers);
    }

    public boolean check(Shelf shelf){
        int[][] checked = new int[6][5];
        boolean[][] valid = new boolean[6][5];
        int i,j,counter,minCount;
        boolean finished;

        /*
        This method checks if there are at least four distinct groups 1x4 or 4x1
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
            for(j=0;j<2;j++){
                if(!shelf.getTile(i,j).isFree() &&
                shelf.getTile(i,j).equals(shelf.getTile(i,j+1)) &&
                shelf.getTile(i,j).equals(shelf.getTile(i,j+2)) &&
                shelf.getTile(i,j).equals(shelf.getTile(i,j+3))){
                    checked[i][j]++;
                    checked[i][j+1]++;
                    checked[i][j+2]++;
                    checked[i][j+3]++;
                    valid[i][j]=true;
                    valid[i][j+1]=true;
                    valid[i][j+2]=true;
                    valid[i][j+3]=true;
                }
            }
        }

        //Count vertical groups
        for(i=0;i<3;i++){
            for(j=0;j<5;j++){
                if(!shelf.getTile(i,j).isFree() &&
                        shelf.getTile(i,j).equals(shelf.getTile(i+1,j)) &&
                        shelf.getTile(i,j).equals(shelf.getTile(i+2,j)) &&
                        shelf.getTile(i,j).equals(shelf.getTile(i+3,j))){
                    checked[i][j]++;
                    checked[i+1][j]++;
                    checked[i+2][j]++;
                    checked[i+3][j]++;
                    valid[i][j]=true;
                    valid[i+1][j]=true;
                    valid[i+2][j]=true;
                    valid[i+3][j]=true;
                }
            }
        }

        counter=0;
        finished=false;

        while(counter<4 && !finished) {
            minCount = -1;
            finished = true;

            //Check horizontal group sums
            for (i = 0; i < 6; i++) {
                for (j = 0; j < 2; j++) {
                    if (valid[i][j] && valid[i][j + 1] && valid[i][j + 2] && valid[i][j + 3]) {
                        if (shelf.getTile(i, j).equals(shelf.getTile(i, j + 1)) &&
                                shelf.getTile(i, j).equals(shelf.getTile(i, j + 2)) &&
                                shelf.getTile(i, j).equals(shelf.getTile(i, j + 3))
                        ) {
                            if (minCount < 0 || minCount > checked[i][j] + checked[i][j + 1] + checked[i][j + 2] + checked[i][j + 3]) {
                                minCount = checked[i][j] + checked[i][j + 1] + checked[i][j + 2] + checked[i][j + 3];
                                finished = false;
                            }
                        }
                    }
                }
            }

            //Check vertical group sums
            for (i = 0; i < 3; i++) {
                for (j = 0; j < 5; j++) {
                    if (valid[i][j] && valid[i + 1][j] && valid[i + 2][j] && valid[i + 3][j]) {
                        if (shelf.getTile(i, j).equals(shelf.getTile(i + 1, j)) &&
                                shelf.getTile(i, j).equals(shelf.getTile(i + 2, j)) &&
                                shelf.getTile(i, j).equals(shelf.getTile(i + 3, j))
                        ) {
                            if (minCount < 0 || minCount > checked[i][j] + checked[i + 1][j] + checked[i + 2][j] + checked[i + 3][j]) {
                                minCount = checked[i][j] + checked[i + 1][j] + checked[i + 2][j] + checked[i + 3][j];
                                finished = false;
                            }
                        }
                    }
                }
            }

            //Count horizontal groups
            for (i = 0; i < 6; i++) {
                for (j = 0; j < 2; j++) {
                    if (valid[i][j] && valid[i][j + 1] && valid[i][j + 2] && valid[i][j + 3]) {
                        if (shelf.getTile(i, j).equals(shelf.getTile(i, j + 1)) &&
                                shelf.getTile(i, j).equals(shelf.getTile(i, j + 2)) &&
                                shelf.getTile(i, j).equals(shelf.getTile(i, j + 3))
                        ) {
                            if (minCount >= checked[i][j] + checked[i][j + 1] + checked[i][j + 2] + checked[i][j + 3]) {
                                valid[i][j] = false;
                                valid[i][j + 1] = false;
                                valid[i][j + 2] = false;
                                valid[i][j + 3] = false;
                                counter++;
                            }
                        }
                    }
                }
            }

            //Count vertical groups
            for (i = 0; i < 3; i++) {
                for (j = 0; j < 5; j++) {
                    if (valid[i][j] && valid[i + 1][j] && valid[i + 2][j] && valid[i + 3][j]) {
                        if (shelf.getTile(i, j).equals(shelf.getTile(i + 1, j)) &&
                                shelf.getTile(i, j).equals(shelf.getTile(i + 2, j)) &&
                                shelf.getTile(i, j).equals(shelf.getTile(i + 3, j))
                        ) {
                            if (minCount >= checked[i][j] + checked[i + 1][j] + checked[i + 2][j] + checked[i + 3][j]) {
                                valid[i][j] = false;
                                valid[i + 1][j] = false;
                                valid[i + 2][j] = false;
                                valid[i + 3][j] = false;
                                counter++;
                            }
                        }
                    }
                }
            }
        }
        return counter>=4;
    }
}