package main.java.it.polimi.ingsw.Model;

import main.java.it.polimi.ingsw.Controller.Response;
import main.java.it.polimi.ingsw.ModelExceptions.NotToRefillException;
import main.java.it.polimi.ingsw.ModelExceptions.WrongTurnException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static main.java.it.polimi.ingsw.Controller.ResponseType.*;

public class Model {
    private final int gameId;
    private boolean gameEnd;
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
    public Model(int gameId, List<Player> players) throws IOException, NotToRefillException, WrongTurnException {
        this.gameId = gameId;
        this.players = players;
        this.gameEnd = false;
        numPlayers = players.size();

        for(int i=0; i<numPlayers; i++){
            players.get(i).setTurnId(i);
            players.get(i).setModel(this);
        }

        double doubleRandomNumber;
        int randomNumber;
        List<Integer> valuesPG = new ArrayList<>();

        for(int i=0; i<numPlayers; i++){
            doubleRandomNumber = Math.random() * 12; //from 0 to 11
            randomNumber = (int)doubleRandomNumber+1; //value from 1 to 12
            while(valuesPG.contains(randomNumber)){
                doubleRandomNumber = Math.random() * 12;
                randomNumber = (int)doubleRandomNumber+1;
            }
            valuesPG.add(randomNumber);
        }
        initPersonalGoal(valuesPG);

        board = new Board(numPlayers);
        firstToEnd = -1;

        doubleRandomNumber = Math.random() * numPlayers; //from 0 to numPlayers-1
        randomNumber = (int)doubleRandomNumber;
        firstToStart = randomNumber;
        turnId = randomNumber;

        commonGoals = new CommonGoal[2];
        int[] valuesCG = new int[2];

        doubleRandomNumber = Math.random() * 12; //from 0 to 11
        randomNumber = (int)doubleRandomNumber+1; //value from 1 to 12
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
        getPlayerByTurnId(turnId).beginTurn();

    }

