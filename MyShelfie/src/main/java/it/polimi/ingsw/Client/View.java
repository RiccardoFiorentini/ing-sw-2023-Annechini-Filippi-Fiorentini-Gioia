package main.java.it.polimi.ingsw.Client;

import main.java.it.polimi.ingsw.Connection.ClientConnectionHandler;
import main.java.it.polimi.ingsw.Controller.Command;
import main.java.it.polimi.ingsw.Controller.Response;

import java.util.Scanner;

public abstract class View {
    private ClientConnectionHandler cch;

    public View(ClientConnectionHandler cch){
        this.cch=cch;
    }
    public void start(){
        new Thread(()->handleEvent("start")).start();
        while(true){
            Response resp=null;
            try {
                resp = cch.getNextResponse();
            }catch(Exception e){
                e.printStackTrace();
            }

            final Response response=resp;

            if(response != null){
                new Thread(()->handleResponse(response)).start();
            }
        }
    }
    public abstract void handleResponse(Response resp);
    public void sendCommand(Command command){
        try{
            cch.sendCommand(command);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public abstract void handleEvent(String event);
}
