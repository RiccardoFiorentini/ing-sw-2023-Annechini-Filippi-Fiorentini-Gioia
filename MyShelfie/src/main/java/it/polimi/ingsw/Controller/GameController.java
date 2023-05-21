package main.java.it.polimi.ingsw.Controller;

import main.java.it.polimi.ingsw.Connection.VirtualView;
import main.java.it.polimi.ingsw.Model.*;
import main.java.it.polimi.ingsw.ModelExceptions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static main.java.it.polimi.ingsw.Controller.ResponseType.*;

public class GameController {
    private final List<Player> players;
    private int joined;
    private final Server server;
    private final Model model;

    /**
     * Class's constructor
     * @author Fiorentini Riccardo
     * @param server reference to the server
     * @param player list of player in the game
     * @param gameId game identifier
     * */
    public GameController(Server server, List<VirtualView> player, int gameId) throws NotToRefillException, WrongTurnException, IOException {
        this.server = server;
        joined = 0;
        this.players = new ArrayList<>();
        for(VirtualView vv: player){
            Player newp = new Player(vv.getNickname(), this);
            this.players.add(newp);
            server.addPlayerToList(newp);
            vv.setPlayer(newp);
            vv.setGameController(this);
            newp.setVirtualView(vv);
        }
        this.model = new Model(gameId, this.players);
        model.updateStartGame();
    }

    /**
     * Method to handle all the commands
     * @author Fiorentini Riccardo
     * @param command object command received
     * @param virtualView virtual view of the player who sent the command
     * */
    public void handleCommand(Command command, VirtualView virtualView) {
        switch(command.getCommandType()) {
            case GAME_JOINED:
                int jcopy;
                synchronized (this) {
                    joined = joined + 1;
                    jcopy=joined;
                }
                if(jcopy == model.getNumPlayers()) {
                    model.updateNewTurn(model.getTurnId(),model.getPlayerByTurnId(model.getTurnId()).getPickableTiles());
                }
                break;
            case SEND_MEX_CHAT:
                if(command.getStrParameter("text")==null || command.getStrParameter("text").equals("")
                        || command.getStrParameter("text").equals(" ")){
                    return;
                }
                if(command.getStrParameter("receiver") == null){
                    try {
                        model.getChat().writeMessage(virtualView.getPlayer(), command.getStrParameter("text"));
                        model.updateChat(new ArrayList<>(players));
                    } catch (IncorrectMessageException e) {
                        return;
                    }
                }else{
                    try {
                        List<Player> toUpdate = new ArrayList<>();
                        toUpdate.add(virtualView.getPlayer());
                        for(Player p : players){
                            if(p.getNickname().equals(command.getStrParameter("receiver"))){
                                model.getChat().writeMessage(virtualView.getPlayer(), p, command.getStrParameter("text"));
                                toUpdate.add(p);
                            }
                        }
                        model.updateChat(toUpdate);
                    } catch (IncorrectMessageException e) {
                        return;
                    }
                }
                break;
            case SELECT_COLUMN:
                Response result1 = new Response(SELECT_COLUMN_RESULT);
                try {
                    if(command.getIntParameter("value")<0 || command.getIntParameter("value")>4)
                        throw new Exception();
                    virtualView.getPlayer().setSelectedColumn(command.getIntParameter("value"));
                    result1.setStrParameter("result", "success");
                    virtualView.sendResponse(result1);
                } catch (WrongTurnException e) {
                    result1.setStrParameter("result", "Not your turn");
                    virtualView.sendResponse(result1);
                } catch (WrongPhaseException e) {
                    result1.setStrParameter("result", "Wrong phase");
                    virtualView.sendResponse(result1);
                } catch (FullColumnException e){
                    result1.setStrParameter("result", "Full column");
                    virtualView.sendResponse(result1);
                } catch(Exception e){
                    result1.setStrParameter("result", "Error");
                    virtualView.sendResponse(result1);
                }
                break;
            case SELECT_TILE:
                try {
                    virtualView.getPlayer().selectTile(command.getIntParameter("row"), command.getIntParameter("col"));
                } catch (NotPickableException e) {
                    Response result2 = new Response(SELECT_TILE_RESULT);
                    result2.setStrParameter("result", "Tile is not pickable");
                    virtualView.sendResponse(result2);
                } catch (WrongPhaseException e) {
                    Response result2 = new Response(SELECT_TILE_RESULT);
                    result2.setStrParameter("result", "Wrong phase");
                    virtualView.sendResponse(result2);
                } catch (WrongTurnException e) {
                    Response result2 = new Response(SELECT_TILE_RESULT);
                    result2.setStrParameter("result", "Not your turn");
                    virtualView.sendResponse(result2);
                } catch(Exception e){
                    Response result2 = new Response(SELECT_TILE_RESULT);
                    result2.setStrParameter("result", "Error: "+e.getMessage());
                    virtualView.sendResponse(result2);
                }

                break;
            case PUT_IN_COLUMN:
                try {
                    virtualView.getPlayer().putInColumn(command.getIntParameter("index"));
                } catch (WrongTurnException e) {
                    Response result3 = new Response(PUT_IN_COLUMN_RESULT);
                    result3.setStrParameter("result", "Not your turn");
                    virtualView.sendResponse(result3);
                } catch (WrongPhaseException e) {
                    Response result3 = new Response(PUT_IN_COLUMN_RESULT);
                    result3.setStrParameter("result", "Wrong phase");
                    virtualView.sendResponse(result3);
                } catch(Exception e){
                    Response result3 = new Response(PUT_IN_COLUMN_RESULT);
                    result3.setStrParameter("result", "Error: "+e.getMessage());
                    virtualView.sendResponse(result3);
                }
                break;
            default:
                //
        }
        if(model.isGameFinished()){
            server.endGame(this);
        }
    }

    /**
     * Method to notify the players about the reconnection of another player
     * and sets the player to the new virtual view
     * @author Fiorentini Riccardo
     * @param vv virtual view of the reconnected player
     * @param pl reconnected player
     * */
    public void reconnected(VirtualView vv, Player pl){
        vv.setNickname(pl.getNickname());
        vv.setPlayer(pl);
        vv.setGameController(this);
        pl.reconnect(vv);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getJoined() {
        return joined;
    }

    public Model getModel() {
        return model;
    }

    public Server getServer() {
        return server;
    }
}
