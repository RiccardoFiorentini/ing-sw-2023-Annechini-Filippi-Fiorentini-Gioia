package main.java.it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.List;
public class Model {
    private int gameId;
    private int numPlayers;
    private List<Player> players;
    private Board board;
    private int turnId; //id of the player whose turn is
    private int firstToEnd;
    private int firstToStart;
    private CommonGoal[] commonGoals;
    private List<Integer> finalPoints;
    private Chat chat;

    /**
     * Class's constructor
     * @author: Nicole Filippi
     * @param: gameId is the id of the game choose by the Controller (useful for multiple games)
     * @param: players is the list of players sent from the Controller
     */
    public Model(int gameId, List<Player> players){
        this.gameId = gameId;
        this.players = players;
        numPlayers = players.size();
        board = new Board(numPlayers);
        firstToEnd = -1;

        // Math.random() generates a random number between 0.0 and 0.999
        // Math.random()*5 is between 0.0 and 3.999
        double doubleRandomNumber = Math.random() * 4;
        // conversion from double to int
        int randomNumber = (int)doubleRandomNumber;
        firstToStart = randomNumber;
        turnId = randomNumber;

        commonGoals = new CommonGoal[2];
        doubleRandomNumber = Math.random() * 12;
        randomNumber = (int)doubleRandomNumber+1;
        switch (randomNumber){
            case 1 :
                commonGoals[0] = new CommonGoal1(numPlayers);
            case 2 :
                commonGoals[0] = new CommonGoal2(numPlayers);
            case 3 :
                commonGoals[0] = new CommonGoal3(numPlayers);
            case 4 :
                commonGoals[0] = new CommonGoal4(numPlayers);
            case 5 :
                commonGoals[0] = new CommonGoal5(numPlayers);
            case 6 :
                commonGoals[0] = new CommonGoal6(numPlayers);
            case 7 :
                commonGoals[0] = new CommonGoal7(numPlayers);
            case 8 :
                commonGoals[0] = new CommonGoal8(numPlayers);
            case 9 :
                commonGoals[0] = new CommonGoal9(numPlayers);
            case 10 :
                commonGoals[0] = new CommonGoal10(numPlayers);
            case 11 :
                commonGoals[0] = new CommonGoal11(numPlayers);
            case 12 :
                commonGoals[0] = new CommonGoal12(numPlayers);
        }
        doubleRandomNumber = Math.random() * 12;
        int randomNumber2 = (int)doubleRandomNumber+1;
        while(randomNumber2==randomNumber){
            doubleRandomNumber = Math.random() * 12;
            randomNumber2 = (int)doubleRandomNumber+1;
        }
        switch (randomNumber2){
            case 1 :
                commonGoals[1] = new CommonGoal1(numPlayers);
            case 2 :
                commonGoals[1] = new CommonGoal2(numPlayers);
            case 3 :
                commonGoals[1] = new CommonGoal3(numPlayers);
            case 4 :
                commonGoals[1] = new CommonGoal4(numPlayers);
            case 5 :
                commonGoals[1] = new CommonGoal5(numPlayers);
            case 6 :
                commonGoals[1] = new CommonGoal6(numPlayers);
            case 7 :
                commonGoals[1] = new CommonGoal7(numPlayers);
            case 8 :
                commonGoals[1] = new CommonGoal8(numPlayers);
            case 9 :
                commonGoals[1] = new CommonGoal9(numPlayers);
            case 10 :
                commonGoals[1] = new CommonGoal10(numPlayers);
            case 11 :
                commonGoals[1] = new CommonGoal11(numPlayers);
            case 12 :
                commonGoals[1] = new CommonGoal12(numPlayers);
        }

        finalPoints = new ArrayList<>();
        chat = new Chat(players);

    }

    /**
     * Method that is called at the end of a turn, before the turn passes to the next player
     * @author: Nicole Filippi
     */
    public void nextTurn(){
        commonGoals[0].check(players.get(turnId).getShelf());
        commonGoals[1].check(players.get(turnId).getShelf());
        if(firstToEnd==-1 && players.get(turnId).getShelf().isFull())
            firstToEnd=turnId;
        turnId = (turnId+1)%numPlayers;
        if(firstToEnd>-1 && turnId==firstToStart) {
            //ending the game
        }
    }

    /**
     * Method that shows if two tiles are the same color
     * @author: Nicole Filippi
     * @param: t1 is the first tile
     * @param: t2 is the second tile
     * @return: true if the tiles are the same color, false in other cases
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
