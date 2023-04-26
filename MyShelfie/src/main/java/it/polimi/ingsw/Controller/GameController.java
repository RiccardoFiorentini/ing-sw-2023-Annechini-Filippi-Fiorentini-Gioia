package main.java.it.polimi.ingsw.Controller;

import main.java.it.polimi.ingsw.Connection.VirtualView;
import main.java.it.polimi.ingsw.Model.*;
import main.java.it.polimi.ingsw.ModelExceptions.*;

import java.io.IOException;
import java.util.List;

import static main.java.it.polimi.ingsw.Controller.ResponseType.*;

public class GameController {

    private final List<Player> players;
    private final List<VirtualView> virtualViews;
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
        players = null;
        this.virtualViews = player;

        for(VirtualView vv: player){
            this.players.add(new Player(vv.getNickname()));
            server.addPlayerToList(players.get(player.size()-1));
            vv.setPlayer(players.get(player.size()-1));
        }

        this.model = new Model(gameId, this.players);

        Response start = new Response(GAME_STARTED);
        start.setObjParameter("board", model.getBoard());
        start.setIntParameter("firstPlayerId", model.getFirstToStart());
        start.setIntParameter("commonGoal1", model.getCommonGoals()[0].getIndex());
        start.setIntParameter("commonGoal2", model.getCommonGoals()[1].getIndex());
        start.setObjParameter("chat", model.getChat());
        start.setIntParameter("commonGoalsRemainingPoint1", model.getCommonGoals()[0].getPointsLeft());
        start.setIntParameter("commonGoalsRemainingPoint2", model.getCommonGoals()[1].getPointsLeft());
        List<Shelf> shelves = null;
        List<String> nicknames = null;
        List<Integer> turnIds = null;
        List<Integer> personalGoals = null;
        List<Integer> commonGoalPoints1 = null;
        List<Integer> commonGoalPoints2 = null;
        for(Player p: players){
            shelves.add(p.getShelf());
            nicknames.add(p.getNickname());
            turnIds.add(p.getTurnId());
            personalGoals.add(p.getPersonalGoal().getIndex());
            commonGoalPoints1.add(p.getPointsCommonGoal()[0]);
            commonGoalPoints2.add(p.getPointsCommonGoal()[1]);
        }
        start.setObjParameter("shelves", shelves);
        start.setObjParameter("nicknames", nicknames);
        start.setObjParameter("turnIds", turnIds);
        start.setObjParameter("personalGoals", personalGoals);
        start.setObjParameter("common_goal_points_1", commonGoalPoints1);
        start.setObjParameter("common_goal_points_2", commonGoalPoints2);

        for(VirtualView vv: player){
            vv.sendResponse(start);
        }
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
                joined = joined + 1;
                if(joined == model.getNumPlayers()) {
                    Response result0 = new Response(NEW_TURN);
                    result0.setIntParameter("CurrentPlayerId", model.getTurnId());
                    result0.setObjParameter("pickableTiles", players.get(model.getTurnId()).getPickableTiles());
                    for (VirtualView vv : this.virtualViews) {
                        vv.sendResponse(result0);
                    }
                }
                break;
            case SEND_MEX_CHAT:
                if(command.getStrParameter("receiver") == null){
                    try {
                        model.getChat().writeMessage(virtualView.getPlayer(), command.getStrParameter("text"));
                        Response message = new Response(NEW_MEX_CHAT);
                        message.setObjParameter("chat", model.getChat());
                        for (VirtualView vv : this.virtualViews) {
                            vv.sendResponse(message);
                        }
                    } catch (IncorrectMessageException e) {
                        return;
                    }
                }else{
                    try {
                        for(Player p: players) {
                            if (p.getNickname().equals(command.getStrParameter("receiver"))) {
                                model.getChat().writeMessage(virtualView.getPlayer(), p, command.getStrParameter("text"));
                                Response message = new Response(NEW_MEX_CHAT);
                                message.setObjParameter("chat", model.getChat());
                                this.virtualViews.get(p.getTurnId()).sendResponse(message);
                            }
                        }
                    } catch (IncorrectMessageException e) {
                        return;
                    }
                }
                break;
            case SELECT_COLUMN:
                Response result1 = new Response(SELECT_COLUMN_RESULT);
                try {
                    virtualView.getPlayer().setSelectedColumn(command.getIntParameter("value"));
                    result1.setStrParameter("result", "success");
                    virtualView.sendResponse(result1);
                } catch (WrongTurnException e) {
                    result1.setStrParameter("result", "Not your turn");
                    virtualView.sendResponse(result1);
                } catch (WrongPhaseException e) {
                    result1.setStrParameter("result", "Wrong phase");
                    virtualView.sendResponse(result1);
                }
                break;
            case SELECT_TILE:
                Response result2 = new Response(SELECT_TILE_RESULT);
                try {
                    virtualView.getPlayer().selectTile(command.getIntParameter("row"), command.getIntParameter("col"));
                    result2.setStrParameter("result", "success");
                    result2.setObjParameter("pickabletiles", virtualView.getPlayer().getPickableTiles());
                    virtualView.sendResponse(result2);
                    if(virtualView.getPlayer().getX2() != -1){
                        Response board = new Response(UPDATE_BOARD);
                        board.setObjParameter("board", model.getBoard());
                        for(VirtualView vv: virtualViews){
                            vv.sendResponse(board);
                        }
                    }
                } catch (NotPickableException e) {
                    result2.setStrParameter("result", "Tile is not pickable");
                    virtualView.sendResponse(result2);
                } catch (WrongPhaseException e) {
                    result2.setStrParameter("result", "Wrong phase");
                    virtualView.sendResponse(result2);
                } catch (WrongTurnException e) {
                    result2.setStrParameter("result", "Not your turn");
                    virtualView.sendResponse(result2);
                }

                break;
            case PUT_IN_COLUMN:
                Response result3 = new Response(PUT_IN_COLUMN_RESULT);
                try {
                    virtualView.getPlayer().putInColumn(command.getIntParameter("index"));
                    result3.setStrParameter("result", "success");
                    if(virtualView.getPlayer().getPickedTiles()[0] == Tile.EMPTY && virtualView.getPlayer().getPickedTiles()[1] == Tile.EMPTY && virtualView.getPlayer().getPickedTiles()[0] == Tile.EMPTY){
                        result3.setIntParameter("currentPlayerId", model.getTurnId());
                    }else{
                        result3.setIntParameter("currentPlayerId", -1);
                    }
                    Response shelf = new Response(UPDATE_PLAYER_SHELF);
                    shelf.setObjParameter("shelf", virtualView.getPlayer().getShelf());
                    shelf.setIntParameter("playerId", virtualView.getPlayer().getTurnId());
                    for(VirtualView vv: virtualViews){
                        vv.sendResponse(shelf);
                    }
                } catch (FullColumnException e) {
                    throw new RuntimeException(e);
                } catch (NotToRefillException e) {
                    throw new RuntimeException(e);
                } catch (WrongTurnException e) {
                    result3.setStrParameter("result", "Not your turn");
                    virtualView.sendResponse(result3);
                } catch (WrongPhaseException e) {
                    result3.setStrParameter("result", "Wrong phase");
                    virtualView.sendResponse(result3);
                }
                break;
            default:
                //
        }
    }

    /*getter e setter*/
}
