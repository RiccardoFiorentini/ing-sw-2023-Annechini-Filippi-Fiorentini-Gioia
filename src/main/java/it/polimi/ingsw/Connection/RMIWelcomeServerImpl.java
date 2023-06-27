package it.polimi.ingsw.Connection;

import it.polimi.ingsw.Controller.Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIWelcomeServerImpl extends UnicastRemoteObject implements RMIWelcomeServer {
    private Server server;

    public RMIWelcomeServerImpl(Server server) throws RemoteException {
        this.server = server;
    }

    public void setConnection(RMIClientConnection rmiClient) throws RemoteException {
        ServerConnectionHandlerRMI rmiServer = null;
        rmiServer = new ServerConnectionHandlerRMI(rmiClient);
        VirtualView virtualView = new VirtualView(server);

        virtualView.setServerConnectionHandler(rmiServer);
        rmiClient.setRMIServer(rmiServer);
        server.addVirtualViewToList(virtualView);
        virtualView.start();
        System.out.println("New RMI client connected!");
    }
}
