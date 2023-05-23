package main.java.it.polimi.ingsw.Controller;

public enum ResponseType {
    //The server confirms the connection
    PONG,

    //The login failed, a new nickname is proposed
    //Map<String, String> strArgs : {<"newnickname", proposed nickname>}
    LOGIN_ERROR,

    //The client entered the queue
    //Map<String, String> strArgs : {<"nickname", accepted nickname>}
    LOGIN_CONFIRMED,

    //The server asks the number of players in the game
    //Map<String, String> strArgs : {<"result", null(first request)||"success"(number accepted)||"Invalid number"(invalid number) >}
    ASK_PLAYERS_NUM,

    //The game started, the initial conditions are sent as arguments
    // Initial conditions:
    // Map<String,Integer> intArgs : {<"firstPlayerId", id>, <"commonGoal1", id(1-12)>, <"commonGoal2", id(1-12)>, <"commonGoalsRemainingPoint1", points>, <"commonGoalsRemainingPoint2", points>,
    //                          <"currentPlayer",id>, <"personalGoal", personalGoal id>, <"isStart", 1 if it's the starting message or 0 if it is used for reconnection>}
    // Map<String,Object> objArgs : {<"board", BoardBean>, <"chat", chatBean>, <"shelves", List<ShelfBean>>, <"nicknames", List<String>>, <"turnIds", List<Integer>>,
    // <"personalGoalMatrix", TilesColor[][] >, <"commonGoalPoints1", List<Integer>>, <"commonGoalPoints2", List<Integer>>, <"connected", List<Boolean>>}
    // Map<String,String> strArgs : {<"commonGoalDescription1", description>, <"commonGoalDescription2", description>}
    // shelves, nicknames, turnIds, personalGoal, connected, commonGoalPoints1 and commonGoalPoints2 follow the order of the players in the list.
    GAME_STARTED,

    // A new message has been sent to the chat
    // Map<String,Object> objArgs : {<"chat", chatBean>}
    NEW_MEX_CHAT,

    // A new turn begins, the current player is specified in the arguments
    // The server sends a boolean matrix corresponding to the pickable tiles to the current player (just for GUI)
    // Map<String,Integer> intArgs : {<"CurrentPlayerId", id>}
    // Map<String,Object> objArgs : {<"pickableTiles", boolean[][]>}
    NEW_TURN,

    // SELECT_COLUMN response
    // Column selected correctly (or error),
    // if successful the selected column is sent as an argument
    // Map<String,String> strArgs : {<"result", "success"(accepted)||"Not your turn"||"Wrong phase"||"Full column"||"Error"(generic exception)>}
    SELECT_COLUMN_RESULT,

    // SELECT_TILE response
    // If there is an error, it is signaled
    // If the tile is selected correctly, for the first selection a boolean
    // matrix corresponding to the pickable tiles is sent, for the second
    // selection the updated buffer is sent
    // Map<String,String> strArgs : {<"result", "success"(accepted)||"Tile is not pickable"||"Wrong phase"||"Not your turn"||"Error"(generic exception)>}
    // Map<String,Object> objArgs : {<"pickableTiles", boolean[][] if first time>,<"buffer", Tile[3]>}
    SELECT_TILE_RESULT,

    // The board is updated
    // Map<String,Object> objArgs : {<"board", boardBean>}
    UPDATE_BOARD,

    // The shelf of a player with playerId = id is updated
    // Map<String,Object> objArgs : {<"shelf", shelfBean>}
    // Map<String,Integer> intArgs : {<"playerId", id>}
    UPDATE_PLAYER_SHELF,

    // PUT_IN_COLUMN response
    // If there is an error, it is signaled
    // When the last tile is put, the change of turn is signaled in the args
    // Map<String,String> strArgs : {<"result", "success"||"Wrong phase"||"Not your turn"||"Error"(generic exception)>}
    // Map<String,Integer> intArgs : {<"turnFinished", 0 if turn not finished, 1 otherwise>}
    // Map<String,String> objArgs : {<"buffer", Tile[3]>}
    PUT_IN_COLUMN_RESULT,

    // A player disconnected
    //Map<String,Integer> intArgs : {<"PlayerId", id of disconnected player>}
    PLAYER_DISCONNECTED,

    // A player reconnected
    // Map<String,Integer> intArgs : {<"PlayerId", id of re-connected player>}
    // Map<String, Object> : {<"connected", List<Boolean>>}
    PLAYER_RECONNECTED,

    // The player is the only one connected
    ONLY_ONE_CONNECTED,

    // The player is the only one remaining, the time
    // remaining for his victory is sent
    // Map<String, Integer>: {<"Timermilliseconds", timerMilliSeconds>},
    ONLY_ONE_CONNECTED_TIMER,

    // The game ended, if it ends correctly the points (in order of
    // turnId) are sent, if the game ends because all but one player
    // disconnected, the victory of the remaining player is signaled
    // Map<String, Object> : {<"finalPoints", List<Integer>>, <"finalPersonalGoalPoints", List<Integer>>, <"finalColorGroupPoints", List<Integer>>}
    // Map<String, Integer>: {<"interrupted", 0 if it finished correctly, 1 if not>}
    GAME_ENDED,

    // A player won a common goal: the winner of the common goal (turnId),
    // the id of the common goal
    // points remaining for that common goal are sent as arguments
    // Map<String,Integer> intArgs : {<"PlayerId", id>, <"pointswon", points>, <"CommonGoalId", id (0 or 1)>, <"remainingpoints", remaining points>}
    //
    COMMON_GOAL_WON,

    //a player completed the shelf
    //Map<String,Integer> intArgs : {<"PlayerId", id>}
    SHELF_COMPLETED

}