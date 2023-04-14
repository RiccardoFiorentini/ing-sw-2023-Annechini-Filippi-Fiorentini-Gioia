package main.java.it.polimi.ingsw.Connection;

import main.java.it.polimi.ingsw.Controller.Command;
import main.java.it.polimi.ingsw.Controller.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnectionHandlerSocket implements ServerConnectionHandler {

    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    /**
     * Class' constructor
     * @author Alessandro Annechini
     * @param socket The socket connecting the server to the current client
     * @throws IOException
     */
    public ServerConnectionHandlerSocket(Socket socket) throws IOException {
        this.socket=socket;
        ois = new ObjectInputStream(socket.getInputStream());
        oos = new ObjectOutputStream(socket.getOutputStream());
    }

    public Command getNextCommand() throws IOException, ClassNotFoundException {
        Command ret = (Command) ois.readObject();
        return ret;
    }

    public void sendResponse(Response response) throws IOException {
        oos.writeObject(response);
    }

}
