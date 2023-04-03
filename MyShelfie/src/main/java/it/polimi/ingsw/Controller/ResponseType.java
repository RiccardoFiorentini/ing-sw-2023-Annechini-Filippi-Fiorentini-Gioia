package main.java.it.polimi.ingsw.Controller;

public enum ResponseType {
    //Handshake between client and server
    HANDSHAKE,

    //The player entered (or created) a match
    ENTERED_MATCH,

    //The username is already used (a new username is suggested)
    INVALID_USERNAME,

    //The game starts (or is resumed)
    START_GAME,

    //Next turn
    NEXT_TURN,

    //The board is updated
    UPDATE_BOARD,

    //One of the shelves is updated
    UPDATE_SHELF,

    //Points on common goals are updated
    UPDATE_COMMON_GOAL,

    //The game finishes, points are returned
    END_GAME,

    //General updates for the current player's view
    UPDATE_CURRENT_INFO,

    //Response to a PING
    PONG,

    //A message arrives
    NEW_MESSAGE,

    //Generic error
    ERROR
}