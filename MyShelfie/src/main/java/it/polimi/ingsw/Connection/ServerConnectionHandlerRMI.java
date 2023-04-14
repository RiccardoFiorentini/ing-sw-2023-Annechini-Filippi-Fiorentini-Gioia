package main.java.it.polimi.ingsw.Connection;

import main.java.it.polimi.ingsw.Controller.Command;
import main.java.it.polimi.ingsw.Controller.Response;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ServerConnectionHandlerRMI implements ServerConnectionHandler, RMIServerConnection {
    final List<Command> queue;
    private RMIClientConnection rmiClient;

    /**
     * Class' constructor
     * @author Alessandro Annechini
     * @param rmiClient The RMI client corresponding to this instance of RMI server
     */

    public ServerConnectionHandlerRMI(RMIClientConnection rmiClient){
        queue = new ArrayList<>();
        this.rmiClient = rmiClient;
    }

    public Command getNextCommand() throws InterruptedException {
        Command ret;

        /*
        This method waits until a Command is put
        in the queue by the sendCommand() method
         */

        synchronized (queue){
            while(queue.size() == 0) queue.wait();
            ret = queue.get(0);
            queue.remove(0);
        }
        return ret;
    }

    public void sendResponse(Response response) throws RemoteException {
        rmiClient.sendResponse(response);
    }

    public void sendCommand(Command command) throws RemoteException {
        synchronized (queue){
            queue.add(command);
            queue.notifyAll();
        }
    }
}
