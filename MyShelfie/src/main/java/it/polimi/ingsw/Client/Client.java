package main.java.it.polimi.ingsw.Client;

import main.java.it.polimi.ingsw.Connection.ClientConnectionHandler;
import main.java.it.polimi.ingsw.Connection.ClientConnectionHandlerRMI;
import main.java.it.polimi.ingsw.Connection.ClientConnectionHandlerSocket;
import main.java.it.polimi.ingsw.Connection.RMIWelcomeServer;
import main.java.it.polimi.ingsw.Controller.Command;
import main.java.it.polimi.ingsw.Controller.CommandType;

import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client {
    ClientConnectionHandler cch;
    View view;

    public static void main(String[] args) throws RemoteException {
        new Client().start();
    }

    /**
     * Method that is called at Client's start, asks the player which kind of connection and interface they want to use
     * @author Nicole Filippi
     */
    public void start() throws RemoteException {
        int connectionType=0;
        int interfaceType=0;
        Scanner scan = new Scanner(System.in);

        while(connectionType!=1 && connectionType!=2){
            System.out.println("Select the connection: \n1. RMI \n2. Socket");
            try {
                connectionType = scan.nextInt();
            }
            catch(Exception e){
                connectionType=0;
            }
        }
        cch = createConnection(connectionType);
        final ClientConnectionHandler threadCch = cch;

        new Thread(()->{
            while(true){
                try{
                    TimeUnit.SECONDS.sleep(5);
                }catch(Exception e){}
                try {
                    threadCch.sendCommand(new Command(CommandType.PING));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        while(interfaceType!=1 && interfaceType!=2){
            System.out.println("Select the interface: \n1. Command Line \n2. Graphic");
            try {
                interfaceType = scan.nextInt();
            }
            catch(Exception e){
                interfaceType=0;
            }
        }
        view = createView(interfaceType);
        new Thread(()->view.start()).start();
    }

    /**
     * Method that creates a connection after the player chooses the protocol
     * @author Nicole Filippi
     * @param type is the integer that indicates the chosen protocol
     * @return the correct implementation of ClientConnectionHandler
     */
    private ClientConnectionHandler createConnection(int type) throws RemoteException {
        switch (type){
            case 1:
                ClientConnectionHandlerRMI cch = new ClientConnectionHandlerRMI();
                try{
                    Registry registry = LocateRegistry.getRegistry();
                    RMIWelcomeServer srv = (RMIWelcomeServer) registry.lookup("MyShelfieRMIServer");
                    srv.setConnection(cch);
                    return cch;
                }catch(Exception e){
                    return null;
                }
            case 2:
                try{
                    Socket socket = new Socket("localhost", 54321);
                    ClientConnectionHandlerSocket cchs = new ClientConnectionHandlerSocket(socket);
                    return cchs;
                }catch(Exception e){
                    return null;
                }
        }
        return null;
    }

    /**
     * Method that creates a view after the player chooses the interface
     * @author Nicole Filippi
     * @param type is the integer that indicates the chosen interface
     * @return the correct implementation of View
     */
    private View createView(int type){
        switch (type){
            case 1:
                return new TUI(cch);
            case 2:
                return new GUI(cch);
        }
        return null;
    }

    public View getView() {
        return view;
    }

}
