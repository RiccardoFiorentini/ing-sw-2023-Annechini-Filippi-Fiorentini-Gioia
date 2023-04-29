package main.java.it.polimi.ingsw.Client;

import main.java.it.polimi.ingsw.Connection.ClientConnectionHandler;
import main.java.it.polimi.ingsw.Controller.Command;
import main.java.it.polimi.ingsw.Controller.CommandType;
import main.java.it.polimi.ingsw.Controller.Response;
import main.java.it.polimi.ingsw.Model.*;
import main.java.it.polimi.ingsw.ModelExceptions.FullColumnException;
import main.java.it.polimi.ingsw.ModelExceptions.IncorrectMessageException;
import main.java.it.polimi.ingsw.ModelExceptions.NotToRefillException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TUI extends View{
    private ClientConnectionHandler cch;
    private Scanner scan = new Scanner(System.in);
    private ClientState state;
    public TUI(ClientConnectionHandler cch) {
        super(cch);
    }

    public static void main(String[] args) throws NotToRefillException, IOException, FullColumnException {
        Board board = new Board(3);
        Shelf shelf = new Shelf();
        shelf.putTile (Tile.CYAN2, 0);
        shelf.putTile (Tile.WHITE1, 0);
        shelf.putTile (Tile.ORANGE1, 0);
        shelf.putTile (Tile.CYAN2, 1);
        Shelf shelf1 = new Shelf();
        shelf1.putTile (Tile.CYAN2, 0);
        shelf1.putTile (Tile.CYAN2, 0);
        shelf1.putTile (Tile.CYAN2, 0);
        shelf1.putTile (Tile.CYAN2, 1);
        Shelf shelf2 = new Shelf();
        shelf2.putTile (Tile.ORANGE1, 0);
        shelf2.putTile (Tile.ORANGE1, 0);
        shelf2.putTile (Tile.ORANGE1, 0);
        shelf2.putTile (Tile.ORANGE1, 1);

        List<ShelfBean> sb = new ArrayList<>();
        sb.add(shelf.toBean());
        sb.add(shelf1.toBean());
        sb.add(shelf2.toBean());

        BoardBean bb = new BoardBean(board.getTilesRemaining(), board.getTiles());

        Player p1 = new Player("niki", null);
        Player p2 = new Player("ale", null);
        Player p3 = new Player("paki", null);
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);

        Chat chat = new Chat(players);
        try {
            chat.writeMessage(p1,"bella a tutti");
            chat.writeMessage(p2,"bella a tutti");
            chat.writeMessage(p3,"bella a tutti");
            chat.writeMessage(p1,"bella a tutti");
            chat.writeMessage(p3,"bella a tutti");
            chat.writeMessage(p3,"bella a tutti");
            chat.writeMessage(p3,"bella a tutti");
            chat.writeMessage(p3,"bella a tutti");
            chat.writeMessage(p3,"bella a tutti");
            chat.writeMessage(p3,"bella a tutti");
            chat.writeMessage(p3,"bella a tutti");
            chat.writeMessage(p3,"bella a tutti");
            chat.writeMessage(p3,"bella a tutti");
            chat.writeMessage(p3,"bella a tutti");

        } catch (IncorrectMessageException e) {
            throw new RuntimeException(e);
        }

        ChatBean cb = new ChatBean(chat.getMessages(), p1);

        List<String> nicknames = new ArrayList<>();
        nicknames.add(p1.getNickname());
        nicknames.add(p2.getNickname());
        nicknames.add(p3.getNickname());

        List<Integer> commonGoalPoints1 = new ArrayList<>();
        commonGoalPoints1.add(0);
        commonGoalPoints1.add(0);
        commonGoalPoints1.add(0);

        List<Integer> commonGoalPoints2 = new ArrayList<>();
        commonGoalPoints2.add(8);
        commonGoalPoints2.add(4);
        commonGoalPoints2.add(2);

        String CommonGoal1Descr = "pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo ";
        String CommonGoal2Descr = "pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo pippo ";

        List<Tile> buffer = new ArrayList<>();
        buffer.add(Tile.EMPTY);
        buffer.add(Tile.BLUE2);
        buffer.add(Tile.ORANGE2);

        TileColor[][] personalGoal = new TileColor[6][5];
        for(int i = 0; i<6; i++){
            for(int j = 0; j<5; j++){
                personalGoal[i][j] = TileColor.EMPTY;
            }
        }

        personalGoal[0][0] = TileColor.ORANGE;
        personalGoal[1][0] = TileColor.BLUE;

        new TUI(null).printTitle();
        TUI t = new TUI(null);
        t.printAll(bb, cb, sb, nicknames, p1.getNickname(), commonGoalPoints1, commonGoalPoints2, CommonGoal1Descr,
                CommonGoal2Descr, buffer, personalGoal, 4, 6);
        t.clearCosole();

        t.printAll(bb, cb, sb, nicknames, p1.getNickname(), commonGoalPoints1, commonGoalPoints2, CommonGoal1Descr,
                CommonGoal2Descr, buffer, personalGoal, 4, 6);


    }

    @Override
    public void handleResponse(Response resp) {
        if(resp == null) return;
        switch(resp.getResponseType()) {
            case LOGIN_ERROR:
                System.out.println("Nickname not valid.");
                if (resp.getStrParameter("newnickname") != null)
                    System.out.println("A valid nickname is: " + resp.getStrParameter("newnickname"));
                System.out.println("Choose another nickname: ");
                break;

            case LOGIN_CONFIRMED:
                System.out.println("Nickname accepted.");
                System.out.println("Your nickname is: " + resp.getStrParameter("nickname"));
                if(state.equals(ClientState.BEFORE_LOGIN))
                    state=ClientState.QUEUE;
                break;

            case ASK_PLAYERS_NUM:
                if (resp.getStrParameter("result") == null) { //first request
                    state=ClientState.ASK_PLAYERS_NUM;
                    System.out.println("How many players?");

                } else if (resp.getStrParameter("result").equals("success")) {  //accepted value
                    System.out.println("Okay, the game will start in a moment...");
                    state=ClientState.QUEUE;
                } else { //not accepted value
                    state=ClientState.ASK_PLAYERS_NUM;
                    System.out.println("Number not valid.");
                    System.out.println("Choose another answer: ");
                }
                break;

            case GAME_STARTED:
                Command command2 = new Command(CommandType.GAME_JOINED);
                if(state==ClientState.QUEUE || state==ClientState.BEFORE_LOGIN)
                    state=ClientState.MATCH_IDLE;
                sendCommand(command2);
                if(resp.getIntParameter("isstart")==1){ //game has just begun
                    System.out.println("The game has started.");
                    printBoard((Board)resp.getObjParameter("board"));
                    //stampa common goals con punti
                    //stampa personal goal
                    //stampa "è il tuo turno" per il primo player o "è il turno di nome" per gli altri

                }
                else{   //reconnected player
                    //stampa chat
                    //stampa board
                    //stampa common goals con punti rimanenti
                    //stampa shelves con nome players ed eventuali punti cg
                    //stampa shelf personale con personal goal ed eventuali punti common goals
                    //"è il turno di"
                }
                break;

            case NEW_MEX_CHAT:
                //stampo solo ultimo messaggio!!
                //CHAT -> nickname: messaggio
                break;

            case NEW_TURN:
                break;

            case SELECT_COLUMN_RESULT:
                break;

            case SELECT_TILE_RESULT:
                break;

            case UPDATE_BOARD:
                break;

            case UPDATE_PLAYER_SHELF:
                break;

            case PUT_IN_COLUMN_RESULT:
                break;

            case PLAYER_DISCONNECTED:
                break;

            case PLAYER_RECONNECTED:
                break;

            case ONLY_ONE_CONNECTED:
                break;

            case ONLY_ONE_CONNECTED_TIMER:
                break;

            case GAME_ENDED:
                break;

            case COMMON_GOAL_WON:
                break;



        }

    }

    @Override
    public void handleEvent(String event) {
        switch(event){
            case "start":
                state=ClientState.BEFORE_LOGIN;
                System.out.println("Choose your nickname: ");
                break;
        }
    }

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
                        Command command = new Command(CommandType.LOGIN);
                        command.setStrParameter("nickname", input);
                        sendCommand(command);
                        break;
                    case ASK_PLAYERS_NUM:
                        char c;
                        Command command2 = new Command(CommandType.PLAYERS_NUM);
                        int num;
                        try{
                            num=Integer.parseInt(input);
                            command2.setIntParameter("num", num);
                            sendCommand(command2);
                            state=ClientState.QUEUE;
                        }catch(Exception e){
                            System.out.println("Invalid number, choose a number between 2 and 4");
                        }
                        break;


                }
            }
        }
    }

    private void printBoard(Board board) {
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

    private void printShelf(Shelf shelf){
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
     */
    public void printAll(BoardBean board, ChatBean chat, List<ShelfBean> shelves, List<String> nicknames, String nickPlayer, List<Integer> commonGoalPoints1,
                         List<Integer> commonGoalPoints2, String CommonGoal1Descr, String CommonGoal2Descr, List<Tile> buffer, TileColor[][] personalGoal,
                         int remainPointCommonGoal1, int remainPointCommonGoal2){

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
                output = output + "║" + "\t@" + nickPlayer + "\n";
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
                if(nicknames.get(k) != nickPlayer) {
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
            output = output + "  @" + nicknames.get(0);
            for(int l = 0; l<21 - nicknames.get(0).length(); l++){
                output = output + " ";
            }
        }
        if(!nicknames.get(1).equals(nickPlayer)){
            output = output + "  @" + nicknames.get(1);
            for(int l = 0; l<21 - nicknames.get(1).length(); l++){
                output = output + " ";
            }
        }
        if(numPlay == 4){
            if(!nicknames.get(2).equals(nickPlayer)){
                output = output + "  @" + nicknames.get(2);
                for(int l = 0; l< 21 - nicknames.get(2).length(); l++){
                    output = output + " ";
                }
            }
            if(!nicknames.get(3).equals(nickPlayer)){
                output = output + "  @" + nicknames.get(3);
                for(int l = 0; l<nicknames.get(3).length()-22; l++){
                    output = output + " ";
                }
            }
        }else if(numPlay == 3){
            if(!nicknames.get(2).equals(nickPlayer)){
                output = output + "  @" + nicknames.get(2);
                for(int l = 0; l< 21 - nicknames.get(2).length(); l++){
                    output = output + " ";
                }
            }
        }
        output = output + "\n";

        System.out.print(output);

    }

    public void clearCosole(){
        //System.out.print((char)27 + "[{9};{0}H");
        //escape command doesn't work
    }

}
