package main.java.it.polimi.ingsw.Client;

import javafx.application.Application;
import javafx.stage.Stage;
import main.java.it.polimi.ingsw.Connection.ClientConnectionHandler;
import main.java.it.polimi.ingsw.Controller.Command;
import main.java.it.polimi.ingsw.Controller.Response;


public class GUI extends Application implements View {
    private final ClientConnectionHandler cch;

    /**
     * Class' constructor
     * @author Nicole Filippi
     * @param cch is the ClientConnectionHandler associated
     */
    public GUI(ClientConnectionHandler cch) {
        this.cch=cch;
    }

    public void start(){
        //TODO onstartup
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

    public void sendCommand(Command command){
        try{
            cch.sendCommand(command);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void handleResponse(Response resp) {

    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}
