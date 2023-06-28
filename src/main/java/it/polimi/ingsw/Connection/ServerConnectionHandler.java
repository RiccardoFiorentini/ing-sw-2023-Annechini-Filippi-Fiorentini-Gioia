package it.polimi.ingsw.Connection;

import it.polimi.ingsw.Controller.Command;
import it.polimi.ingsw.Controller.Response;

import java.io.IOException;

public interface ServerConnectionHandler {
    /**
     * This (blocking) method returns the client command, when one is sent
     * @author Alessandro Annechini
     * @return The Command object sent from the client
     */
    Command getNextCommand() throws IOException, ClassNotFoundException, InterruptedException;
    /**
     * This method sends the response to the client
     * @author Alessandro Annechini
     * @param response The response to be sent
     */
    void sendResponse(Response response) throws IOException;
    /**
     * This method ensures the termination of the connection between the client and the server
     * @author Alessandro Annechini
     */
    void disconnect();
}