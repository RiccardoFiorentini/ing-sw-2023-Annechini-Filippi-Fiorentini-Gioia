package test.ModelTest;
import main.java.it.polimi.ingsw.ModelExceptions.IncorrectMessageException;
import main.java.it.polimi.ingsw.Model.*;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChatTest {
    Chat chat;
    static List<Player> players;
    @BeforeAll
    public static void setUpClass(){
        players=new ArrayList<>();
        players.add(new Player("Nicole", null));
        players.add(new Player("Alessandro", null));
        players.add(new Player("Pasquale", null));
        players.add(new Player("Riccardo", null));
        for(int i=0; i<4; i++){
            players.get(i).setTurnId(i);
        }
    }

    @BeforeEach
    public void setUp(){
        chat = new Chat(players);
    }

    @Test
    public void writeMessageBroadcast_CorrectMessage_CorrectExecution() throws IncorrectMessageException {
        assertTrue(chat.getMessages().size()==0);
        chat.writeMessage(players.get(0), "message");
        assertTrue(chat.getMessages().size()==1);
        assertTrue(chat.getMessages().get(0).getSender()==players.get(0));
        assertTrue(chat.getMessages().get(0).getText().equals("message"));
        assertTrue(chat.getMessages().get(0).getReceiver().equals(players));
    }

    @Test
    public void emptyBroadcastException_EmptyMessage_RaiseIncorrectMessageException(){
        try{
            chat.writeMessage(players.get(0), "");
            fail();
        }catch(IncorrectMessageException e){
        }
    }

    @Test
    public void writeMessage_CorrectMessageFormat_CorrectExecution() throws IncorrectMessageException {
        assertTrue(chat.getMessages().size()==0);
        chat.writeMessage(players.get(0), players.get(1), "message");
        assertTrue(chat.getMessages().size()==1);
        assertTrue(chat.getMessages().get(0).getSender()==players.get(0));
        assertTrue(chat.getMessages().get(0).getReceiver().contains(players.get(1)) && chat.getMessages().get(0).getReceiver().size()==1);
        assertTrue(chat.getMessages().get(0).getText().equals("message"));
    }

    @Test
    public void emptySingleException_EmptyMessage_RaiseIncorrectMessageException(){
        try{
            chat.writeMessage(players.get(0), players.get(1), "");
            fail();
        }catch(IncorrectMessageException e){
        }
    }

    @Test
    public void wrongReceiverException_InvalidReceiver_RaiseIncorrectMessageException(){
        try{
            chat.writeMessage(players.get(0), new Player("Pippo", null), "message");
            fail();
        }catch(IncorrectMessageException e){
        }
    }

    @Test
    public void writeBroadcastMessage_CorrectMessageFormat_CorrectExecution() throws IncorrectMessageException {
        assertTrue(chat.getMessages().size()==0);
        chat.writeMessage(players.get(0), players.get(1), "message");
        chat.writeMessage(players.get(1), "second message");
        assertTrue(chat.getMessages().size()==2);
        chat.writeMessage(players.get(2), players.get(3), "third message");
        chat.writeMessage(players.get(3), "fourth message");
        assertTrue(chat.getMessages().size()==4);
    }
}

