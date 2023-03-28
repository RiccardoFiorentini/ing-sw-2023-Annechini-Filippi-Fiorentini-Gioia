package main.java.it.polimi.ingsw.Model;

import java.util.List;

/**
 **/
public class Message {
    private final Player sender;
    private final List<Player> receiver;
    private final String text;

    /**
     * Class's constructor
     * @author Fiorentini Riccardo
     */
    public Message(Player sender, List<Player> receiver, String text){
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
    }

    public Player getSender() {
        return sender;
    }

    public List<Player> getReceiver() {
        return receiver;
    }

    public String getText() {
        return text;
    }
}
