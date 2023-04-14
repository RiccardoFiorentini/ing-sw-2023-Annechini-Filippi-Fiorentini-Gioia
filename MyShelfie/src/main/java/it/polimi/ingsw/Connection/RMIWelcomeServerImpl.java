package main.java.it.polimi.ingsw.Connection;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIWelcomeServerImpl extends UnicastRemoteObject implements RMIWelcomeServer {

    public RMIWelcomeServerImpl() throws RemoteException {
    }

    public void setConnection(RMIClientConnection rmiClient) throws RemoteException {
        /*
        This method instantiates the RMI connection between client and server
        in this method, this lines will be present:

            ...
            ServerConnectionHandlerRMI rmiServer = new ServerConnectionHandlerRMI(rmiClient);
            rmiClient.setRMIServer(rmiServer);
            ...

         */

    }
}
