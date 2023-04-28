package main.java.it.polimi.ingsw.Client;

import main.java.it.polimi.ingsw.Connection.ClientConnectionHandler;
import main.java.it.polimi.ingsw.Controller.Response;

public class GUI extends View{
    public GUI(ClientConnectionHandler cch) {
        super(cch);
    }

    @Override
    public void handleResponse(Response resp) {

    }

    @Override
    public void handleEvent(String event) {

    }
}
