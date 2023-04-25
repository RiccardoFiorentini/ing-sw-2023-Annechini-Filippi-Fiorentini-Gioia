package main.java.it.polimi.ingsw.Controller;

import main.java.it.polimi.ingsw.Connection.VirtualView;
import main.java.it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.List;

public class Server {

    private final List<Player> players;
    private final List<VirtualView> virtualViews;


    public Server(){
        players = new ArrayList<>();
        virtualViews = new ArrayList<>();

        //...
    }

    public void start(){
        //Instantiates RMI welcome server and listens for socket clients
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayerToList(Player player){
        synchronized(players){
            players.add(player);
        }
    }

    public void removePlayerFromList(Player player){
        synchronized(players){
            players.remove(player);
        }
    }

    public List<VirtualView> getVirtualViews() {
        return virtualViews;
    }

    public void addVirtualViewToList(VirtualView virtualView){
        synchronized (virtualViews) {
            virtualViews.add(virtualView);
        }
    }

    public void removeVirtualViewFromList(VirtualView virtualView){
        synchronized (virtualViews) {
            virtualViews.remove(virtualView);
        }
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
