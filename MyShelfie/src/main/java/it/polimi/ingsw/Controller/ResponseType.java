package main.java.it.polimi.ingsw.Controller;

public enum ResponseType {
    //The server confirms the connection
    PONG,

    //The login failed, a new nickname is proposed
    LOGIN_ERROR,

    //The client entered the queue
    LOGIN_CONFIRMED,

    //The server asks the number of players in the game
    ASK_PLAYERS_NUM,

    //The game started, the initial conditions are sent as arguments
    // Initial conditions:
    // Map<String,Integer> intArgs : {<"firstPlayerId", id>, <"commonGoal1", id>, <"commonGoal2", id>, <"commonGoalsRemainingPoint1", points>, <"commonGoalsRemainingPoint2", points>}
    // Map<String,Object> objArgs : {<"board", board>, <"chat", chat>, <"shelves", List<Shelf>>, <"nicknames", List<String>>, <"turnIds", List<Integer>>,
    // <"personalGoals", List<Integer>>, <"commonGoalPoints1", List<Integer>>, <"commonGoalPoints2", List<Integer>>}
    //
    // shelves, nicknames, turnIds, peronalGoals, commonGoalPoints1 and commonGoalPoints2 follow the order of the players in the list.
    GAME_STARTED,

    // A new message has been sent to the chat
    // Map<String,Object> objArgs : {<"chat", chat>}
    NEW_MEX_CHAT,

    // A new turn begins, the current player is specified in the arguments
    // The server sends a boolean matrix corresponding to the pickable tiles to the current player
    // Map<String,Integer> intArgs : {<"CurrentPlayerId", id>}
    // Map<String,Object> objArgs : {<"pickableTiles", boolean pickableTiles[][]>}
    NEW_TURN,

    // SELECT_COLUMN response
    // Column selected correctly (or error),
    // if successful the selected column is sent as an argument
    // Map<String,String> strArgs : {<"result", "success" or error string>}
    SELECT_COLUMN_RESULT,

    // SELECT_TILE response
    // If there is an error, it is signaled
    // If the tile is selected correctly, for the first selection a boolean
    // matrix corresponding to the pickable tiles is sent, for the second
    // selection the updated buffer is sent
    // Map<String,String> strArgs : {<"result", "success" or error string>}
    // Map<String,Object> objArgs : {<"pickableTiles", boolean pickableTiles[][]>}
    SELECT_TILE_RESULT,

    // The board is updated
    // Map<String,Object> objArgs : {<"board", board>}
    UPDATE_BOARD,

    // The shelf of a player with playerId = id is updated
    // Map<String,Object> objArgs : {<"shelf", shelf>}
    // Map<String,Integer> intArgs : {<"playerId", id>}
    UPDATE_PLAYER_SHELF,

    // PUT_IN_COLUMN response
    // If there is an error, it is signaled
    // When the last tile is put, the change of turn is signaled in the args
    // Map<String,String> strArgs : {<"result", "success" or error string>}
    // Map<String,Integer> intArgs : {<"currentPlayerId", id or -1 if the turn is not finished>}
    PUT_IN_COLUMN_RESULT,

    // A player disconnected
    PLAYER_DISCONNECTED,

    // A player reconnected
    PLAYER_RECONNECTED,

    // The player is the only one connected
    ONLY_ONE_CONNECTED,

    // The player is the only one remaining, the time
    // remaining for his victory is sent
    ONLY_ONE_CONNECTED_TIMER,

    // The game ended, if it ends correctly the points (in order of
    // turnId) are sent, if the game ends because all but one player
    // disconnected, the victory of the remaining player is signaled
    GAME_ENDED,

    // A player won a common goal: the winner of the common goal (turnId),
    // the id of the common goal, the points won by the player and the
    // points remaining for that common goal are sent as arguments
    COMMON_GOAL_WON

}