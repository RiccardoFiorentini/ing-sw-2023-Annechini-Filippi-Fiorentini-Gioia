package main.java.it.polimi.ingsw.Client;

import javafx.application.Application;
import javafx.stage.Stage;
import main.java.it.polimi.ingsw.Connection.ClientConnectionHandler;
import main.java.it.polimi.ingsw.Controller.Command;
import main.java.it.polimi.ingsw.Controller.Response;


public class GUI extends View {

    /**
     * Class' constructor
     * @param cch is the ClientConnectionHandler associated
     * @author Nicole Filippi
     */
    public GUI(ClientConnectionHandler cch) {
        super(cch);
    }

    public static void main (String args[]){
        Graphics.main(args);
    }

    @Override
    public void handleResponse(Response resp) {

    }

    @Override
    public void onStartup() {

    }
}
