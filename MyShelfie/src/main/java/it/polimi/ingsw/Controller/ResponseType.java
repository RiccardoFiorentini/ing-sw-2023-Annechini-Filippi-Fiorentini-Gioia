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
    // Initial conditions: board, shelves, nicknames, turnIds, firstPlayerId,
    // common_goals_id, personal_goal_id, chat, common_goals_points for
    // each player, common_goals_remaining_point for each common goal
    GAME_STARTED,

    // A new message has been sent to the chat
    NEW_MEX_CHAT,

    // A new turn begins, the current player is specified in the arguments
    // The server sends a boolean matrix corresponding to the pickable tiles to the current player
    NEW_TURN,

    // SELECT_COLUMN response
    // Column selected correctly (or error),
    // if successful the selected column is sent as an argument
    SELECT_COLUMN_RESULT,

    // SELECT_TILE response
    // If there is an error, it is signaled
    // If the tile is selected correctly, for the first selection a boolean
    // matrix corresponding to the pickable tiles is sent, for the second
    // selection the updated buffer is sent
    SELECT_TILE_RESULT,

    // The board is updated
    UPDATE_BOARD,

    // The shelf of a player is updated
    UPDATE_PLAYER_SHELF,

    // PUT_IN_COLUMN response
    // If there is an error, it is signaled
    // When the last tile is put, the change of turn is signaled in the args
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