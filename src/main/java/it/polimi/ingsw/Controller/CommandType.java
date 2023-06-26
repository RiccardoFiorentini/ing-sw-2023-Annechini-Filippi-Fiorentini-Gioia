package it.polimi.ingsw.Controller;

public enum CommandType {
    //The client confirms the connection
    PING("S"),

    // Disconnection
    DISCONNECT("S"),

    //Enter the queue with a nickname
    LOGIN("S"),

    //The first client of the queue decides the number of players
    //if the number inserted is invalid another request is sent
    PLAYERS_NUM("S"),

    //Ack sent from the players after the game starts
    GAME_JOINED("C"),

    // Send a new message in the chat
    // Map<String,String> strArgs : {<"receiver", "nickname of receiver or null if broadcast">, <"text", "message sent">}
    SEND_MEX_CHAT("C"),

    // Select column
    // Map<String,Integer> intArgs : {<"value", columnIndex>}
    SELECT_COLUMN("C"),

    // Select tile
    // Map<String,Integer> intArgs : {<"row", rowIndex>, <"col", colIndex>}
    SELECT_TILE("C"),

    // Put the tile from the buffer to the shelf column
    // Map<String,Integer> intArgs : {<"index", bufferIndex>}
    PUT_IN_COLUMN("C");

    final String handler;

    CommandType(String handler){
        this.handler = handler;
    }

    public String getHandler() {
        return handler;
    }
}