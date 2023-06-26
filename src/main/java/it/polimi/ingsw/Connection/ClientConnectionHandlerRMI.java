package it.polimi.ingsw.Connection;

import it.polimi.ingsw.Controller.Command;
import it.polimi.ingsw.Controller.Response;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ClientConnectionHandlerRMI extends UnicastRemoteObject implements ClientConnectionHandler, RMIClientConnection {

    @Serial
    private static final long serialVersionUID = 1L;
    private final List<Response> queue;
    private transient RMIServerConnection rmiServer;

    /**
     * Class' constructor
     * @author Alessandro Annechini
     */
    public ClientConnectionHandlerRMI() throws RemoteException {
        queue = new ArrayList<>();
    }

    public void setRMIServer(RMIServerConnection rmiServer) throws RemoteException {
        this.rmiServer = rmiServer;
    }

    public Response getNextResponse() throws InterruptedException {
        Response ret;

        /*
        This method waits until a Response is put
        in the queue by the sendResponse() method
         */

        synchronized (queue){
            while(queue.size() == 0) queue.wait();
            ret = queue.get(0);
            queue.remove(0);
        }
        return ret;
    }

    public void sendResponse(Response response) throws RemoteException {
        synchronized (queue) {
            queue.add(response);
            queue.notifyAll();
        }
    }

    public void sendCommand(Command command) throws RemoteException {
        rmiServer.sendCommand(command);
    }

    public void disconnect(){
        synchronized (queue){
            queue.add(null);
            queue.notifyAll();
        }
    }
}
