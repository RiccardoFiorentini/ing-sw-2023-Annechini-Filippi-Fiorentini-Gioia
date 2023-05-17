package main.java.it.polimi.ingsw.Client;

import main.java.it.polimi.ingsw.Connection.ClientConnectionHandler;
import main.java.it.polimi.ingsw.Controller.Command;
import main.java.it.polimi.ingsw.Controller.CommandType;
import main.java.it.polimi.ingsw.Controller.Response;
import main.java.it.polimi.ingsw.Model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class GUI {
    private final ClientConnectionHandler cch;
    private String playerNickname;
    private int playerTurnId;
    private Scanner scan = new Scanner(System.in);
    private ClientState state;

    //GAME PARAMETERS
    private int firstPlayerId;
    private int[] commonGoalsId;
    private int[] commonGoalsRemainingPoint;
    private int currPlayerId;
    private BoardBean board;
    private ChatBean chat;
    private List<ShelfBean> shelves;
    private List<String> nicknames;
    private List<Integer> turnIds;
    private TileColor[][] personalGoal; //6x5
    private List<Integer> commonGoalPoints1;
    private List<Integer> commonGoalPoints2;
    private List<Boolean> connected;
    private Tile[] buffer;

    /**
     * Class' constructor
     * @param cch is the ClientConnectionHandler associated
     * @author Nicole Filippi
     */
    public GUI(ClientConnectionHandler cch) {
        this.cch=cch;
        playerNickname = null;
        playerTurnId = -1;
        commonGoalsId = new int[2];
        commonGoalsRemainingPoint = new int[2];
        buffer=new Tile[2];
    }

    /**
     * Starts the View of the game, creates two thread: the one that handles the Ping
     * to the server and the one that starts the view.
     * @author Nicole Filippi
     */
    public void start(){
        new Thread(()->onStartup()).start();
        while(true){
            Response resp=null;
            try {
                resp = cch.getNextResponse();
            }catch(Exception e){
                //e.printStackTrace();
            }

            final Response response=resp;

            if(response != null){
                new Thread(()->handleResponse(response)).start();
            }
        }
    }

    /**
     * Method that handles the startup
     * @author Nicole Filippi
     */
    public void onStartup() {
        //TODO
    }

    /**
     * Method that sends a given command to the server
     * @author Nicole Filippi
     * @param command is the command that has to be sent
     */
    public void sendCommand(Command command){
        try{
            cch.sendCommand(command);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method that handles responses sent by the server to the client
     * @author Nicole Filippi
     * @param resp is the response sent from the server
     */
    public void handleResponse(Response resp) {
        if(resp == null) return;
        switch(resp.getResponseType()) {
            case LOGIN_ERROR:
                //TODO
                break;

            case LOGIN_CONFIRMED:
                playerNickname=resp.getStrParameter("nickname");
                //TODO
                break;

            case ASK_PLAYERS_NUM:
                if (resp.getStrParameter("result") == null) { //first request

                }
                else if (resp.getStrParameter("result").equals("success")) {  //accepted value
                    //TODO
                }else{ //not accepted value
                    //TODO
                }
                break;

            case GAME_STARTED:
                Command command = new Command(CommandType.GAME_JOINED);
                sendCommand(command);
                buffer = new Tile[]{Tile.EMPTY,Tile.EMPTY,Tile.EMPTY};
                firstPlayerId=resp.getIntParameter("firstPlayerId");
                commonGoalsId[0]=resp.getIntParameter("commongoal1");
                commonGoalsId[1]=resp.getIntParameter("commongoal2");
                commonGoalsRemainingPoint[0]=resp.getIntParameter("commongoalsremainingpoint1");
                commonGoalsRemainingPoint[1]=resp.getIntParameter("commongoalsremainingpoint2");
                currPlayerId=resp.getIntParameter("currentplayer");
                board=(BoardBean) resp.getObjParameter("board");
                chat=(ChatBean)resp.getObjParameter("chat");
                shelves=(List<ShelfBean>)resp.getObjParameter("shelves");
                nicknames=(List<String>)resp.getObjParameter("nicknames");
                turnIds=(List<Integer>)resp.getObjParameter("turnIds");
                for(int i=0; i<turnIds.size(); i++){
                    if((nicknames).get(i).equals(playerNickname))
                        playerTurnId=turnIds.get(i);
                }
                personalGoal=(TileColor[][])resp.getObjParameter("personalgoalmatrix");
                commonGoalPoints1=(List<Integer>)resp.getObjParameter("commongoalpoints1");
                commonGoalPoints2=(List<Integer>)resp.getObjParameter("commongoalpoints2");
                connected=(List<Boolean>)resp.getObjParameter("connected");
                //TODO
                break;

            case NEW_MEX_CHAT:
                chat=(ChatBean) resp.getObjParameter("chat") ;
                //TODO
                break;

            case NEW_TURN:
                currPlayerId=resp.getIntParameter("CurrentPlayerId");
                if(playerTurnId==currPlayerId){
                    //TODO
                }else{
                    //TODO
                }
                //TODO
                break;

            case SELECT_COLUMN_RESULT:
                if("success".equals(resp.getStrParameter("result"))){ //andato a buon fine
                   //TODO
                }else{
                    //TODO
                }
                //TODO
                break;

            case SELECT_TILE_RESULT:
                if("success".equals(resp.getStrParameter("result"))){ //andato a buon fine
                    //TODO
                    //if(first tile){}
                    //else{
                    //  buffer=(Tile[]) resp.getObjParameter("buffer");
                    // }
                }else{
                    //TODO
                }
                //TODO
                break;

            case PUT_IN_COLUMN_RESULT:
                if("success".equals(resp.getStrParameter("result"))){ //andato a buon fine
                    //TODO
                    buffer=(Tile[]) resp.getObjParameter("buffer");
                    if(resp.getIntParameter("turnFinished")==0) {
                       //TODO
                    }else{
                        //TODO
                    }
                }else{
                    //TODO
                }
                //TODO
                break;

            case UPDATE_BOARD:
                board=(BoardBean)resp.getObjParameter("board");
                //TODO
                break;

            case UPDATE_PLAYER_SHELF:
                shelves.set(resp.getIntParameter("playerid"),(ShelfBean)resp.getObjParameter("shelf"));
                //TODO
                break;

            case PLAYER_DISCONNECTED:
                connected.set(resp.getIntParameter("playerid"),false);
                //TODO
                break;

            case PLAYER_RECONNECTED:
                connected.set(resp.getIntParameter("playerid"),true);
                //TODO
                break;

            case ONLY_ONE_CONNECTED:
                //TODO
                break;

            case ONLY_ONE_CONNECTED_TIMER:
                //TODO
                break;

            case GAME_ENDED:
                List<Integer> points;
                if(resp.getIntParameter("interrupted")==0){ //finished correctly
                    points=(List<Integer>)resp.getObjParameter("finalPoints");
                    List<Integer> tmpPoints = new ArrayList<>(points);
                    List<String> tmpNick = new ArrayList<>(nicknames);
                    List<Integer> resPoints = new ArrayList<>();
                    List<String> resNick = new ArrayList<>();
                    int max, maxPos;
                    while(tmpNick.size()>0){
                        max=-1;
                        maxPos=-1;
                        for(int i=0; i<tmpNick.size(); i++){
                            if(tmpPoints.get(i)>max) {
                                max = tmpPoints.get(i);
                                maxPos = i;
                            }
                        }
                        resPoints.add(max);
                        resNick.add(tmpNick.get(maxPos));
                        tmpPoints.remove((Integer)max);
                        tmpNick.remove(maxPos);
                    }
                    //TODO
                }
                else{
                    //TODO
                }
                //TODO
                break;

            case COMMON_GOAL_WON:
                if(resp.getIntParameter("commongoalid")==0) {
                    commonGoalPoints1.set(resp.getIntParameter("playerid"), resp.getIntParameter("pointswon"));
                    commonGoalsRemainingPoint[0]=resp.getIntParameter("remainingpoints");
                }else{
                    commonGoalPoints2.set(resp.getIntParameter("playerid"), resp.getIntParameter("pointswon"));
                    commonGoalsRemainingPoint[0]=resp.getIntParameter("remainingpoints");
                }
                //TODO
                break;

            case SHELF_COMPLETED:
                //TODO
        }
    }


    public void doLogin(String nickname){
        if(nickname==null || nickname.equals("")){
            //TODO handle error
            return;
        }
        Command command=new Command(CommandType.LOGIN);
        command.setStrParameter("nickname", nickname);
        sendCommand(command);
    }

    public void doPlayersNum(int num){
        if(num<2 || num>4){
            //TODO handle error
            return;
        }
        Command command = new Command(CommandType.PLAYERS_NUM);
        command.setIntParameter("num", num);
        sendCommand(command);
    }

    public void doSelectColumn(int column){
        if(column<0||column>4){
            //TODO handle error
            return;
        }
        Command command = new Command(CommandType.SELECT_COLUMN);
        command.setIntParameter("value", column);
        sendCommand(command);
    }

    public void doSelectTile(int row, int col){
        if(col<0||col>8||row<0||row>8){
            //TODO handle error
            return;
        }
        Command command = new Command(CommandType.SELECT_TILE);
        command.setIntParameter("row", row);
        command.setIntParameter("col", col);
        sendCommand(command);
    }

    public void doPutInColumn(int index){
        if(index<0||index>2){
            //TODO handle error
            return;
        }
        Command command = new Command(CommandType.PUT_IN_COLUMN);
        command.setIntParameter("index", index);
        sendCommand(command);
    }

    public void doSendMex(String message, String receiver){
        if(message==null || message.equals("")){
            //TODO handle error
            return;
        }
        Command command = new Command(CommandType.SEND_MEX_CHAT);
        command.setStrParameter("text", message);
        command.setStrParameter("receiver", receiver); //null if broadcast message
        sendCommand(command);
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    public int getPlayerTurnId() {
        return playerTurnId;
    }

    public Scanner getScan() {
        return scan;
    }

    public ClientState getState() {
        return state;
    }

    public int getFirstPlayerId() {
        return firstPlayerId;
    }

    public int[] getCommonGoalsId() {
        return commonGoalsId;
    }

    public int[] getCommonGoalsRemainingPoint() {
        return commonGoalsRemainingPoint;
    }

    public int getCurrPlayerId() {
        return currPlayerId;
    }

    public BoardBean getBoard() {
        return board;
    }

    public ChatBean getChat() {
        return chat;
    }

    public List<ShelfBean> getShelves() {
        return shelves;
    }

    public List<String> getNicknames() {
        return nicknames;
    }

    public List<Integer> getTurnIds() {
        return turnIds;
    }

    public TileColor[][] getPersonalGoal() {
        return personalGoal;
    }

    public List<Integer> getCommonGoalPoints1() {
        return commonGoalPoints1;
    }

    public List<Integer> getCommonGoalPoints2() {
        return commonGoalPoints2;
    }

    public List<Boolean> getConnected() {
        return connected;
    }

    public Tile[] getBuffer() {
        return buffer;
    }

}
