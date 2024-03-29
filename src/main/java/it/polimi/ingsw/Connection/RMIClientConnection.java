package it.polimi.ingsw.Connection;

import it.polimi.ingsw.Controller.Response;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIClientConnection extends Remote {
    /**
     * Send a Response to the RMi client
     * @author Alessandro Annechini
     * @param response The response to be sent
     */
    void sendResponse(Response response) throws RemoteException;

    /**
     * Set the corresponding RMI server in order to link the ConnectionHandler
     * of the client to the one on the server
     * @author Alessandro Annechini
     * @param rmiServer The RMI server corresponding to the client
     */
    void setRMIServer(RMIServerConnection rmiServer) throws RemoteException;
}
