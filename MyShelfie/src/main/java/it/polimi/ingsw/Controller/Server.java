package main.java.it.polimi.ingsw.Controller;

import main.java.it.polimi.ingsw.Connection.*;
import main.java.it.polimi.ingsw.Model.Player;

import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Server {
    private static final int timeout = 10;
    private final List<Player> players;
    private final List<VirtualView> virtualViews;
    private final List<VirtualView> queue;

    private final Map<Integer,GameController> games;

    private int numPlayersForMatch;
    private VirtualView firstOfMatch;

    /**
     * Class' constructor
     * @author Alessandro Annechini
     */
    public Server(){
        players = new ArrayList<>();
        virtualViews = new ArrayList<>();
        queue = new ArrayList<>();
        games = new HashMap<>();
    }

    /**
     * Start MyShelfie server
     * @author Alessandro Annechini
     */
    public void start(){
        try(ServerSocket listener = new ServerSocket(54321)){
            RMIWelcomeServer myShelfieRMIServer = new RMIWelcomeServerImpl(this);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("MyShelfieRMIServer", myShelfieRMIServer);

            //new Thread(() -> handleConnections()).start();
            new Thread(() -> handleQueue()).start();

            while(true){
                VirtualView virtualView = null;
                try{
                    Socket socket = listener.accept();
                    ServerConnectionHandlerSocket schs = new ServerConnectionHandlerSocket(socket);
                    virtualView = new VirtualView(this);
                    virtualView.setServerConnectionHandler(schs);
                    addVirtualViewToList(virtualView);
                    virtualView.start();
                    System.out.println("New client connected: " + socket.getInetAddress());
                } catch(Exception e){
                    System.out.println(e.getMessage());
                    if(virtualView!=null) virtualView.disconnect();
                }
            }
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method handles and executes the incoming command
     * @author Alessandro Annechini
     */
    public void handleCommand(Command command, VirtualView virtualView){
        //called asynchronously from virtual view
        System.out.println("New command! -> ");
        if(command == null || !command.getCommandType().getHandler().equals("S")) return;
        System.out.println("New command! "+command.getCommandType());
        if(command.getCommandType() == CommandType.PING){

            virtualView.pingReceived();

        } else if (command.getCommandType() == CommandType.DISCONNECT) {

            virtualView.disconnect();

        } else if(command.getCommandType() == CommandType.LOGIN){

            String nickname= command.getStrParameter("nickname");
            if(nickname == null){

                Response response = new Response(ResponseType.LOGIN_ERROR);
                virtualView.sendResponse(response);

            } else if(!unusedNickname(nickname)){

                //NICKNAME is already in use

                Response response = new Response(ResponseType.LOGIN_ERROR);
                response.setStrParameter("newnickname",findNewNickname(nickname));
                virtualView.sendResponse(response);

            } else {
                Player oldp = searchOldPlayer(nickname);
                if(oldp != null){

                    //NICKNAME corresponds to a disconnected player

                    oldp.getModel().getGameController().reconnect(oldp,virtualView);

                }else{

                    //NICKNAME is valid

                    virtualView.setNickname(nickname);
                    addVirtualViewToList(virtualView);
                    addToQueue(virtualView);
                    Response response = new Response(ResponseType.LOGIN_CONFIRMED);
                    response.setStrParameter("nickname",nickname);
                    virtualView.sendResponse(response);

                }
            }
        } else if(command.getCommandType() == CommandType.PLAYERS_NUM){
            if(virtualView.equals(firstOfMatch) && (numPlayersForMatch<2 || numPlayersForMatch>4 )){
                if(command.getIntParameter("num") < 2 || command.getIntParameter("num") > 4){
                    Response res = new Response(ResponseType.ASK_PLAYERS_NUM);
                    res.setStrParameter("result","Invalid number");
                    virtualView.sendResponse(res);
                }else{
                    Response res = new Response(ResponseType.ASK_PLAYERS_NUM);
                    res.setStrParameter("result","success");
                    virtualView.sendResponse(res);
                    numPlayersForMatch = command.getIntParameter("num");
                    synchronized (queue){
                        queue.notifyAll();
                    }
                }
            }
        }
    }

    /**
     * This method checks if the nickname is not used by any connected player
     * @author Alessandro Annechini
     * @param nickname The nickname to be checked
     * @return True if the nickname is not used by any connected player, false otherwise
     */
    private boolean unusedNickname(String nickname){
        synchronized (virtualViews){
            for(VirtualView vv : virtualViews){
                if(nickname.equals(vv.getNickname())) return false;
            }
        }
        return true;
    }

    /**
     * This method returns the player corresponding to the nickname
     * @author Alessandro Annechini
     * @param nickname The nickname to be checked
     * @return The player with the given nickname (null if it doesn't exist)
     */
    private Player searchOldPlayer(String nickname){
        Player result = null;
        synchronized (players){
            for(Player p : players){
                if(nickname.equals(p.getNickname()) && !p.isConnected()) {
                    result = p;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * This method returns a suggestion for a new valid nickname
     * @param oldNickname An invalid nickname
     * @return The new suggested nickname
     */
    private String findNewNickname(String oldNickname){
        int i = 1;
        String newNickname = oldNickname + String.valueOf(i);
        while(!unusedNickname(newNickname) || searchOldPlayer(newNickname)!=null){
            i++;
            newNickname = oldNickname + String.valueOf(i);
        }
        return newNickname;
    }

    /**
     * This method handles the queue of players waiting to play
     * @author Alessandro Annechini
     */
    private void handleQueue(){
        while(true){
            try{
                numPlayersForMatch = -1;
                firstOfMatch=null;
                synchronized (queue){
                    while(queue.size()==0) queue.wait();
                }
                firstOfMatch = queue.get(0);
                final VirtualView firstOfList = firstOfMatch;

                Response res = new Response(ResponseType.ASK_PLAYERS_NUM);
                firstOfList.sendResponse(res);

                synchronized (queue){
                    while(queue.get(0).equals(firstOfList) && queue.size()<numPlayersForMatch
                            || numPlayersForMatch<2 || numPlayersForMatch>4) queue.wait();
                }
                if(queue.get(0).equals(firstOfList)){
                    //CREATE NEW MATCH

                    List<VirtualView> gamers = new ArrayList<>();
                    synchronized (queue){
                        for(int i = 0; i< numPlayersForMatch; i++ ){
                            gamers.add(popFromQueue());
                        }
                        firstOfMatch=null;
                        queue.notifyAll();
                    }
                    new GameController(this,gamers,0);
                }
            } catch (Exception e){}
        }
    }

    /**
     * This method checks periodically the connections of all the players connected to the server
     * @author Alessandro Annechini
     */
    private void handleConnections(){
        List<VirtualView> tested = new ArrayList<>();
        while(true){
            tested.clear();
            synchronized (virtualViews){
                tested.addAll(virtualViews);
            }
            for(VirtualView vv : tested){
                vv.newConnectionCheck();
                vv.sendResponse(new Response(ResponseType.PONG));
            }
            try{
                TimeUnit.SECONDS.sleep(timeout/2);

                //Send PONG
                for(VirtualView vv : tested){
                    vv.sendResponse(new Response(ResponseType.PONG));
                }

                //Disconnect virtual view that didn't respond
                for(VirtualView vv : tested){
                    if(!vv.checkCurrConnection()){
                        final VirtualView vview = vv;
                        new Thread(() -> vview.disconnect()).start();
                    }
                }

                TimeUnit.SECONDS.sleep( timeout % 2==0 ? timeout/2 : timeout/2 +1);
            } catch(Exception ignored){}
        }
    }


    public List<Player> getPlayers() {
        return players;
    }

    /**
     * This method adds a Player to the global list of players
     * @author Alessandro Annechini
     * @param player The player to be added
     */
    public void addPlayerToList(Player player){
        synchronized(players){
            players.add(player);
            players.notifyAll();
        }
    }

    /**
     * This method removes a Player from the global list of players
     * @author Alessandro Annechini
     * @param player The player to be removed
     */
    public void removePlayerFromList(Player player){
        synchronized(players){
            players.remove(player);
            players.notifyAll();
        }
    }

    public List<VirtualView> getVirtualViews() {
        return virtualViews;
    }

    /**
     * This method adds the virtual view to the global list of virtual views
     * @author Alessandro Annechini
     * @param virtualView The virtual view to be added
     */
    public void addVirtualViewToList(VirtualView virtualView){
        synchronized (virtualViews) {
            virtualViews.add(virtualView);
            virtualViews.notifyAll();
        }
    }

    /**
     * This method adds the virtual view to the queue
     * @author Alessandro Annechini
     * @param virtualView The virtual view to be added
     */
    public void addToQueue(VirtualView virtualView){
        synchronized (queue){
            queue.add(virtualView);
            queue.notifyAll();
        }
    }

    /**
     * This method returns the first virtual view of the queue, removing it from the queue
     * @author Alessandro Annechini
     * @return The popped virtual view
     */
    public VirtualView popFromQueue(){
        VirtualView ret;
        synchronized (queue){
            ret = queue.get(0);
            queue.remove(0);
            queue.notifyAll();
        }
        return ret;
    }

    /**
     * This method removes the virtual view from the global list of virtual views (and from the queue, if present)
     * @author Alessandro Annechini
     * @param virtualView The virtual view to be removed
     */
    public void removeVirtualViewFromList(VirtualView virtualView){
        synchronized (virtualViews) {
            virtualViews.remove(virtualView);
            virtualViews.notifyAll();
        }
        synchronized (queue){
            queue.remove(virtualView);
            queue.notifyAll();
        }
    }

    /**
     * This method removes the players of the game that just ended from the global list and signals the deletion to the virtual view
     * @author Alessandro Annechini
     * @param gameController The game controller of the game that just ended
     */
    public void endGame(GameController gameController){
        for(Player p : gameController.getPlayers()){
            removePlayerFromList(p);
            p.getVirtualView().exitGame();
        }
    }
}
