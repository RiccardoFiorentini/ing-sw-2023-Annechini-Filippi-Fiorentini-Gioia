package main.java.it.polimi.ingsw.Client;

import main.java.it.polimi.ingsw.Connection.ClientConnectionHandler;
import main.java.it.polimi.ingsw.Controller.Command;
import main.java.it.polimi.ingsw.Controller.CommandType;
import main.java.it.polimi.ingsw.Controller.Response;
import main.java.it.polimi.ingsw.Controller.ResponseType;
import main.java.it.polimi.ingsw.Model.*;
import main.java.it.polimi.ingsw.ModelExceptions.FullColumnException;
import main.java.it.polimi.ingsw.ModelExceptions.NotToRefillException;

import java.io.IOException;
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
        new TUI(null).printBoard(board);
        Shelf shelf = new Shelf();
        shelf.putTile (Tile.CYAN2, 0);
        shelf.putTile (Tile.WHITE1, 0);
        shelf.putTile (Tile.ORANGE1, 0);
        shelf.putTile (Tile.CYAN2, 1);
        new TUI(null).printShelf(shelf);

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

}
