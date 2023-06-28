package it.polimi.ingsw.Connection;

import it.polimi.ingsw.Controller.Command;
import it.polimi.ingsw.Controller.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnectionHandlerSocket implements ClientConnectionHandler {

    private Socket socket;
    private final ObjectInputStream ois;
    private final ObjectOutputStream oos;

    /**
     * Class' constructor
     * @author Alessandro Annechini
     * @param socket The socket connecting this node to the server
     */
    public ClientConnectionHandlerSocket(Socket socket) throws IOException {
        this.socket=socket;
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
    }

    public Response getNextResponse() throws IOException, ClassNotFoundException {
        Response ret = (Response) ois.readObject();
        return ret;
    }

    public synchronized void sendCommand(Command command) throws IOException {
        oos.reset();
        oos.writeObject(command);
        oos.flush();
    }

    public void disconnect(){
        if(socket!=null){
            try{
                socket.close();
                socket = null;
            } catch(IOException e){
                socket = null;
            }
        }
    }
}
