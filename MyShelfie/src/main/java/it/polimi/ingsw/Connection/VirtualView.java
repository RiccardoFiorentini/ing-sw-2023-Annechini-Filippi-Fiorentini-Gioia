package main.java.it.polimi.ingsw.Connection;

import main.java.it.polimi.ingsw.Controller.Command;
import main.java.it.polimi.ingsw.Controller.GameController;
import main.java.it.polimi.ingsw.Controller.Response;
import main.java.it.polimi.ingsw.Controller.Server;
import main.java.it.polimi.ingsw.Model.Player;

import java.io.IOException;


public class VirtualView {
    private ServerConnectionHandler sch;
    private Player player;
    private Server server;
    private GameController gameController;
    private String nickname;
    private boolean safeConnection;
    private boolean connected;

    public VirtualView(Server server){
        this.safeConnection = true;
        this.connected = true;
        this.server = server;
    }

    public void start(){
        //called from whoever instantiates this object
        new Thread( () -> listener() ).start();
    }

    private void listener(){
        //called asynchronously from start method
        while(connected){
            final Command command;
            try{
                command = sch.getNextCommand();
            } catch (IOException e) {
                this.connected = false;
                break;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(command!=null && command.getCommandType().getHandler().equals("S")){
                new Thread( () -> server.handleCommand(command,this) ).start();
            }
            if(command!=null && command.getCommandType().getHandler().equals("C") && gameController!=null){
                new Thread( () -> gameController.handleCommand(command,this) ).start();
            }
        }
    }

    public void setServerConnectionHandler(ServerConnectionHandler sch) {
        this.sch = sch;
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    public void setGameController(GameController gameController){
        this.gameController = gameController;
    }

    public void sendResponse(Response response) throws IOException {
        sch.sendResponse(response);
    }

    public Player getPlayer(){
        return player;
    }

    public GameController getGameController(){
        return gameController;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public String getNickname(){
        return nickname;
    }

    public void newConnectionCheck(){
        this.safeConnection = false;
    }

    public void pingReceived(){
        this.safeConnection = true;
    }

    public boolean checkCurrConnection(){
        return safeConnection;
    }

    public void disconnect(){
        if(connected){
            this.connected = false;
            sch.disconnect();
            if(player!=null)
                player.disconnect();
        }
    }
}