    /**
     * Method that is called at the end of a turn, before the turn passes to the next player
     * @author Nicole Filippi, Pasquale Gioia
     */
    public void nextTurn() throws NotToRefillException, WrongTurnException {

        if(getPlayerByTurnId(turnId).getPointsCommonGoal()[0]==0 && commonGoals[0].check(getPlayerByTurnId(turnId).getShelf())){
            int tmpPointsWon = commonGoals[0].getPointsLeft();
            getPlayerByTurnId(turnId).setPointsCommonGoal(0, commonGoals[0].pullPoints());
            updateCommonGoalWon(turnId,0,tmpPointsWon,commonGoals[0].getPointsLeft());
        }
        if(getPlayerByTurnId(turnId).getPointsCommonGoal()[1]==0 && commonGoals[1].check(getPlayerByTurnId(turnId).getShelf())){
            int tmpPointsWon = commonGoals[1].getPointsLeft();
            getPlayerByTurnId(turnId).setPointsCommonGoal(1, commonGoals[1].pullPoints());
            updateCommonGoalWon(turnId,1,tmpPointsWon,commonGoals[1].getPointsLeft());
        }
        if(firstToEnd==-1 && getPlayerByTurnId(turnId).getShelf().isFull()){
            //check if player's shelf is full
            firstToEnd=turnId;
            updateShelfCompleted(turnId);
        }

        if(getNumPlayersConnected()<=1){
            //Sending one player left timer has started response to connected players

            int remaining = 60000;

            while(remaining>0 && getNumPlayersConnected()==1){
                try{
                    updateOnlyOneConnectedTimer(remaining);
                    remaining-=1000;
                    TimeUnit.SECONDS.sleep(1);
                }catch(Exception e){
                    remaining-=5;
                }
            }

            //Timer has ended:
            // If no player has connected the game should end
            if(getNumPlayersConnected()<=1){
                //End game due to lack of enough players -- (interrupted = 1)
                updateGameEnd(1);
                this.gameEnd = true;
            } else {
                //If a player has connected the game should go on
                do{
                    turnId = (turnId + 1) % numPlayers;//next player
                } while (!getPlayerByTurnId(turnId).isConnected());

                if (getNumPlayersConnected() > 1 && firstToEnd > -1 && turnId == firstToStart) {
                    //End game as natural game flow -- (interrupted = 0)
                    countPoints();
                    updateGameEnd(0);
                } else {
                    if (board.checkFill()) {  //check if the board has to be refilled
                        board.refill();
                        updateBoard();
                    }
                    getPlayerByTurnId(turnId).beginTurn();
                    updateNewTurn(getTurnId(),players.get(getTurnId()).getPickableTiles());
                }
            }

        } else {
            //There is more than one player connected
            do {
                turnId = (turnId + 1) % numPlayers;//next player
            } while (!getPlayerByTurnId(turnId).isConnected());

            if (getNumPlayersConnected() > 1 && firstToEnd > -1 && turnId == firstToStart) {
                //End game as natural game flow -- (interrupted =0)
                countPoints();
                updateGameEnd(0);
            } else {
                if (board.checkFill()) {
                    //check if the board has to be refilled
                    board.refill();
                    updateBoard();
                }
                getPlayerByTurnId(turnId).beginTurn();
                updateNewTurn(getTurnId(),players.get(getTurnId()).getPickableTiles());
            }
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

        for(int i=0; i<values.length; i++)
        {
            switch (values[i]){
                case 1 :
                    commonGoals[i] = new CommonGoal1(numPlayers);
                    break;
                case 2 :
                    commonGoals[i] = new CommonGoal2(numPlayers);
                    break;
                case 3 :
                    commonGoals[i] = new CommonGoal3(numPlayers);
                    break;
                case 4 :
                    commonGoals[i] = new CommonGoal4(numPlayers);
                    break;
                case 5 :
                    commonGoals[i] = new CommonGoal5(numPlayers);
                    break;
                case 6 :
                    commonGoals[i] = new CommonGoal6(numPlayers);
                    break;
                case 7 :
                    commonGoals[i] = new CommonGoal7(numPlayers);
                    break;
                case 8 :
                    commonGoals[i] = new CommonGoal8(numPlayers);
                    break;
                case 9 :
                    commonGoals[i] = new CommonGoal9(numPlayers);
                    break;
                case 10 :
                    commonGoals[i] = new CommonGoal10(numPlayers);
                    break;
                case 11 :
                    commonGoals[i] = new CommonGoal11(numPlayers);
                    break;
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
        for(int i=0; i<values.size(); i++)
        {
            players.get(i).setPersonalGoal(new PersonalGoal(values.get(i)));
        }

    }


    /**
     * Method that counts players' points at the end of the game
     * @author Pasquale Gioia
     */
    private void countPoints(){
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
        this.gameEnd = true;
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

    public boolean isGameFinished() {
        return gameEnd;
    }

    public Player getPlayerByTurnId(int turnId){
        Player turnIdPlayer = null;
        for(Player p: players)
            if(p.getTurnId()==turnId) {
                turnIdPlayer = p;
            }

        return turnIdPlayer;
    }

    public int getNumPlayersConnected(){
        int numPlayers = 0;
        for(Player p: getPlayers())
            if(p.isConnected())
                numPlayers++;
        return numPlayers;
    }


    /**
     * This method sends the response to all players
     * @author Pasquale Gioia, Alessandro Annechini
     * @param response The response to be sent
     */
    public void broadcast(Response response){
        for(Player p: players){
            p.update(response);
        }
    }

    /**
     * This method sends the NEW_TURN response
     * @author Pasquale Gioia, Alessandro Annechini
     * @param turnId Turn id of the current player
     * @param pickableTiles Pickable tiles
     */
    public void updateNewTurn(int turnId, boolean[][] pickableTiles){
        Response response = new Response(NEW_TURN);
        response.setIntParameter("currentPlayerId",turnId);
        response.setObjParameter("pickableTiles",pickableTiles);
        broadcast(response);
    }

    /**
     * This method sends the UPDATE_BOARD response
     * @author Pasquale Gioia, Alessandro Annechini
     */
    public void updateBoard(){
        Response response = new Response(UPDATE_BOARD);
        response.setObjParameter("board",board.toBean());
        broadcast(response);
    }

    /**
     * This method sends the UPDATE_PLAYER_SHELF response
     * @author Pasquale Gioia, Alessandro Annechini
     * @param turnId Player who updated the shelf
     * @param shelf Updated shelf
     */
    public void updateShelf(int turnId, Shelf shelf){
        Response response = new Response(UPDATE_PLAYER_SHELF);
        response.setObjParameter("shelf", shelf.toBean());
        response.setIntParameter("playerId", turnId);
        broadcast(response);
    }

    /**
     * This method sends the GAME_ENDED response
     * @author Pasquale Gioia, Alessandro Annechini
     * @param interrupted 1 if the game ended due to disconnections, 0 if the game ended correctly
     */
    public void updateGameEnd(int interrupted){
        Response end = new Response(GAME_ENDED);
        end.setObjParameter("finalPoints", finalPoints);
        end.setIntParameter("interrupted", interrupted);
        broadcast(end);
    }

    /**
     * This method sends the ONLY_ONE_CONNECTED_TIMER response
     * @author Pasquale Gioia, Alessandro Annechini
     * @param milliseconds Milliseconds remaining to victory
     */
    public void updateOnlyOneConnectedTimer(int milliseconds){
        Response timerStarted = new Response(ONLY_ONE_CONNECTED_TIMER);
        timerStarted.setIntParameter("timermilliseconds", milliseconds);
        broadcast(timerStarted);
    }

    /**
     * This method sends the SHELF_COMPLETED response
     * @author Pasquale Gioia, Alessandro Annechini
     * @param turnId Player who completed the shelf
     */
    public void updateShelfCompleted(int turnId){
        Response fin = new Response(SHELF_COMPLETED);
        fin.setIntParameter("playerId",turnId);
        broadcast(fin);
    }

    /**
     * This method sends the COMMON_GOAL_WON response
     * @author Pasquale Gioia, Alessandro Annechini
     * @param turnId Player who completed the common goal
     * @param commonGoalId Id of the common goal (0 or 1)
     * @param pointsWon Points won
     * @param pointsRemaining Points remaining
     */
    public void updateCommonGoalWon(int turnId, int commonGoalId, int pointsWon, int pointsRemaining){
        Response commonGoalWon = new Response(COMMON_GOAL_WON);
        commonGoalWon.setIntParameter("PlayerId", turnId);
        commonGoalWon.setIntParameter("PointsWon", pointsWon);
        commonGoalWon.setIntParameter("CommonGoalId", commonGoalId);
        commonGoalWon.setIntParameter("RemainingPoints", pointsRemaining);
        broadcast(commonGoalWon);
    }

    /**
     * This method sends the PLAYER_DISCONNECTED response
     * @author Pasquale Gioia, Alessandro Annechini
     * @param turnId Player who disconnected
     */
    public void updatePlayerDisconnected(int turnId){
        Response disc = new Response(PLAYER_DISCONNECTED);
        disc.setIntParameter("playerId", turnId);
        broadcast(disc);
    }

    /**
     * This method sends the PLAYER_RECONNECTED response
     * @author Pasquale Gioia, Alessandro Annechini
     * @param turnId Player who reconnected
     */
    public void updatePlayerReconnected(int turnId){
        Response disc = new Response(PLAYER_RECONNECTED);
        disc.setIntParameter("playerId", turnId);
        broadcast(disc);
    }

    /**
     * This method sends the GAME_STARTED response
     * @author Pasquale Gioia, Alessandro Annechini
     * @param player The player that will receive the response
     * @param isStart 1 if the game has just started, 0 otherwise
     * @return The GAME_STARTED response
     */
    public Response createGameStartedResponse(Player player, int isStart){

        Response recon = new Response(GAME_STARTED);

        recon.setObjParameter("board", board.toBean());
        recon.setIntParameter("firstPlayerId", firstToStart);
        recon.setIntParameter("currentPlayer", turnId);
        recon.setIntParameter("isStart", isStart);
        recon.setIntParameter("commonGoal1", commonGoals[0].getIndex());
        recon.setIntParameter("commonGoal2", commonGoals[1].getIndex());
        recon.setObjParameter("chat", chat.toBean(player));
        recon.setIntParameter("commonGoalsRemainingPoint1", commonGoals[0].getPointsLeft());
        recon.setIntParameter("commonGoalsRemainingPoint2", commonGoals[1].getPointsLeft());
        recon.setStrParameter("commonGoalDescription1", commonGoals[0].getDescription());
        recon.setStrParameter("commonGoalDescription2", commonGoals[1].getDescription());
        List<ShelfBean> shelves = new ArrayList<>();
        List<String> nicknames = new ArrayList<>();
        List<Integer> turnIds = new ArrayList<>();
        List<Integer> commonGoalPoints1 = new ArrayList<>();
        List<Integer> commonGoalPoints2 = new ArrayList<>();
        List<Boolean> connected = new ArrayList<>();

        for(Player p: players){
            shelves.add(p.getShelf().toBean());
            nicknames.add(p.getNickname());
            turnIds.add(p.getTurnId());
            commonGoalPoints1.add(p.getPointsCommonGoal()[0]);
            commonGoalPoints2.add(p.getPointsCommonGoal()[1]);
            connected.add(p.getConnected());
        }

        recon.setObjParameter("connected", connected);
        recon.setObjParameter("shelves", shelves);
        recon.setObjParameter("nicknames", nicknames);
        recon.setObjParameter("turnIds", turnIds);
        recon.setObjParameter("personalGoalMatrix", player.getPersonalGoal().toBean());
        recon.setIntParameter("personalGoal", player.getPersonalGoal().getIndex());
        recon.setObjParameter("commongoalpoints1", commonGoalPoints1);
        recon.setObjParameter("commongoalpoints2", commonGoalPoints2);
        return recon;
    }

    /**
     * This method sends the PUT_IN_COLUMN_RESULT response
     * @author Pasquale Gioia, Alessandro Annechini
     * @param player The player that modified the shelf
     * @param turnFinished 0 if the turn has not finished, 1 otherwise
     */
    public void responsePutInColumnResult(Player player,int turnFinished){
        Response response = new Response(PUT_IN_COLUMN_RESULT);
        response.setStrParameter("result","success");
        response.setIntParameter("turnFinished", turnFinished);
        response.setObjParameter("buffer", player.getPickedTiles());
        player.update(response);
    }

    /**
     * This method sends the SELECT_TILE_RESULT response
     * @author Pasquale Gioia, Alessandro Annechini
     * @param player The player that selected the tile
     * @param pickableTiles Pickable tiles (first time)
     * @param buffer Buffer of picked tiles (second time)
     */
    public void responseSelectTileResult(Player player,  boolean[][] pickableTiles, Tile[] buffer ){
        Response response = new Response(SELECT_TILE_RESULT);
        response.setStrParameter("result","success");
        response.setObjParameter("pickabletiles",pickableTiles);
        response.setObjParameter("buffer",buffer);
        player.update(response);
    }

    /**
     * This method sends the NEW_MEX_CHAT response
     * @author Pasquale Gioia, Alessandro Annechini
     * @param toUpdate The players that will receive the update
     */
    public void updateChat(List<Player> toUpdate){
        Response response = new Response(NEW_MEX_CHAT);
        for(Player p : toUpdate){
            response.setObjParameter("chat",chat.toBean(p));
            p.update(response);
        }
    }

    /**
     * This method sends the GAME_STARTED response
     * @author Pasquale Gioia, Alessandro Annechini
     */
    public void updateStartGame(){
        for(Player p : players){
            p.update(createGameStartedResponse(p,1));
        }
    }

}
