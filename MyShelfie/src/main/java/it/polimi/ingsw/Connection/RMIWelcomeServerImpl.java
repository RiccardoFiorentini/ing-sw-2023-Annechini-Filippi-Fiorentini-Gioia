package main.java.it.polimi.ingsw.Connection;

import main.java.it.polimi.ingsw.Controller.Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIWelcomeServerImpl extends UnicastRemoteObject implements RMIWelcomeServer {
    private Server server;

    public RMIWelcomeServerImpl(Server server) throws RemoteException {
        this.server = server;
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
