package main.java.it.polimi.ingsw.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class Model {
    private final int gameId;
    private final int numPlayers;
    private final List<Player> players;
    private final Board board;
    private int turnId; //id of the player whose turn is (0-3)
    private int firstToEnd;
    private int firstToStart;
    private final CommonGoal[] commonGoals;
    private final List<Integer> finalPoints;
    private final Chat chat;

    /**
     * Class' constructor
     * @author Nicole Filippi
     * @param gameId is the id of the game choose by the Controller (useful for multiple games)
     * @param players is the list of players sent from the Controller
     */
    public Model(int gameId, List<Player> players) throws IOException {
        this.gameId = gameId;
        this.players = players;
        numPlayers = players.size();

        double doubleRandomNumber;
        int randomNumber;
        List<Integer> valuesPG = new ArrayList<>();

        for(int i=0; i<numPlayers; i++){
            doubleRandomNumber = Math.random() * 12;
            randomNumber = (int)doubleRandomNumber+1;
            while(valuesPG.contains(randomNumber)){
                doubleRandomNumber = Math.random() * 12;
                randomNumber = (int)doubleRandomNumber+1;
            }
            valuesPG.add(randomNumber);
        }
        initPersonalGoal(valuesPG);

        board = new Board(numPlayers);
        firstToEnd = -1;

        doubleRandomNumber = Math.random() * 4;
        randomNumber = (int)doubleRandomNumber;
        firstToStart = randomNumber;
        turnId = randomNumber;

        commonGoals = new CommonGoal[2];
        int[] valuesCG = new int[2];

        doubleRandomNumber = Math.random() * 12;
        randomNumber = (int)doubleRandomNumber+1;
        doubleRandomNumber = Math.random() * 12;
        int randomNumber2 = (int)doubleRandomNumber+1;
        while(randomNumber2==randomNumber){
            doubleRandomNumber = Math.random() * 12;
            randomNumber2 = (int)doubleRandomNumber+1;
        }

        valuesCG[0]=randomNumber;
        valuesCG[1]=randomNumber2;
        initCommonGoal(valuesCG);

        finalPoints = new ArrayList<>();
        chat = new Chat(players);
        players.get(turnId).beginTurn();
    }

    /**
     * Method that is called at the end of a turn, before the turn passes to the next player
     * @author Nicole Filippi
     */
    public void nextTurn(){
        if(players.get(turnId).getPointsCommonGoal()[0]==0 && commonGoals[0].check(players.get(turnId).getShelf())){
            players.get(turnId).setPointsCommonGoal(0, commonGoals[0].pullPoints());
        }
        if(players.get(turnId).getPointsCommonGoal()[1]==0 && commonGoals[1].check(players.get(turnId).getShelf())){
            players.get(turnId).setPointsCommonGoal(1, commonGoals[1].pullPoints());
        }

        if(firstToEnd==-1 && players.get(turnId).getShelf().isFull())   //check if player's board is full
            firstToEnd=turnId;

        turnId = (turnId+1)%numPlayers;     //next player

        if(firstToEnd>-1 && turnId==firstToStart) {         //the play is finished
            int points;
            for(int i=0; i<numPlayers; i++){
                points=0;
                points+=players.get(i).getPointsCommonGoal()[0];
                points+=players.get(i).getPointsCommonGoal()[1];
                points+=players.get(i).getPersonalGoal().getPoints(players.get(i).getShelf());
                if(i==firstToEnd)
                    points++;
                points+=players.get(i).getShelf().getGroupsPoints();
                finalPoints.add(points);
            }
            turnId=-1;
        }
        else{
            if(board.checkFill())   //check if the board has to be refilled
                board.refill();
            players.get(turnId).beginTurn();
        }
    }

    /**
     * Method that shows if two tiles are the same color
     * @author Nicole Filippi
     * @deprecated replaced by the method equals in Tile class
     * @param t1 is the first tile
     * @param t2 is the second tile
     * @return true if the tiles are the same color, false in other cases
     */
    public static boolean equalsTiles(Tile t1, Tile t2){
        if((t1==Tile.GREEN1||t1==Tile.GREEN2||t1==Tile.GREEN3) && (t2==Tile.GREEN1||t2==Tile.GREEN2||t2==Tile.GREEN3))
            return true;
        if((t1==Tile.BLUE1||t1==Tile.BLUE2||t1==Tile.BLUE3) && (t2==Tile.BLUE1||t2==Tile.BLUE2||t2==Tile.BLUE3))
            return true;
        if((t1==Tile.PINK1||t1==Tile.PINK2||t1==Tile.PINK3) && (t2==Tile.PINK1||t2==Tile.PINK2||t2==Tile.PINK3))
            return true;
        if((t1==Tile.WHITE1||t1==Tile.WHITE2||t1==Tile.WHITE3) && (t2==Tile.WHITE1||t2==Tile.WHITE2||t2==Tile.WHITE3))
            return true;
        if((t1==Tile.ORANGE1||t1==Tile.ORANGE2||t1==Tile.ORANGE3) && (t2==Tile.ORANGE1||t2==Tile.ORANGE2||t2==Tile.ORANGE3))
            return true;
        if((t1==Tile.CYAN1||t1==Tile.CYAN2||t1==Tile.CYAN3) && (t2==Tile.CYAN1||t2==Tile.CYAN2||t2==Tile.CYAN3))
            return true;
        return false;
    }

    /**
     * Method that sets 2 different common goals
     * @author Nicole Filippi
     * @param values is an array of different random integers
     */
    private void initCommonGoal(int[] values){

        for(int i=0; i< values.length; i++)
        {
            switch (values[i]){
                case 1 :
                    commonGoals[i] = new CommonGoal1(numPlayers);
                case 2 :
                    commonGoals[i] = new CommonGoal2(numPlayers);
                case 3 :
                    commonGoals[i] = new CommonGoal3(numPlayers);
                case 4 :
                    commonGoals[i] = new CommonGoal4(numPlayers);
                case 5 :
                    commonGoals[i] = new CommonGoal5(numPlayers);
                case 6 :
                    commonGoals[i] = new CommonGoal6(numPlayers);
                case 7 :
                    commonGoals[i] = new CommonGoal7(numPlayers);
                case 8 :
                    commonGoals[i] = new CommonGoal8(numPlayers);
                case 9 :
                    commonGoals[i] = new CommonGoal9(numPlayers);
                case 10 :
                    commonGoals[i] = new CommonGoal10(numPlayers);
                case 11 :
                    commonGoals[i] = new CommonGoal11(numPlayers);
                case 12 :
                    commonGoals[i] = new CommonGoal12(numPlayers);
            }
        }
    }

    /**
     * Method that sets numPlayers different personal goals
     * @author Nicole Filippi
     * @param values is a list of different random integers
     */
    private void initPersonalGoal(List<Integer> values) throws IOException {
        for(int i=0; i< values.size(); i++)
        {
            players.get(i).setPersonalGoal(new PersonalGoal(i));
        }

    }

    public int getGameId() {
        return gameId;
    }
    public int getNumPlayers() {
        return numPlayers;
    }
    public List<Player> getPlayers() {
        return players;
    }
    public Board getBoard() {
        return board;
    }
    public int getTurnId() {
        return turnId;
    }
    public int getFirstToEnd() {
        return firstToEnd;
    }
    public int getFirstToStart() {
        return firstToStart;
    }
    public CommonGoal[] getCommonGoals() {
        return commonGoals;
    }
    public List<Integer> getFinalPoints() {
        return finalPoints;
    }
    public Chat getChat() {
        return chat;
    }
}
