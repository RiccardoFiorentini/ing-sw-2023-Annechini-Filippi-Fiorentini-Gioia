package test.ModelTest;
import main.java.it.polimi.ingsw.Model.Model;
import main.java.it.polimi.ingsw.Model.Player;
import main.java.it.polimi.ingsw.ModelExceptions.NotToRefillException;
import main.java.it.polimi.ingsw.ModelExceptions.WrongTurnException;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {
    Model model;
    static List<Player> players;

    @BeforeAll
    public static void setUpClass(){
        players=new ArrayList<>();
        players.add(new Player("Nicole", null));
        players.add(new Player("Alessandro", null));
        players.add(new Player("Pasquale", null));
        players.add(new Player("Riccardo", null));
    }

    @BeforeEach
    public void setUp() throws IOException, NotToRefillException, WrongTurnException {
        model = new Model(1, players);
    }

    @Test
    public void modelConstructor_CorrectParameters_CorrectModelInitialization() throws NotToRefillException, WrongTurnException, IOException {

        for(int k=0; k<20; k++){
            model=new Model(k, players);
            assertTrue(model.getFirstToStart()>=0 && model.getFirstToStart()<=4);

            for(int i=0; i<4; i++){
                assertTrue(model.getPlayers().get(i).getPersonalGoal()!=null);
                for(int j=0; j<i; j++){
                    assertTrue(model.getPlayers().get(i).getPersonalGoal().getIndex()!=model.getPlayers().get(j).getPersonalGoal().getIndex());
                }
            }
            assertTrue(model.getCommonGoals()[0]!=null);
            assertTrue(model.getCommonGoals()[1]!=null);
            assertTrue(model.getCommonGoals()[0].getIndex()!=model.getCommonGoals()[1].getIndex());

        }

    }

    @Test
    public void nextTurn_CustomTurnPhase_NextTurnHandledCorrectly() throws NotToRefillException, WrongTurnException {
        for(int i=0; i<8; i++){
            int turn1 = model.getTurnId();
            model.nextTurn();
            int turn2 = model.getTurnId();
            assertTrue(turn2==(turn1+1)%4);
            assertTrue(model.getFirstToEnd()==-1);
        }

    }

}
