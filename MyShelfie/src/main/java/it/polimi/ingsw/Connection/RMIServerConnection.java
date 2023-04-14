package main.java.it.polimi.ingsw.Connection;

import main.java.it.polimi.ingsw.Controller.Command;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerConnection extends Remote {
    /**
     * Send a Command to the RMi server
     * @author Alessandro Annechini
     * @param command The command to be sent
     * @throws RemoteException
     */
    public void sendCommand(Command command) throws RemoteException;
}
