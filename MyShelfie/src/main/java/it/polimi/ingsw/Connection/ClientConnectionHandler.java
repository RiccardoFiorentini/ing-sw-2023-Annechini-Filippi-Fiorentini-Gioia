package main.java.it.polimi.ingsw.Connection;

import main.java.it.polimi.ingsw.Controller.Command;
import main.java.it.polimi.ingsw.Controller.Response;

import java.io.IOException;

public interface ClientConnectionHandler {
    /**
     * This (blocking) method returns the server response, when one is sent
     * @author Alessandro Annechini
     * @return The Response object sent from the server
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    public Response getNextResponse() throws IOException, ClassNotFoundException, InterruptedException;

    /**
     * This method sends the command to the server
     * @author Alessandro Annechini
     * @param command The command to be sent
     * @throws IOException
     */
    public void sendCommand(Command command) throws IOException;
}