package main.java.it.polimi.ingsw.Controller;

import main.java.it.polimi.ingsw.Connection.VirtualView;
import main.java.it.polimi.ingsw.Model.Player;

import java.util.List;

public class Server {

    private List<Player> players;
    private List<VirtualView> virtualViews;


    public Server(){

    }

    public void start(){
        //Instantiates RMI welcome server
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<VirtualView> getVirtualViews() {
        return virtualViews;
    }

    public void handleCommand(Command command, VirtualView virtualView){
        //called asynchronously from virtual view
    }

    private void handleQueue(){
        //called asynchronously from start method
    }

    private void handleConnections(){
        //called asynchronously from start method
    }

}
