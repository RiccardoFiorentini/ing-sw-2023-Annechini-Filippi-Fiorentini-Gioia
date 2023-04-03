package main.java.it.polimi.ingsw.Controller;

public enum CommandType {
    //Handshake between client and server
    HANDSHAKE,

    //A player tries to create or enter a match
    ENTER_MATCH,

    //The admin starts the match
    START_MATCH,

    //The current player selects the column
    SELECT_COLUMN,

    //The current player selects a tile from the board
    SELECT_TILE,

    //The current player puts a tile in the shelf
    PUT_IN_COL,

    //A player writes a message
    WRITE_MESSAGE,

    //A signal is sent to the server in order to check the connection
    PING
}