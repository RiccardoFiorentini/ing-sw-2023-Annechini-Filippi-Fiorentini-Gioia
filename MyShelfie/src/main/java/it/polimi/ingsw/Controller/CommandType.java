package main.java.it.polimi.ingsw.Controller;

public enum CommandType {
    //The client confirms the connection
    PING("S"),

    // Disconnection
    DISCONNECT("S"),

    //Enter the queue with a nickname
    LOGIN("S"),

    //The first client of the queue decides the number of players
    //if the number inserted is invalid another request is sent
    PLAYERS_NUM("C"),

    //Ack sent from the players after the game starts
    GAME_JOINED("C"),

    // Send a new message in the chat
    SEND_MEX_CHAT("C"),

    // Select column
    SELECT_COLUMN("C"),

    // Select tile
    SELECT_TILE("C"),

    // Put the tile from the buffer to the shelf column
    PUT_IN_COLUMN("C");

    String handler;

    CommandType(String handler){
        this.handler = handler;
    }

    public String getHandler() {
        return handler;
    }
}