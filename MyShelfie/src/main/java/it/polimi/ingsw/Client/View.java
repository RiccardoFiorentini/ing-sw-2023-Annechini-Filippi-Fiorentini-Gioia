package main.java.it.polimi.ingsw.Client;

import javafx.application.Application;
import main.java.it.polimi.ingsw.Connection.ClientConnectionHandler;
import main.java.it.polimi.ingsw.Controller.Command;
import main.java.it.polimi.ingsw.Controller.Response;


public interface View{

    /**
     * Starts the View of the game
     * @author Nicole Filippi
     */
    public void start();

    /**
     * Method that handles the responses sent by the server to the client
     * @author Nicole Filippi
     * @param resp is the response that needs to be handled
     */
    public void handleResponse(Response resp);

    /**
     * Method that sends a given command to the server
     * @author Nicole Filippi
     * @param command is the command that has to be sent
     */
    public void sendCommand(Command command);

}
