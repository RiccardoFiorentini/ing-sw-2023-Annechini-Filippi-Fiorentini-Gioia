package it.polimi.ingsw.Connection;

import it.polimi.ingsw.Controller.Command;
import it.polimi.ingsw.Controller.Response;

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

    /**
     * This method ensures the termination of the connection between the client and the server
     * @author Alessandro Annechini
     */
    public void disconnect();
}