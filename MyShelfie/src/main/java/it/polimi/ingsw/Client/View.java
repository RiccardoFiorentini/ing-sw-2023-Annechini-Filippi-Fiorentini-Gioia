package main.java.it.polimi.ingsw.Client;

import main.java.it.polimi.ingsw.Connection.ClientConnectionHandler;
import main.java.it.polimi.ingsw.Controller.Command;
import main.java.it.polimi.ingsw.Controller.Response;


public abstract class View {
    private final ClientConnectionHandler cch;

    /**
     * Class' constructor
     * @author Nicole Filippi
     * @param cch is the ClientConnectionHandler associated
     */
    public View(ClientConnectionHandler cch){
        this.cch=cch;
    }

    /**
     * Starts the View of the game, creates two thread: the one that handles the Ping
     * to the server and the one that starts the view.
     * @author Nicole Filippi
     */
    public void start(){
        new Thread(()->onStartup()).start();
        while(true){
            Response resp=null;
            try {
                resp = cch.getNextResponse();
            }catch(Exception e){
                //e.printStackTrace();
            }

            final Response response=resp;

            if(response != null){
                new Thread(()->handleResponse(response)).start();
            }
        }
    }

    /**
     * Method that handles the responses sent by the server to the client
     * @author Nicole Filippi
     * @param resp is the response that needs to be handled
     */
    public abstract void handleResponse(Response resp);

    /**
     * Method that sends a given command to the server
     * @author Nicole Filippi
     * @param command is the command that has to be sent
     */
    public void sendCommand(Command command){
        try{
            cch.sendCommand(command);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method that handles the startup of the particular view
     * @author Nicole Filippi
     */
    public abstract void onStartup();
}
