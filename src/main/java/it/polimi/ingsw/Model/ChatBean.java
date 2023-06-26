package it.polimi.ingsw.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChatBean implements Serializable {

    private List<String> sender;
    private List<String> text;
    private List<Boolean> privateMessage;

    /**
     * Class' constructor
     * @author Pasquale Gioia
     * @param messages is the list of messages
     * @param beanReceiver is the receiver of this bean
     */
    public ChatBean(List<Message> messages, Player beanReceiver){
        sender = new ArrayList<>();
        text = new ArrayList<>();
        privateMessage = new ArrayList<>();

        for (Message message : messages) {
            if (message.getReceiver().contains(beanReceiver) || message.getSender().equals(beanReceiver)) {

                if (message.getReceiver().size() == 1)//Private message
                    this.privateMessage.add(true);
                else //Broadcast message
                    this.privateMessage.add(false);


                this.sender.add(message.getSender().getNickname());
                this.text.add(message.getText());
            }
        }
    }

    public List<String> getSender() {
        return sender;
    }
    public List<String> getText(){ return text; }
    public List<Boolean> getPrivateMessage(){ return privateMessage; }

}
