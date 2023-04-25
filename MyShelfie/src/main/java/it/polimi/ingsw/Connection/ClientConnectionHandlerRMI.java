package main.java.it.polimi.ingsw.Connection;

import main.java.it.polimi.ingsw.Controller.Command;
import main.java.it.polimi.ingsw.Controller.Response;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ClientConnectionHandlerRMI implements ClientConnectionHandler, RMIClientConnection {
    final List<Response> queue;
    private RMIServerConnection rmiServer;

    /**
     * Class' constructor
     * @author Alessandro Annechini
     */
    public ClientConnectionHandlerRMI(){
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
