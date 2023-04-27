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

    /**
     * Class' constructor
     * @author Alessandro Annechini
     * @param server The main server
     */
    public VirtualView(Server server){
        this.safeConnection = true;
        this.connected = true;
        this.server = server;
    }

    /**
     * This method starts a new thread to listen for new Commands
     * @author Alessandro Annechini
     */
    public void start(){
        //called from whoever instantiates this object
        new Thread( () -> listener() ).start();
    }

    /**
     * This method waits for new Commands and then starts a thread in order to execute it
     * @author Alessandro Annechini
     */
    private void listener(){
        //called asynchronously from start method
        while(connected){
            final Command command;
            try{
                command = sch.getNextCommand();
            } catch (IOException e) {
                disconnect();
                break;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch(Exception e){
                disconnect();
                break;
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

    /**
     * This method sends a Response to the client. If something goes wrong, the client is disconnected
     * @author Alessandro Annechini
     * @param response The response to be sent
     */
    public void sendResponse(Response response){
        if(connected){
            try{
                sch.sendResponse(response);
            } catch(IOException e) {
                e.printStackTrace();
                disconnect();
            }
        }
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

    /**
     * This method starts a new connection check
     * @author Alessandro Annechini
     */
    public void newConnectionCheck(){
        this.safeConnection = false;
    }

    /**
     * This method asserts the correct connection to the client
     * @author Alessandro Annechini
     */
    public void pingReceived(){
        this.safeConnection = true;
    }

    /**
     * Check if the client asserted the validity of the connection
     * @author Alessandro Annechini
     * @return True if a PING has arrived, false otherwise
     */
    public boolean checkCurrConnection(){
        return safeConnection;
    }

    /**
     * This method disconnects the virtual view (with the corresponding ServerConnectionHandler and eventual Player).
     * Note that this method may take a while to execute, so it is best to start a new thread
     * @author Alessandro Annechini
     */
    public void disconnect(){
        System.out.println(server.getVirtualViews().size());
        server.removeVirtualViewFromList(this);
        if(connected){
            System.out.println("Player disconnected");
            System.out.println(server.getVirtualViews().size());
            this.connected = false;
            sch.disconnect();
            if(player!=null)
                player.disconnect();
        }
    }

    /**
     * This virtual view exits the game and returns again an anonymous virtual view
     * @author Alessandro Annechini
     */
    public void exitGame(){
        this.player = null;
        this.nickname = null;
        this.gameController = null;
    }

    public boolean isConnected(){
        return connected;
    }
}
