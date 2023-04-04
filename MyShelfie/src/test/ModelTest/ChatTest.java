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
        players.add(new Player("Nicole", null, 0));
        players.add(new Player("Alessandro", null, 1));
        players.add(new Player("Pasquale", null, 2));
        players.add(new Player("Riccardo", null, 3));
    }

    @BeforeEach
    public void setUp() throws Exception {
        chat = new Chat(players);
    }

    @Test
    public void newBroadcastMessage() throws IncorrectMessageException {
        assertTrue(chat.getMessages().size()==0);
        chat.writeMessage(players.get(0), "message");
        assertTrue(chat.getMessages().size()==1);
        assertTrue(chat.getMessages().get(0).getSender()==players.get(0));
        assertTrue(chat.getMessages().get(0).getText().equals("message"));
        assertTrue(chat.getMessages().get(0).getReceiver().equals(players));
    }

    @Test
    public void callExceptionBroadcast(){
        try{
            chat.writeMessage(players.get(0), "");
            fail();
        }catch(IncorrectMessageException e){
        }
    }

    @Test
    public void newMessage() throws IncorrectMessageException {
        assertTrue(chat.getMessages().size()==0);
        chat.writeMessage(players.get(0), players.get(1), "message");
        assertTrue(chat.getMessages().size()==1);
        assertTrue(chat.getMessages().get(0).getSender()==players.get(0));
        assertTrue(chat.getMessages().get(0).getReceiver().contains(players.get(1)) && chat.getMessages().get(0).getReceiver().size()==1);
        assertTrue(chat.getMessages().get(0).getText().equals("message"));
    }

    @Test
    public void callExceptionEmptyText(){
        try{
            chat.writeMessage(players.get(0), players.get(1), "");
            fail();
        }catch(IncorrectMessageException e){
        }
    }

    @Test
    public void callExceptionWrongReceiver(){
        try{
            chat.writeMessage(players.get(0), new Player("Pippo", null, 4), "message");
            fail();
        }catch(IncorrectMessageException e){
        }
    }

    @Test
    public void newMessages() throws IncorrectMessageException {
        assertTrue(chat.getMessages().size()==0);
        chat.writeMessage(players.get(0), players.get(1), "message");
        chat.writeMessage(players.get(1), "second message");
        assertTrue(chat.getMessages().size()==2);
        chat.writeMessage(players.get(2), players.get(3), "third message");
        chat.writeMessage(players.get(3), "fourth message");
        assertTrue(chat.getMessages().size()==4);
    }
}

