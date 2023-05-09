package main.java.it.polimi.ingsw.Client;

import main.java.it.polimi.ingsw.Connection.ClientConnectionHandler;
import main.java.it.polimi.ingsw.Controller.Command;
import main.java.it.polimi.ingsw.Controller.CommandType;
import main.java.it.polimi.ingsw.Controller.Response;
import main.java.it.polimi.ingsw.Model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TUI implements View{
    private final ClientConnectionHandler cch;
    private String playerNickname;
    private int playerTurnId;
    private Scanner scan = new Scanner(System.in);
    private ClientState state;

    //GAME PARAMETERS
    private int firstPlayerId;
    private int[] commonGoalsId;
    private int[] commonGoalsRemainingPoint;
    private String[] commonGoalsDesc;
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
    private String phase=null;

    /**
     * Class' constructor
     * @author Nicole Filippi
     * @param cch is the ClientConnectionHandler associated (Socket or RMI)
     */
    public TUI(ClientConnectionHandler cch) {
        this.cch=cch;
        playerNickname = null;
        playerTurnId = -1;
        commonGoalsId = new int[2];
        commonGoalsRemainingPoint = new int[2];
        commonGoalsDesc = new String[2];
        buffer=new Tile[2];
    }


    public void start(){
        state=ClientState.BEFORE_LOGIN;
        printTitle();
        clearConsole();
        printTitle();
        System.out.println("Choose your nickname: ");
        new Thread(()->handleInput()).start();
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

    public void sendCommand(Command command){
        try{
            cch.sendCommand(command);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public synchronized void handleResponse(Response resp) {
        if(resp == null) return;
        switch(resp.getResponseType()) {
            case LOGIN_ERROR:
                playerNickname = null;

                clearConsole();
                printTitle();

                System.out.println("Nickname not valid.");
                if (resp.getStrParameter("newnickname") != null)
                    System.out.println("A valid nickname is: " + resp.getStrParameter("newnickname"));
                System.out.println("Choose another nickname: ");
                break;

            case LOGIN_CONFIRMED:
                clearConsole();
                printTitle();

                System.out.println("Nickname accepted.");
                playerNickname=resp.getStrParameter("nickname");
                System.out.println("Your nickname is: " + playerNickname);
                if(state.equals(ClientState.BEFORE_LOGIN))
                    state=ClientState.QUEUE;
                break;

            case ASK_PLAYERS_NUM:
                if(state==ClientState.BEFORE_LOGIN || state==ClientState.QUEUE || state==ClientState.ASK_PLAYERS_NUM){
                    if (resp.getStrParameter("result") == null) { //first request
                        state=ClientState.ASK_PLAYERS_NUM;
                        System.out.println("How many players?");

                    } else if (resp.getStrParameter("result").equals("success")) {  //accepted value
                        System.out.println("Okay, the game will start in a moment...");
                        state=ClientState.QUEUE;
                    } else { //not accepted value
                        clearConsole();
                        printTitle();

                        state=ClientState.ASK_PLAYERS_NUM;
                        System.out.println("Number not valid.");
                        System.out.println("Choose another answer: ");
                    }
                }

                break;

            case GAME_STARTED:
                if(state==ClientState.QUEUE || state==ClientState.BEFORE_LOGIN || state==ClientState.ASK_PLAYERS_NUM)
                    state=ClientState.MATCH_IDLE;

                Command command2 = new Command(CommandType.GAME_JOINED);
                sendCommand(command2);
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
                commonGoalsDesc[0]=resp.getStrParameter("commongoaldescription1");
                commonGoalsDesc[1]=resp.getStrParameter("commongoaldescription2");

                clearConsole();
                printGameScreen();

                break;

            case NEW_MEX_CHAT:
                chat=(ChatBean) resp.getObjParameter("chat") ;
                clearConsole();
                printGameScreen();
                break;

            case NEW_TURN:
                currPlayerId=resp.getIntParameter("CurrentPlayerId");

                if(playerTurnId==currPlayerId){
                    state=ClientState.SELECT_COLUMN;
                    phase="IT'S YOUR TURN \nSelect the column where you want to put the tiles: ";
                }else{
                    state=ClientState.MATCH_IDLE;
                    phase="Player "+nicknames.get(currPlayerId)+" is playing...";
                }
                clearConsole();
                printGameScreen();
                break;

            case SELECT_COLUMN_RESULT:
                if("success".equals(resp.getStrParameter("result"))){
                    state=ClientState.SELECT_FIRST_TILE;
                    phase="Now select the first tile you want to pick, writing row and column ('r c') desired using the indexing.";
                    System.out.println(phase);
                }else{
                    state=ClientState.SELECT_COLUMN;
                    phase="You chose a non-valid column, please select a number between 0 and 4: ";
                    System.out.println(phase);
                }
                break;

            case SELECT_TILE_RESULT:
                if("success".equals(resp.getStrParameter("result"))){
                    if(state==ClientState.SELECT_FIRST_TILE) {
                        state = ClientState.SELECT_SECOND_TILE;
                        phase="Now select the last tile you want to pick, it has to be on the same row or on the same column\n" +
                                "and with a max distance of 2. If you want to pick just one tile, select the same tile: ";
                        System.out.println(phase);
                    }else{
                        state=ClientState.PUT_IN_COLUMN;
                        buffer=(Tile[]) resp.getObjParameter("buffer");
                        phase="Choose a tile you want to pick from the buffer: ";
                        clearConsole();
                        printGameScreen();

                    }

                }else{
                    if(state==ClientState.SELECT_FIRST_TILE) {
                        state = ClientState.SELECT_FIRST_TILE;
                        phase="Invalid coordinates, select another tile: ";
                        System.out.println(phase);
                    }else{
                        state=ClientState.SELECT_SECOND_TILE;
                        phase="Invalid coordinates, select another tile: ";
                        System.out.println(phase);
                    }
                }
                break;

            case PUT_IN_COLUMN_RESULT:
                if("success".equals(resp.getStrParameter("result"))){
                    buffer=(Tile[]) resp.getObjParameter("buffer");
                    if(resp.getIntParameter("turnFinished")==0) {
                        state = ClientState.PUT_IN_COLUMN;
                        phase="Choose another tile you want to pick from the buffer: ";
                    }else{
                        state=ClientState.MATCH_IDLE;
                    }
                    clearConsole();
                    printGameScreen();
                }else{
                    state=ClientState.PUT_IN_COLUMN;
                    phase="You chose a non-valid position, please select a valid number between 0 and 2: ";
                    System.out.println(phase);
                }
                break;

            case UPDATE_BOARD:
                board=(BoardBean)resp.getObjParameter("board");
                clearConsole();
                printGameScreen();
                break;

            case UPDATE_PLAYER_SHELF:
                shelves.set(resp.getIntParameter("playerid"),(ShelfBean)resp.getObjParameter("shelf"));
                clearConsole();
                printGameScreen();
                break;

            case PLAYER_DISCONNECTED:
                connected.set(resp.getIntParameter("playerid"),false);
                System.out.println("The player "+nicknames.get(resp.getIntParameter("playerid"))+" has disconnected.");
                break;

            case PLAYER_RECONNECTED:
                connected.set(resp.getIntParameter("playerid"),true);
                System.out.println("The player "+nicknames.get(resp.getIntParameter("playerid"))+" has reconnected.");
                break;

            case ONLY_ONE_CONNECTED:
                System.out.println("You are the only one connected... please wait for other players to reconnect");
                break;

            case ONLY_ONE_CONNECTED_TIMER:
                System.out.println("You are the only one connected... please wait "+resp.getIntParameter("timermilliseconds")/1000+" seconds.");
                break;

            case GAME_ENDED:
                clearConsole();
                List<Integer> points;
                System.out.println("GAME ENDED!!!");
                if(resp.getIntParameter("interrupted")==0){ //finished correctly
                    System.out.println("RESULTS: ");
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
                    for(int i=0; i<resPoints.size(); i++){
                        System.out.println(i +") " + resNick.get(i) + ": " + resPoints.get(i));
                    }
                }
                else{
                    System.out.println("You are the only one remained... YOU WIN!");
                }
                break;

            case COMMON_GOAL_WON:
                if(resp.getIntParameter("commongoalid")==0) {
                    commonGoalPoints1.set(resp.getIntParameter("playerid"), resp.getIntParameter("pointswon"));
                    commonGoalsRemainingPoint[0]=resp.getIntParameter("remainingpoints");
                }else{
                    commonGoalPoints2.set(resp.getIntParameter("playerid"), resp.getIntParameter("pointswon"));
                    commonGoalsRemainingPoint[0]=resp.getIntParameter("remainingpoints");
                }
                clearConsole();
                printGameScreen();
                break;

            case SHELF_COMPLETED:
                //
                break;
        }
    }


    /**
     * Method that handles the input sent from the user, using the ClientState class
     * (for every state the View expects a precise input)
     * @author Nicole Filippi
     */
    public void handleInput(){
        String input;
        String[] words;
        while(true){
            input = scan.nextLine();
            words=input.split(" ");
            String message = "";
            if("-m".equals(words[0])){
                Command command = new Command(CommandType.SEND_MEX_CHAT);
                for(int i=1; i<words.length; i++){
                    message=message+words[i]+" ";
                }
                command.setStrParameter("text", message);
                sendCommand(command);
            }
            else if("-pm".equals(words[0])){
                Command command = new Command(CommandType.SEND_MEX_CHAT);

                for(int i=2; i<words.length; i++){
                    message=message+words[i]+" ";
                }
                command.setStrParameter("text", message);
                command.setStrParameter("receiver", words[1]);
                sendCommand(command);
            }
            else{
                switch (state){
                    case BEFORE_LOGIN:
                        playerNickname = input;
                        Command command = new Command(CommandType.LOGIN);
                        command.setStrParameter("nickname", input);
                        sendCommand(command);
                        break;
                    case ASK_PLAYERS_NUM:
                        Command command2 = new Command(CommandType.PLAYERS_NUM);
                        try{
                            int num=Integer.parseInt(input);
                            command2.setIntParameter("num", num);
                            sendCommand(command2);
                            state=ClientState.QUEUE;
                        }catch(Exception e){
                            state=ClientState.ASK_PLAYERS_NUM;
                            System.out.println("Invalid number, choose a number between 2 and 4");
                        }
                        break;

                    case SELECT_COLUMN:
                        Command command3 = new Command(CommandType.SELECT_COLUMN);
                        try{
                            state=ClientState.MATCH_IDLE;
                            int num=Integer.parseInt(input);
                            command3.setIntParameter("value", num);
                            sendCommand(command3);
                        }catch(Exception e){
                            state=ClientState.SELECT_COLUMN;
                            System.out.println("Invalid number, choose a number between 0 and 4");
                        }
                        break;

                    case SELECT_FIRST_TILE:
                        Command command4 = new Command(CommandType.SELECT_TILE);
                        String[] numbers=input.split(" ");
                        try{
                            int row=Integer.parseInt(numbers[0]);
                            int col=Integer.parseInt(numbers[1]);
                            command4.setIntParameter("row", row);
                            command4.setIntParameter("col", col);
                            sendCommand(command4);
                        }catch(Exception e){
                            System.out.println("Invalid numbers, select the row and then the column using the shown indexes!");
                        }
                        break;

                    case SELECT_SECOND_TILE:
                        Command command5 = new Command(CommandType.SELECT_TILE);
                        String[] numbers1=input.split(" ");
                        try{
                            int row=Integer.parseInt(numbers1[0]);
                            int col=Integer.parseInt(numbers1[1]);
                            command5.setIntParameter("row", row);
                            command5.setIntParameter("col", col);
                            sendCommand(command5);
                        }catch(Exception e){
                            System.out.println("Invalid numbers, select the row and then the column using the shown index!");
                        }
                        break;

                    case PUT_IN_COLUMN:
                        Command command6 = new Command(CommandType.PUT_IN_COLUMN);
                        try{
                            int index=Integer.parseInt(input);
                            state=ClientState.MATCH_IDLE;
                            command6.setIntParameter("index", index);
                            sendCommand(command6);
                        }catch(Exception e){
                            state=ClientState.PUT_IN_COLUMN;
                            System.out.println("Invalid position number");
                        }
                        break;

                    default:
                        break;
                }
            }
        }
    }

    /**
     * Method that calls printAll
     * @author Nicole Filippi
     */
    public void printGameScreen(){
        printAll(board, chat, shelves, nicknames, playerNickname, commonGoalPoints1, commonGoalPoints2, commonGoalsDesc[0], commonGoalsDesc[1], Arrays.stream(buffer).toList(), personalGoal, commonGoalsRemainingPoint[0], commonGoalsRemainingPoint[1], currPlayerId, connected, phase);
    }


    /**
     * Method that prints a given board
     * @author Nicole Filippi
     * @param board is the board that has to be printed
     * @deprecated replaced by the method printAll()
     */
    private void printBoard(BoardBean board) {
        System.out.println("╔═══╦═══╦═══╦═══╦═══╦═══╦═══╦═══╦═══╦═══╗");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i == 0) {
                    if (j == 0)
                        System.out.print("║   ");
                    else
                        System.out.print("║ " + (j - 1) + " ");
                } else {
                    if (j == 0)
                        System.out.print("║ " + (i - 1) + " ");
                    else if (!board.getTiles()[i - 1][j - 1].isFree()) {
                        switch (board.getTiles()[i - 1][j - 1].getColor()) {
                            case GREEN:
                                System.out.print("║ G ");
                                break;
                            case BLUE:
                                System.out.print("║ B ");
                                break;
                            case CYAN:
                                System.out.print("║ C ");
                                break;
                            case PINK:
                                System.out.print("║ P ");
                                break;
                            case WHITE:
                                System.out.print("║ W ");
                                break;
                            case ORANGE:
                                System.out.print("║ O ");
                                break;
                        }
                    } else
                        System.out.print("║   ");
                }

            }
            System.out.println("║");
            if(i!=9)
                System.out.println("╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣");
        }
        System.out.println("╚═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╝");
    }

    /**
     * Method that prints a given shelf
     * @author Nicole Filippi
     * @param shelf is the shelf that has to be printed
     * @deprecated replaced by the method printAll()
     */
    private void printShelf(ShelfBean shelf){
        System.out.println("╔═══╦═══╦═══╦═══╦═══╗");
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                if (!shelf.getTiles()[i][j].isFree()) {
                        switch (shelf.getTiles()[i][j].getColor()) {
                            case GREEN:
                                System.out.print("║ G ");
                                break;
                            case BLUE:
                                System.out.print("║ B ");
                                break;
                            case CYAN:
                                System.out.print("║ C ");
                                break;
                            case PINK:
                                System.out.print("║ P ");
                                break;
                            case WHITE:
                                System.out.print("║ W ");
                                break;
                            case ORANGE:
                                System.out.print("║ O ");
                                break;
                        }
                    } else
                        System.out.print("║   ");
            }
            System.out.println("║");
            if(i!=5)
                System.out.println("╠═══╬═══╬═══╬═══╬═══╣");
        }
        System.out.println("╠═══╩═══╩═══╩═══╩═══╣");
        System.out.println("╩ 0   1   2   3   4 ╩");

    }

    /**
     * Method to display the Title
     * @author Fiorentini Riccardo
     */
    public void printTitle(){
        System.out.println((char)27 + "[33m" + (char)27 + "[40m" + "\n" +
                "\t\t\t\t███╗   ███╗██╗   ██╗███████╗██╗  ██╗███████╗██╗     ███████╗██╗███████╗\n" +
                "\t\t\t\t████╗ ████║╚██╗ ██╔╝██╔════╝██║  ██║██╔════╝██║     ██╔════╝██║██╔════╝\n" +
                "\t\t\t\t██╔████╔██║ ╚████╔╝ ███████╗███████║█████╗  ██║     █████╗  ██║█████╗  \n" +
                "\t\t\t\t██║╚██╔╝██║  ╚██╔╝  ╚════██║██╔══██║██╔══╝  ██║     ██╔══╝  ██║██╔══╝  \n" +
                "\t\t\t\t██║ ╚═╝ ██║   ██║   ███████║██║  ██║███████╗███████╗██║     ██║███████╗\n" +
                "\t\t\t\t╚═╝     ╚═╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚══════╝╚══════╝╚═╝     ╚═╝╚══════╝\n" +
                "\t\t\t\t                                                                       \n" + (char)27 + "[37m");
    }


    /**
     * Method to display the state of the match
     * @author Fiorentini Riccardo
     * @param board board of the game
     * @param chat chat of the game
     * @param shelves players' shelves list, it follows the order of the playerId
     * @param nicknames players' nickname list, it follows the order of the playerId
     * @param nickPlayer nickname of the client's player
     * @param commonGoalPoints1 list of points referred to each player for the first common goal
     * @param commonGoalPoints2 list of points referred to each player for the second common goal
     * @param CommonGoal1Descr description of the first common goal
     * @param CommonGoal2Descr description of the second common goal
     * @param buffer buffer of the client
     * @param personalGoal personal goal of the client
     * @param remainPointCommonGoal1 points remaining for the first common goal
     * @param remainPointCommonGoal2 points remaining for the second common goal
     * @param currPlayerId the id of the current player
     * @param connected a boolean list players (true if currently connected)
     * @param phase a string that is printed at the end that describes the actual phase of the game
     */
    public void printAll(BoardBean board, ChatBean chat, List<ShelfBean> shelves, List<String> nicknames, String nickPlayer, List<Integer> commonGoalPoints1,
                         List<Integer> commonGoalPoints2, String CommonGoal1Descr, String CommonGoal2Descr, List<Tile> buffer, TileColor[][] personalGoal,
                         int remainPointCommonGoal1, int remainPointCommonGoal2, int currPlayerId, List<Boolean> connected, String phase){

        String cg11  = "";
        String cg12  = "";
        String cg13  = "";
        String cg21  = "";
        String cg22  = ""; //66 e 80
        String cg23  = "";

        int chars = 0;
        String [] tmp = CommonGoal1Descr.split(" ");
        int len = tmp.length;
        int c = 0;
        while(chars<66 && c < len){
            chars+=tmp[c].length() + 1;
            cg11 = cg11 + tmp[c] + " ";
            c++;
        }
        cg11 = cg11 + "\n";
        chars = 0;
        while(chars<80 && c < len){
            chars+=tmp[c].length() + 1;
            cg12 = cg12 + tmp[c] + " ";
            c++;
        }
        cg12 = cg12 + "\n";
        chars = 0;
        while(chars<80 && c < len){
            chars+=tmp[c].length() + 1;
            cg13 = cg13 + tmp[c] + " ";
            c++;
        }
        cg13 = cg13 + "\n";

        chars = 0;
        tmp = CommonGoal2Descr.split(" ");
        len = tmp.length;
        c = 0;
        while(chars<66 && c < len){
            chars+=tmp[c].length() + 1;
            cg21 = cg21 + tmp[c] + " ";
            c++;
        }
        cg21 = cg21 + "\n";
        chars = 0;
        while(chars<80 && c < len){
            chars+=tmp[c].length() + 1;
            cg22 = cg22 + tmp[c] + " ";
            c++;
        }
        cg22 = cg22 + "\n";
        chars = 0;
        while(chars<80 && c < len){
            chars+=tmp[c].length() + 1;
            cg23 = cg23 + tmp[c] + " ";
            c++;
        }
        cg23 = cg23 + "\n";



        String output = "╔═══╦═══╦═══╦═══╦═══╦═══╦═══╦═══╦═══╦═══╗\n";
        int sizeChat = chat.getSender().size();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i == 0) {
                    if (j == 0)
                        output = output + "║   ";
                    else
                        output = output + "║ " + (j - 1) + " ";
                } else {
                    if (j == 0)
                        output = output + "║ " + (i - 1) + " ";
                    else if (!board.getTiles()[i - 1][j - 1].isFree()) {
                        switch (board.getTiles()[i - 1][j - 1].getColor()) {
                            case GREEN:
                                output = output + "║" + (char)27 + "[42m" + (char)27 + "[30m" + " G " + (char)27 + "[40m" + (char)27 + "[37m";
                                break;
                            case BLUE:
                                output = output + "║" + (char)27 + "[44m" + (char)27 + "[30m" + " B " + (char)27 + "[40m" + (char)27 + "[37m";
                                break;
                            case CYAN:
                                output = output + "║" + (char)27 + "[46m" + (char)27 + "[30m" + " C "  + (char)27 + "[40m" + (char)27 + "[37m";
                                break;
                            case PINK:
                                output = output + "║" + (char)27 + "[45m" + (char)27 + "[30m" + " P "  + (char)27 + "[40m" + (char)27 + "[37m";
                                break;
                            case WHITE:
                                output = output + "║" + (char)27 + "[47m" + (char)27 + "[30m"  + " W "  + (char)27 + "[40m" + (char)27 + "[37m";
                                break;
                            case ORANGE:
                                output = output + "║" + (char)27 + "[41m" + (char)27 + "[30m" + " O "  + (char)27 + "[40m" + (char)27 + "[37m";
                                break;
                        }
                    } else
                        output = output + "║   ";
                }

            }
            if(i == 0){
                int com1 = 0;
                int com2 = 0;
                for(int k = 0; k < nicknames.size(); k++){
                    if(nicknames.get(k).equals(nickPlayer)){
                        com1 = commonGoalPoints1.get(k);
                        com2 = commonGoalPoints2.get(k);
                    }
                }
                output = output + "║" + "\tCommon Goal 1: " + com1 + "\t\t\t\t\t\t\t\tChat:\n";
                output = output + "╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣\tCommon Goal 2: " + com2;
                if(sizeChat > 9){
                    output = output + "\t\t\t\t\t\t\t\t<@" + chat.getSender().get(sizeChat-10) + "> " + chat.getText().get(sizeChat-10) + "\n";
                }else if(sizeChat!=0){
                    output = output + "\t\t\t\t\t\t\t\t<@" + chat.getSender().get(0) + "> " + chat.getText().get(0) + "\n";
                }else{
                    output = output + "\n";
                }
            }else if(i == 1){
                int tmpId = 0;
                for(int k = 0; k<nicknames.size(); k++){
                    if(nicknames.get(k).equals(nickPlayer)){
                        tmpId = k;
                    }
                }
                if(tmpId==currPlayerId){
                    output = output  + "║" + (char)27 + "[33m" + "\t@" + nickPlayer + (char)27 + "[37m" + "\n";
                }else{
                    output = output + "║" + "\t@" + nickPlayer + "\n";
                }
                output = output + "╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣\t\t\t╔═══╦═══╦═══╗";
                if(sizeChat > 9){
                    output = output + "\t\t\t\t\t\t\t<@" + chat.getSender().get(sizeChat-9) + "> " + chat.getText().get(sizeChat-9) + "\n";
                }else if(sizeChat>=2){
                    output = output + "\t\t\t\t\t\t\t<@" + chat.getSender().get(1) + "> " + chat.getText().get(1) + "\n";
                }else{
                    output = output + "\n";
                }
            }else if(i == 2){
                output = output + "║" + "\tBuffer:\t";
                switch (buffer.get(0).getColor()) {
                    case GREEN:
                        output = output + "║" + (char)27 + "[42m" + (char)27 + "[30m" + " G " + (char)27 + "[40m" + (char)27 + "[37m";
                        break;
                    case BLUE:
                        output = output + "║" + (char)27 + "[44m" + (char)27 + "[30m" + " B " + (char)27 + "[40m" + (char)27 + "[37m";
                        break;
                    case CYAN:
                        output = output + "║" + (char)27 + "[46m" + (char)27 + "[30m" + " C "  + (char)27 + "[40m" + (char)27 + "[37m";
                        break;
                    case PINK:
                        output = output + "║" + (char)27 + "[45m" + (char)27 + "[30m" + " P "  + (char)27 + "[40m" + (char)27 + "[37m";
                        break;
                    case WHITE:
                        output = output + "║" + (char)27 + "[47m" + (char)27 + "[30m"  + " W "  + (char)27 + "[40m" + (char)27 + "[37m";
                        break;
                    case ORANGE:
                        output = output + "║" + (char)27 + "[41m" + (char)27 + "[30m" + " O "  + (char)27 + "[40m" + (char)27 + "[37m";
                        break;
                    case EMPTY:
                        output = output + "║   ";
                        break;
                }
                switch (buffer.get(1).getColor()) {
                    case GREEN:
                        output = output + "║" + (char)27 + "[42m" + (char)27 + "[30m" + " G " + (char)27 + "[40m" + (char)27 + "[37m";
                        break;
                    case BLUE:
                        output = output + "║" + (char)27 + "[44m" + (char)27 + "[30m" + " B " + (char)27 + "[40m" + (char)27 + "[37m";
                        break;
                    case CYAN:
                        output = output + "║" + (char)27 + "[46m" + (char)27 + "[30m" + " C "  + (char)27 + "[40m" + (char)27 + "[37m";
                        break;
                    case PINK:
                        output = output + "║" + (char)27 + "[45m" + (char)27 + "[30m" + " P "  + (char)27 + "[40m" + (char)27 + "[37m";
                        break;
                    case WHITE:
                        output = output + "║" + (char)27 + "[47m" + (char)27 + "[30m"  + " W "  + (char)27 + "[40m" + (char)27 + "[37m";
                        break;
                    case ORANGE:
                        output = output + "║" + (char)27 + "[41m" + (char)27 + "[30m" + " O "  + (char)27 + "[40m" + (char)27 + "[37m";
                        break;
                    case EMPTY:
                        output = output + "║   ";
                        break;
                }
                switch (buffer.get(2).getColor()) {
                    case GREEN:
                        output = output + "║" + (char)27 + "[42m" + (char)27 + "[30m" + " G " + (char)27 + "[40m" + (char)27 + "[37m";
                        break;
                    case BLUE:
                        output = output + "║" + (char)27 + "[44m" + (char)27 + "[30m" + " B " + (char)27 + "[40m" + (char)27 + "[37m";
                        break;
                    case CYAN:
                        output = output + "║" + (char)27 + "[46m" + (char)27 + "[30m" + " C "  + (char)27 + "[40m" + (char)27 + "[37m";
                        break;
                    case PINK:
                        output = output + "║" + (char)27 + "[45m" + (char)27 + "[30m" + " P "  + (char)27 + "[40m" + (char)27 + "[37m";
                        break;
                    case WHITE:
                        output = output + "║" + (char)27 + "[47m" + (char)27 + "[30m"  + " W "  + (char)27 + "[40m" + (char)27 + "[37m";
                        break;
                    case ORANGE:
                        output = output + "║" + (char)27 + "[41m" + (char)27 + "[30m" + " O "  + (char)27 + "[40m" + (char)27 + "[37m";
                        break;
                    case EMPTY:
                        output = output + "║   ";
                        break;
                }
                output = output + "║   Personal Goal:\n";
                output = output + "╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣\t\t\t╚═══╩═══╩═══╝";
                if(sizeChat > 9){
                    output = output + "\t\t\t\t\t\t\t<@" + chat.getSender().get(sizeChat-8) + "> " + chat.getText().get(sizeChat-8) + "\n";
                }else if(sizeChat>=3){
                    output = output + "\t\t\t\t\t\t\t<@" + chat.getSender().get(2) + "> " + chat.getText().get(2) + "\n";
                }else{
                    output = output + "\n";
                }
            }else if(i == 3){
                output = output + "║\t╔═══╦═══╦═══╦═══╦═══╗\t╔═══╦═══╦═══╦═══╦═══╗\n╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣\t";
                int id = 0;
                for(int k = 0; k < nicknames.size(); k++){
                    if(nicknames.get(k).equals(nickPlayer)){
                        id = k;
                    }
                }
                for(int k = 0; k<5; k++){
                    switch (shelves.get(id).getTiles()[0][k].getColor()) {
                        case GREEN:
                            output = output + "║" + (char)27 + "[42m" + (char)27 + "[30m" + " G " + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case BLUE:
                            output = output + "║" + (char)27 + "[44m" + (char)27 + "[30m" + " B " + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case CYAN:
                            output = output + "║" + (char)27 + "[46m" + (char)27 + "[30m" + " C "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case PINK:
                            output = output + "║" + (char)27 + "[45m" + (char)27 + "[30m" + " P "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case WHITE:
                            output = output + "║" + (char)27 + "[47m" + (char)27 + "[30m"  + " W "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case ORANGE:
                            output = output + "║" + (char)27 + "[41m" + (char)27 + "[30m" + " O "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case EMPTY:
                            output = output + "║   ";
                            break;
                    }
                }
                output = output + "║\t";
                for(int k = 0; k<5; k++){
                    switch (personalGoal[0][k]) {
                        case GREEN:
                            output = output + "║" + (char)27 + "[42m" + (char)27 + "[30m" + " G " + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case BLUE:
                            output = output + "║" + (char)27 + "[44m" + (char)27 + "[30m" + " B " + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case CYAN:
                            output = output + "║" + (char)27 + "[46m" + (char)27 + "[30m" + " C "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case PINK:
                            output = output + "║" + (char)27 + "[45m" + (char)27 + "[30m" + " P "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case WHITE:
                            output = output + "║" + (char)27 + "[47m" + (char)27 + "[30m"  + " W "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case ORANGE:
                            output = output + "║" + (char)27 + "[41m" + (char)27 + "[30m" + " O "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case EMPTY:
                            output = output + "║   ";
                            break;
                    }
                }
                output = output + "║";
                if(sizeChat > 9){
                    output = output + "\t<@" + chat.getSender().get(sizeChat-7) + "> " + chat.getText().get(sizeChat-7) + "\n";
                }else if(sizeChat>=4){
                    output = output + "\t<@" + chat.getSender().get(3) + "> " + chat.getText().get(3) + "\n";
                }else{
                    output = output + "\n";
                }
            }else if(i == 4){
                output = output + "║\t╠═══╬═══╬═══╬═══╬═══╣\t╠═══╬═══╬═══╬═══╬═══╣\n╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣\t";
                int id = 0;
                for(int k = 0; k < nicknames.size(); k++){
                    if(nicknames.get(k).equals(nickPlayer)){
                        id = k;
                    }
                }
                for(int k = 0; k<5; k++){
                    switch (shelves.get(id).getTiles()[1][k].getColor()) {
                        case GREEN:
                            output = output + "║" + (char)27 + "[42m" + (char)27 + "[30m" + " G " + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case BLUE:
                            output = output + "║" + (char)27 + "[44m" + (char)27 + "[30m" + " B " + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case CYAN:
                            output = output + "║" + (char)27 + "[46m" + (char)27 + "[30m" + " C "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case PINK:
                            output = output + "║" + (char)27 + "[45m" + (char)27 + "[30m" + " P "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case WHITE:
                            output = output + "║" + (char)27 + "[47m" + (char)27 + "[30m"  + " W "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case ORANGE:
                            output = output + "║" + (char)27 + "[41m" + (char)27 + "[30m" + " O "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case EMPTY:
                            output = output + "║   ";
                            break;
                    }
                }
                output = output + "║\t";
                for(int k = 0; k<5; k++){
                    switch (personalGoal[1][k]) {
                        case GREEN:
                            output = output + "║" + (char)27 + "[42m" + (char)27 + "[30m" + " G " + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case BLUE:
                            output = output + "║" + (char)27 + "[44m" + (char)27 + "[30m" + " B " + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case CYAN:
                            output = output + "║" + (char)27 + "[46m" + (char)27 + "[30m" + " C "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case PINK:
                            output = output + "║" + (char)27 + "[45m" + (char)27 + "[30m" + " P "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case WHITE:
                            output = output + "║" + (char)27 + "[47m" + (char)27 + "[30m"  + " W "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case ORANGE:
                            output = output + "║" + (char)27 + "[41m" + (char)27 + "[30m" + " O "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case EMPTY:
                            output = output + "║   ";
                            break;
                    }
                }
                output = output + "║";
                if(sizeChat > 9){
                    output = output + "\t<@" + chat.getSender().get(sizeChat-6) + "> " + chat.getText().get(sizeChat-6) + "\n";
                }else if(sizeChat>=5){
                    output = output + "\t<@" + chat.getSender().get(4)+ "> " + chat.getText().get(4) + "\n";
                }else{
                    output = output + "\n";
                }
            }else if(i == 5){
                output = output + "║\t╠═══╬═══╬═══╬═══╬═══╣\t╠═══╬═══╬═══╬═══╬═══╣\n╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣\t";
                int id = 0;
                for(int k = 0; k < nicknames.size(); k++){
                    if(nicknames.get(k).equals(nickPlayer)){
                        id = k;
                    }
                }
                for(int k = 0; k<5; k++){
                    switch (shelves.get(id).getTiles()[2][k].getColor()) {
                        case GREEN:
                            output = output + "║" + (char)27 + "[42m" + (char)27 + "[30m" + " G " + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case BLUE:
                            output = output + "║" + (char)27 + "[44m" + (char)27 + "[30m" + " B " + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case CYAN:
                            output = output + "║" + (char)27 + "[46m" + (char)27 + "[30m" + " C "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case PINK:
                            output = output + "║" + (char)27 + "[45m" + (char)27 + "[30m" + " P "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case WHITE:
                            output = output + "║" + (char)27 + "[47m" + (char)27 + "[30m"  + " W "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case ORANGE:
                            output = output + "║" + (char)27 + "[41m" + (char)27 + "[30m" + " O "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case EMPTY:
                            output = output + "║   ";
                            break;
                    }
                }
                output = output + "║\t";
                for(int k = 0; k<5; k++){
                    switch (personalGoal[2][k]) {
                        case GREEN:
                            output = output + "║" + (char)27 + "[42m" + (char)27 + "[30m" + " G " + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case BLUE:
                            output = output + "║" + (char)27 + "[44m" + (char)27 + "[30m" + " B " + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case CYAN:
                            output = output + "║" + (char)27 + "[46m" + (char)27 + "[30m" + " C "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case PINK:
                            output = output + "║" + (char)27 + "[45m" + (char)27 + "[30m" + " P "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case WHITE:
                            output = output + "║" + (char)27 + "[47m" + (char)27 + "[30m"  + " W "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case ORANGE:
                            output = output + "║" + (char)27 + "[41m" + (char)27 + "[30m" + " O "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case EMPTY:
                            output = output + "║   ";
                            break;
                    }
                }
                output = output + "║";
                if(sizeChat > 9){
                    output = output + "\t<@" +  chat.getSender().get(sizeChat-5) + "> " + chat.getText().get(sizeChat-5) + "\n";
                }else if(sizeChat>=6){
                    output = output + "\t<@" +  chat.getSender().get(5) + "> " + chat.getText().get(5) + "\n";
                }else{
                    output = output + "\n";
                }
            }else if(i == 6){
                output = output + "║\t╠═══╬═══╬═══╬═══╬═══╣\t╠═══╬═══╬═══╬═══╬═══╣\n╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣\t";
                int id = 0;
                for(int k = 0; k < nicknames.size(); k++){
                    if(nicknames.get(k).equals(nickPlayer)){
                        id = k;
                    }
                }
                for(int k = 0; k<5; k++){
                    switch (shelves.get(id).getTiles()[3][k].getColor()) {
                        case GREEN:
                            output = output + "║" + (char)27 + "[42m" + (char)27 + "[30m" + " G " + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case BLUE:
                            output = output + "║" + (char)27 + "[44m" + (char)27 + "[30m" + " B " + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case CYAN:
                            output = output + "║" + (char)27 + "[46m" + (char)27 + "[30m" + " C "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case PINK:
                            output = output + "║" + (char)27 + "[45m" + (char)27 + "[30m" + " P "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case WHITE:
                            output = output + "║" + (char)27 + "[47m" + (char)27 + "[30m"  + " W "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case ORANGE:
                            output = output + "║" + (char)27 + "[41m" + (char)27 + "[30m" + " O "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case EMPTY:
                            output = output + "║   ";
                            break;
                    }
                }
                output = output + "║\t";
                for(int k = 0; k<5; k++){
                    switch (personalGoal[3][k]) {
                        case GREEN:
                            output = output + "║" + (char)27 + "[42m" + (char)27 + "[30m" + " G " + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case BLUE:
                            output = output + "║" + (char)27 + "[44m" + (char)27 + "[30m" + " B " + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case CYAN:
                            output = output + "║" + (char)27 + "[46m" + (char)27 + "[30m" + " C "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case PINK:
                            output = output + "║" + (char)27 + "[45m" + (char)27 + "[30m" + " P "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case WHITE:
                            output = output + "║" + (char)27 + "[47m" + (char)27 + "[30m"  + " W "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case ORANGE:
                            output = output + "║" + (char)27 + "[41m" + (char)27 + "[30m" + " O "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case EMPTY:
                            output = output + "║   ";
                            break;
                    }
                }
                output = output + "║";
                if(sizeChat > 9){
                    output = output + "\t<@" + chat.getSender().get(sizeChat-4) + "> " + chat.getText().get(sizeChat-4) + "\n";
                }else if(sizeChat>=7){
                    output = output + "\t<@" + chat.getSender().get(6) + "> " + chat.getText().get(6) + "\n";
                }else{
                    output = output + "\n";
                }
            }else if(i == 7){
                output = output + "║\t╠═══╬═══╬═══╬═══╬═══╣\t╠═══╬═══╬═══╬═══╬═══╣\n╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣\t";
                int id = 0;
                for(int k = 0; k < nicknames.size(); k++){
                    if(nicknames.get(k).equals(nickPlayer)){
                        id = k;
                    }
                }
                for(int k = 0; k<5; k++){
                    switch (shelves.get(id).getTiles()[4][k].getColor()) {
                        case GREEN:
                            output = output + "║" + (char)27 + "[42m" + (char)27 + "[30m" + " G " + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case BLUE:
                            output = output + "║" + (char)27 + "[44m" + (char)27 + "[30m" + " B " + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case CYAN:
                            output = output + "║" + (char)27 + "[46m" + (char)27 + "[30m" + " C "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case PINK:
                            output = output + "║" + (char)27 + "[45m" + (char)27 + "[30m" + " P "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case WHITE:
                            output = output + "║" + (char)27 + "[47m" + (char)27 + "[30m"  + " W "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case ORANGE:
                            output = output + "║" + (char)27 + "[41m" + (char)27 + "[30m" + " O "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case EMPTY:
                            output = output + "║   ";
                            break;
                    }
                }
                output = output + "║\t";
                for(int k = 0; k<5; k++){
                    switch (personalGoal[4][k]) {
                        case GREEN:
                            output = output + "║" + (char)27 + "[42m" + (char)27 + "[30m" + " G " + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case BLUE:
                            output = output + "║" + (char)27 + "[44m" + (char)27 + "[30m" + " B " + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case CYAN:
                            output = output + "║" + (char)27 + "[46m" + (char)27 + "[30m" + " C "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case PINK:
                            output = output + "║" + (char)27 + "[45m" + (char)27 + "[30m" + " P "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case WHITE:
                            output = output + "║" + (char)27 + "[47m" + (char)27 + "[30m"  + " W "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case ORANGE:
                            output = output + "║" + (char)27 + "[41m" + (char)27 + "[30m" + " O "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case EMPTY:
                            output = output + "║   ";
                            break;
                    }
                }
                output = output + "║";
                if(sizeChat > 9){
                    output = output + "\t<@" + chat.getSender().get(sizeChat-3) + "> " + chat.getText().get(sizeChat-3) + "\n";
                }else if(sizeChat>=8){
                    output = output + "\t<@" + chat.getSender().get(7) + "> " + chat.getText().get(7) + "\n";
                }else{
                    output = output + "\n";
                }
            }else if(i == 8){
                output = output + "║\t╠═══╬═══╬═══╬═══╬═══╣\t╠═══╬═══╬═══╬═══╬═══╣\n╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣\t";
                int id = 0;
                for(int k = 0; k < nicknames.size(); k++){
                    if(nicknames.get(k).equals(nickPlayer)){
                        id = k;
                    }
                }
                for(int k = 0; k<5; k++){
                    switch (shelves.get(id).getTiles()[5][k].getColor()) {
                        case GREEN:
                            output = output + "║" + (char)27 + "[42m" + (char)27 + "[30m" + " G " + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case BLUE:
                            output = output + "║" + (char)27 + "[44m" + (char)27 + "[30m" + " B " + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case CYAN:
                            output = output + "║" + (char)27 + "[46m" + (char)27 + "[30m" + " C "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case PINK:
                            output = output + "║" + (char)27 + "[45m" + (char)27 + "[30m" + " P "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case WHITE:
                            output = output + "║" + (char)27 + "[47m" + (char)27 + "[30m"  + " W "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case ORANGE:
                            output = output + "║" + (char)27 + "[41m" + (char)27 + "[30m" + " O "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case EMPTY:
                            output = output + "║   ";
                            break;
                    }
                }
                output = output + "║\t";
                for(int k = 0; k<5; k++){
                    switch (personalGoal[5][k]) {
                        case GREEN:
                            output = output + "║" + (char)27 + "[42m" + (char)27 + "[30m" + " G " + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case BLUE:
                            output = output + "║" + (char)27 + "[44m" + (char)27 + "[30m" + " B " + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case CYAN:
                            output = output + "║" + (char)27 + "[46m" + (char)27 + "[30m" + " C "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case PINK:
                            output = output + "║" + (char)27 + "[45m" + (char)27 + "[30m" + " P "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case WHITE:
                            output = output + "║" + (char)27 + "[47m" + (char)27 + "[30m"  + " W "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case ORANGE:
                            output = output + "║" + (char)27 + "[41m" + (char)27 + "[30m" + " O "  + (char)27 + "[40m" + (char)27 + "[37m";
                            break;
                        case EMPTY:
                            output = output + "║   ";
                            break;
                    }
                }
                output = output + "║";
                if(sizeChat > 9){
                    output = output + "\t<@" + chat.getSender().get(sizeChat-2) + "> " + chat.getText().get(sizeChat-2) + "\n";
                }else if(sizeChat == 9){
                    output = output + "\t<@" + chat.getSender().get(8) + "> " + chat.getText().get(8) + "\n";
                }else{
                    output = output + "\n";
                }
            }else if(i == 9){
                output = output + "║\t╠═══╩═══╩═══╩═══╩═══╣\t╠═══╩═══╩═══╩═══╩═══╣\n╚═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╝\t╩ 0   1   2   3   4 ╩\t╩ 0   1   2   3   4 ╩\n\n";
            }
        }

        int numPlay = nicknames.size();
        if(numPlay == 4){
            output = output + "╔═══╦═══╦═══╦═══╦═══╗\t╔═══╦═══╦═══╦═══╦═══╗\t╔═══╦═══╦═══╦═══╦═══╗\n";
        } else if (numPlay == 3) {
            output = output + "╔═══╦═══╦═══╦═══╦═══╗\t╔═══╦═══╦═══╦═══╦═══╗\n";
        } else {
            output = output + "╔═══╦═══╦═══╦═══╦═══╗\n";
        }

        for(int i = 0; i < 6; i++){
            for(int k = 0; k < numPlay; k++){
                if(!nicknames.get(k).equals(nickPlayer)) {
                    for (int j = 0; j < 5; j++) {
                        switch (shelves.get(k).getTiles()[i][j].getColor()) {
                            case GREEN:
                                output = output + "║" + (char)27 + "[42m" + (char)27 + "[30m" + " G " + (char)27 + "[40m" + (char)27 + "[37m";
                                break;
                            case BLUE:
                                output = output + "║" + (char)27 + "[44m" + (char)27 + "[30m" + " B " + (char)27 + "[40m" + (char)27 + "[37m";
                                break;
                            case CYAN:
                                output = output + "║" + (char)27 + "[46m" + (char)27 + "[30m" + " C "  + (char)27 + "[40m" + (char)27 + "[37m";
                                break;
                            case PINK:
                                output = output + "║" + (char)27 + "[45m" + (char)27 + "[30m" + " P "  + (char)27 + "[40m" + (char)27 + "[37m";
                                break;
                            case WHITE:
                                output = output + "║" + (char)27 + "[47m" + (char)27 + "[30m"  + " W "  + (char)27 + "[40m" + (char)27 + "[37m";
                                break;
                            case ORANGE:
                                output = output + "║" + (char)27 + "[41m" + (char)27 + "[30m" + " O "  + (char)27 + "[40m" + (char)27 + "[37m";
                                break;
                            case EMPTY:
                                output = output + "║   ";
                                break;
                        }
                    }
                    output = output + "║\t";
                }
            }

            if(i==0){
                if(numPlay == 4){
                    output = output + "\n╠═══╬═══╬═══╬═══╬═══╣\t╠═══╬═══╬═══╬═══╬═══╣\t╠═══╬═══╬═══╬═══╬═══╣\tCommon Goal 1: " + cg11;
                } else if (numPlay == 3) {
                    output = output + "\n╠═══╬═══╬═══╬═══╬═══╣\t╠═══╬═══╬═══╬═══╬═══╣\tCommon Goal 1: " + cg11;
                } else {
                    output = output + "\n╠═══╬═══╬═══╬═══╬═══╣\tCommon Goal 1: " + cg11;
                }
            } else if (i == 1) {
                output = output + cg12;
                if(numPlay == 4){
                    output = output + "╠═══╬═══╬═══╬═══╬═══╣\t╠═══╬═══╬═══╬═══╬═══╣\t╠═══╬═══╬═══╬═══╬═══╣\t" + cg13;
                } else if (numPlay == 3) {
                    output = output + "╠═══╬═══╬═══╬═══╬═══╣\t╠═══╬═══╬═══╬═══╬═══╣\t" + cg13;
                } else {
                    output = output + "╠═══╬═══╬═══╬═══╬═══╣\t" + cg13;
                }
            }else if (i == 2) {
                if(numPlay == 4){
                    output = output + "\n╠═══╬═══╬═══╬═══╬═══╣\t╠═══╬═══╬═══╬═══╬═══╣\t╠═══╬═══╬═══╬═══╬═══╣\t";
                } else if (numPlay == 3) {
                    output = output + "\n╠═══╬═══╬═══╬═══╬═══╣\t╠═══╬═══╬═══╬═══╬═══╣\t";
                } else {
                    output = output + "\n╠═══╬═══╬═══╬═══╬═══╣\t";
                }
                output = output + "Common Goal 2: " + cg21;
            }else if(i == 3){
                output = output + cg22;
                if(numPlay == 4){
                    output = output + "╠═══╬═══╬═══╬═══╬═══╣\t╠═══╬═══╬═══╬═══╬═══╣\t╠═══╬═══╬═══╬═══╬═══╣\t" + cg23;
                } else if (numPlay == 3) {
                    output = output + "╠═══╬═══╬═══╬═══╬═══╣\t╠═══╬═══╬═══╬═══╬═══╣\t" + cg23;
                } else {
                    output = output + "╠═══╬═══╬═══╬═══╬═══╣\t" + cg23;
                }
            }else if(i == 4){
                if(numPlay == 4){
                    output = output + "\n╠═══╬═══╬═══╬═══╬═══╣\t╠═══╬═══╬═══╬═══╬═══╣\t╠═══╬═══╬═══╬═══╬═══╣\tRemaining Points 1: " + remainPointCommonGoal1 + "\n";
                } else if (numPlay == 3) {
                    output = output + "\n╠═══╬═══╬═══╬═══╬═══╣\t╠═══╬═══╬═══╬═══╬═══╣\tRemaining Points 1: " + remainPointCommonGoal1 + "\n";
                } else {
                    output = output + "\n╠═══╬═══╬═══╬═══╬═══╣\tRemaining Points 1: " + remainPointCommonGoal1 + "\n";
                }
            } else if(i == 5){
                if(numPlay == 4){
                    output = output + "\n╠═══╩═══╩═══╩═══╩═══╣\t╠═══╩═══╩═══╩═══╩═══╣\t╠═══╩═══╩═══╩═══╩═══╣\tRemaining Points 2: " + remainPointCommonGoal2 + "\n";
                    output = output + "╩ 0   1   2   3   4 ╩\t╩ 0   1   2   3   4 ╩\t╩ 0   1   2   3   4 ╩\n";
                } else if (numPlay == 3) {
                    output = output + "\n╠═══╩═══╩═══╩═══╩═══╣\t╠═══╩═══╩═══╩═══╩═══╣\tRemaining Points 2: " + remainPointCommonGoal2 + "\n";
                    output = output + "╩ 0   1   2   3   4 ╩\t╩ 0   1   2   3   4 ╩\n";
                } else {
                    output = output + "\n╠═══╩═══╩═══╩═══╩═══╣\tRemaining Points 2: " + remainPointCommonGoal2 + "\n";
                    output = output + "╩ 0   1   2   3   4 ╩\n";
                }
            }
        }

        if(!nicknames.get(0).equals(nickPlayer)){
            output = output + "  Common Goal 1: " + commonGoalPoints1.get(0) + "      ";
        }
        if(!nicknames.get(1).equals(nickPlayer)){
            output = output + "  Common Goal 1: " + commonGoalPoints1.get(1) + "      ";
        }
        if(numPlay == 4){
            if(!nicknames.get(2).equals(nickPlayer)){
                output = output + "  Common Goal 1: " + commonGoalPoints1.get(2) + "      ";
            }
            if(!nicknames.get(3).equals(nickPlayer)){
                output = output + "  Common Goal 1: " + commonGoalPoints1.get(3) + "      ";
            }
        }else if(numPlay == 3){
            if(!nicknames.get(2).equals(nickPlayer)){
                output = output + "  Common Goal 1: " + commonGoalPoints1.get(2) + "      ";
            }
        }
        output = output + "\n";

        if(!nicknames.get(0).equals(nickPlayer)){
            output = output + "  Common Goal 2: " + commonGoalPoints2.get(0) + "      ";
        }
        if(!nicknames.get(1).equals(nickPlayer)){
            output = output + "  Common Goal 2: " + commonGoalPoints2.get(1) + "      ";
        }
        if(numPlay == 4){
            if(!nicknames.get(2).equals(nickPlayer)){
                output = output + "  Common Goal 2: " + commonGoalPoints2.get(2) + "      ";
            }
            if(!nicknames.get(3).equals(nickPlayer)){
                output = output + "  Common Goal 2: " + commonGoalPoints2.get(3) + "      ";
            }
        }else if(numPlay == 3){
            if(!nicknames.get(2).equals(nickPlayer)){
                output = output + "  Common Goal 2: " + commonGoalPoints2.get(2) + "      ";
            }
        }
        output = output + "\n";
        if(!nicknames.get(0).equals(nickPlayer)){
            if(connected.get(0) && currPlayerId!=0){
                output = output + "  @" + nicknames.get(0);
            }else if(connected.get(0) && currPlayerId==0){
                output = output + "  @" + (char)27 + "[33m" + nicknames.get(0) + (char)27 + "[37m";
            }else{
                output = output + "  @" + (char)27 + "[31m" + nicknames.get(0) + (char)27 + "[37m";
            }
            for(int l = 0; l<21 - nicknames.get(0).length(); l++){
                output = output + " ";
            }
        }
        if(!nicknames.get(1).equals(nickPlayer)){
            if(connected.get(1) && currPlayerId!=1){
                output = output + "  @" + nicknames.get(1);
            }else if(connected.get(1) && currPlayerId==1){
                output = output + "  @" + (char)27 + "[33m" + nicknames.get(1) + (char)27 + "[37m";
            }else{
                output = output + "  @" + (char)27 + "[31m" + nicknames.get(1) + (char)27 + "[37m";
            }            for(int l = 0; l<21 - nicknames.get(1).length(); l++){
                output = output + " ";
            }
        }
        if(numPlay == 4){
            if(!nicknames.get(2).equals(nickPlayer)){
                if(connected.get(2) && currPlayerId!=2){
                    output = output + "  @" + nicknames.get(2);
                }else if(connected.get(2) && currPlayerId==2){
                    output = output + "  @" + (char)27 + "[33m" + nicknames.get(2) + (char)27 + "[37m";
                }else{
                    output = output + "  @" + (char)27 + "[31m" + nicknames.get(2) + (char)27 + "[37m";
                }
                for(int l = 0; l< 21 - nicknames.get(2).length(); l++){
                    output = output + " ";
                }
            }
            if(!nicknames.get(3).equals(nickPlayer)){
                if(connected.get(3) && currPlayerId!=3){
                    output = output + "  @" + nicknames.get(3);
                }else if(connected.get(3) && currPlayerId==3){
                    output = output + "  @" + (char)27 + "[33m" + nicknames.get(3) + (char)27 + "[37m";
                }else{
                    output = output + "  @" + (char)27 + "[31m" + nicknames.get(3) + (char)27 + "[37m";
                }
                for(int l = 0; l<nicknames.get(3).length()-22; l++){
                    output = output + " ";
                }
            }
        }else if(numPlay == 3){
            if(!nicknames.get(2).equals(nickPlayer)){
                if(connected.get(2) && currPlayerId!=2){
                    output = output + "  @" + nicknames.get(2);
                }else if(connected.get(2) && currPlayerId==2){
                    output = output + "  @" + (char)27 + "[33m" + nicknames.get(2) + (char)27 + "[37m";
                }else{
                    output = output + "  @" + (char)27 + "[31m" + nicknames.get(2) + (char)27 + "[37m";
                }
                for(int l = 0; l< 21 - nicknames.get(2).length(); l++){
                    output = output + " ";
                }                for(int l = 0; l< 21 - nicknames.get(2).length(); l++){
                    output = output + " ";
                }
            }
        }
        output = output + "\n";
        if(phase!=null){
            output = output + phase;
        }
        System.out.print(output);

    }

    public void clearConsole(){
        //System.out.print((char)27 + "[{9};{0}H");
        //escape command doesn't work
        for(int i=0;i<100;i++) System.out.println("\n");
    }

}
