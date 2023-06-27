package it.polimi.ingsw.Client;

import it.polimi.ingsw.Connection.ClientConnectionHandler;
import it.polimi.ingsw.Connection.ClientConnectionHandlerRMI;
import it.polimi.ingsw.Connection.ClientConnectionHandlerSocket;
import it.polimi.ingsw.Connection.RMIWelcomeServer;
import it.polimi.ingsw.Controller.Command;
import it.polimi.ingsw.Controller.CommandType;

import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client {
    ClientConnectionHandler cch;
    private static String serverIp;
    public static void main(String[] args) throws RemoteException {
        new Client().start();
    }

    /**
     * Method that is called at Client's start, asks the player which kind of connection and interface they want to use
     * @author Nicole Filippi
     */
    public void start() throws RemoteException {
        int interfaceType=0;
        Scanner scan = new Scanner(System.in);

        System.out.println("Insert server ip address:");
        try {
            serverIp = scan.nextLine();
        }
        catch(Exception e){
            serverIp="localhost";
        }

        while(interfaceType!=1 && interfaceType!=2){
            System.out.println("Select the interface: \n1. Command Line \n2. Graphic");
            try {
                interfaceType = scan.nextInt();
            }
            catch(Exception e){
                interfaceType=0;
            }
        }
        if(interfaceType==1){
            TUI view = new TUI();
            view.start();
        }else{
            GUIApplication.main(null);
        }
    }

    /**
     * Method that creates a connection after the player chooses the protocol
     * @author Nicole Filippi
     * @param type is the integer that indicates the chosen protocol
     * @return the correct implementation of ClientConnectionHandler
     */
    public static ClientConnectionHandler createConnection(int type) throws RemoteException {
        ClientConnectionHandler ret = null;
        switch (type){
            case 1:
                ClientConnectionHandlerRMI cch = new ClientConnectionHandlerRMI();
                try{
                    Registry registry = LocateRegistry.getRegistry(serverIp);
                    RMIWelcomeServer srv = (RMIWelcomeServer) registry.lookup("MyShelfieRMIServer");
                    srv.setConnection(cch);
                    ret=cch;
                }catch(Exception e){
                    return null;
                }
                break;
            case 2:
                try{
                    Socket socket = new Socket(serverIp, 54321);
                    ClientConnectionHandlerSocket cchs = new ClientConnectionHandlerSocket(socket);
                    ret=cchs;
                }catch(Exception e){
                    return null;
                }
                break;

        }

        final ClientConnectionHandler threadCch = ret;

        new Thread(()->{
            while(true){
                try{
                    TimeUnit.SECONDS.sleep(5);
                }catch(Exception e){}
                try {
                    threadCch.sendCommand(new Command(CommandType.PING));
                } catch (Exception e) {
                    System.exit(1);
                }
            }
        }).start();

        return ret;
    }
}
