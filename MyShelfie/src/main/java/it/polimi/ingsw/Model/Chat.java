package main.java.it.polimi.ingsw.Model;

import main.java.it.polimi.ingsw.ModelExceptions.IncorrectMessageException;

import java.util.ArrayList;
import java.util.List;
public class Chat {
    private List<Message> messages;
    private List<Player> players;

    /**
     * Class' constructor
     * @author Nicole Filippi
     * @param players is the list of the players sent from the Model that creates the Chat
     */
    public Chat(List<Player> players){
        messages = new ArrayList<>();
        this.players = players;
    }

    /**
     * Method used to add a new message in the chat, the message is sent to one single player
     * @author Nicole Filippi
     * @param sender is the player who is sending the message
     * @param receiver is the player who receives the message
     * @param text is the body of the message
     * @throws IncorrectMessageException when the text of the message is empty
    */
    public void writeMessage(Player sender, Player receiver, String text) throws IncorrectMessageException {
        if(text.length()==0 || !players.contains(receiver))
            throw new IncorrectMessageException();
        List<Player> rec= new ArrayList<>();
        rec.add(receiver);
        Message m = new Message(sender, rec, text);
        messages.add(m);
    }

    /**
     * Method used to add a new broadcast message in the chat
     * @author Nicole Filippi
     * @param sender is the player who is sending the message
     * @param text is the body of the message
     * @throws IncorrectMessageException when the text of the message is empty
     */
    public void writeMessage(Player sender, String text) throws IncorrectMessageException{
        if(text.length()==0)
            throw new IncorrectMessageException();
        Message m = new Message(sender, players, text);
        messages.add(m);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public ChatBean toBean(Player beanReceiver){
        return new ChatBean(messages, beanReceiver);
    }

}
