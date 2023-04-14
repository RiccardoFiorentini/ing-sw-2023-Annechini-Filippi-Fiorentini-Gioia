package main.java.it.polimi.ingsw.Connection;

import main.java.it.polimi.ingsw.Controller.Command;
import main.java.it.polimi.ingsw.Controller.Response;

import java.io.IOException;

public interface ServerConnectionHandler {
    /**
     * This (blocking) method returns the client command, when one is sent
     * @author Alessandro Annechini
     * @return The Command object sent from the client
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    public Command getNextCommand() throws IOException, ClassNotFoundException, InterruptedException;
    /**
     * This method sends the response to the client
     * @author Alessandro Annechini
     * @param response The response to be sent
     * @throws IOException
     */
    public void sendResponse(Response response) throws IOException;
}