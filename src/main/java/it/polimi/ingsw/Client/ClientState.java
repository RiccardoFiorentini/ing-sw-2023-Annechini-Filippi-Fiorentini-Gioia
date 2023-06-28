package it.polimi.ingsw.Client;

public enum ClientState {
    BEFORE_LOGIN,
    ASK_PLAYERS_NUM,
    QUEUE,
    MATCH_IDLE,
    SELECT_COLUMN,
    SELECT_FIRST_TILE,
    SELECT_SECOND_TILE,
    PUT_IN_COLUMN,

    GAME_ENDED,

}
