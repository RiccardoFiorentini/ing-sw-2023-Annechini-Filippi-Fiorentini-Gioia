package main.java.it.polimi.ingsw.Connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIWelcomeServer extends Remote {
    /**
     * This method instantiates the RMI server corresponding to the
     * rmiClient callee, al well as the VirtualView.
     * @param rmiClient
     * @throws RemoteException
     */
    public void setConnection(RMIClientConnection rmiClient) throws RemoteException;
}
