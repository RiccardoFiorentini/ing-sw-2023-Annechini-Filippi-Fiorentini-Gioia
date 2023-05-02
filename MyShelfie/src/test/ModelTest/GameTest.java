package test.ModelTest;

import main.java.it.polimi.ingsw.Model.Model;
import main.java.it.polimi.ingsw.Model.Player;
import main.java.it.polimi.ingsw.Model.Tile;
import main.java.it.polimi.ingsw.ModelExceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTest {
    Model model;
    List<Player> players;
    @BeforeEach
    public void setUp() throws NotToRefillException, WrongTurnException, IOException {
        players = new ArrayList<>();
        players.add(new Player("Pasquale", null));
        players.add(new Player("Riccardo", null));

        model=new Model(1, players);
    }

    @Test
    public void game_TwoPlayersGameSimulation_CorrectLogicExecution() throws Exception {

        players.get(model.getTurnId()).setSelectedColumn(1);
        players.get(model.getTurnId()).selectTile(4,1);
        players.get(model.getTurnId()).selectTile(5,1); //n
        players.get(model.getTurnId()).putInColumn(1);
        players.get(model.getTurnId()).putInColumn(0);

        players.get(model.getTurnId()).setSelectedColumn(3);
        players.get(model.getTurnId()).selectTile(5,2);
        players.get(model.getTurnId()).selectTile(3,2); //r
        players.get(model.getTurnId()).putInColumn(1);
        players.get(model.getTurnId()).putInColumn(2);
        players.get(model.getTurnId()).putInColumn(0);

        players.get(model.getTurnId()).setSelectedColumn(1);
        players.get(model.getTurnId()).selectTile(1,3);
        players.get(model.getTurnId()).selectTile(3,3); //n
        players.get(model.getTurnId()).putInColumn(2);
        players.get(model.getTurnId()).putInColumn(1);
        players.get(model.getTurnId()).putInColumn(0);

        players.get(model.getTurnId()).setSelectedColumn(1);
        players.get(model.getTurnId()).selectTile(5,3); //r
        players.get(model.getTurnId()).selectTile(6,3);
        players.get(model.getTurnId()).putInColumn(1);
        players.get(model.getTurnId()).putInColumn(0);

        players.get(model.getTurnId()).setSelectedColumn(1);
        players.get(model.getTurnId()).selectTile(3,6); //n
        players.get(model.getTurnId()).selectTile(3,6);
        players.get(model.getTurnId()).putInColumn(0);

        players.get(model.getTurnId()).setSelectedColumn(2);
        players.get(model.getTurnId()).selectTile(7,5); //r
        players.get(model.getTurnId()).selectTile(7,4);
        players.get(model.getTurnId()).putInColumn(0);
        players.get(model.getTurnId()).putInColumn(1);

        players.get(model.getTurnId()).setSelectedColumn(4);
        players.get(model.getTurnId()).selectTile(1,4);  //n
        players.get(model.getTurnId()).selectTile(2,4);
        players.get(model.getTurnId()).putInColumn(1);
        players.get(model.getTurnId()).putInColumn(0);

        players.get(model.getTurnId()).setSelectedColumn(4);
        players.get(model.getTurnId()).selectTile(6,4); //r
        players.get(model.getTurnId()).selectTile(6,5);
        players.get(model.getTurnId()).putInColumn(0);
        players.get(model.getTurnId()).putInColumn(1);

        players.get(model.getTurnId()).setSelectedColumn(3);
        players.get(model.getTurnId()).selectTile(5,6); //n
        players.get(model.getTurnId()).selectTile(5,5);
        players.get(model.getTurnId()).putInColumn(1);
        players.get(model.getTurnId()).putInColumn(0);

        players.get(model.getTurnId()).setSelectedColumn(4);
        players.get(model.getTurnId()).selectTile(4,7); //r
        players.get(model.getTurnId()).selectTile(4,6);
        players.get(model.getTurnId()).putInColumn(0);
        players.get(model.getTurnId()).putInColumn(1);

        players.get(model.getTurnId()).setSelectedColumn(4);
        players.get(model.getTurnId()).selectTile(2,5); //n
        players.get(model.getTurnId()).selectTile(3,5);
        players.get(model.getTurnId()).putInColumn(0);
        players.get(model.getTurnId()).putInColumn(1);

        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                assertTrue(model.getBoard().getTiles()[i][j]!=Tile.EMPTY);
            }
        }

        players.get(model.getTurnId()).setSelectedColumn(0);
        players.get(model.getTurnId()).selectTile(7,5);
        players.get(model.getTurnId()).selectTile(7,4); //r
        players.get(model.getTurnId()).putInColumn(1);
        players.get(model.getTurnId()).putInColumn(0);

        players.get(model.getTurnId()).setSelectedColumn(3);
        players.get(model.getTurnId()).selectTile(6,5); //n
        players.get(model.getTurnId()).selectTile(6,3);
        players.get(model.getTurnId()).putInColumn(0);
        players.get(model.getTurnId()).putInColumn(2);
        players.get(model.getTurnId()).putInColumn(1);

        players.get(model.getTurnId()).setSelectedColumn(3);
        players.get(model.getTurnId()).selectTile(5,3); //r
        players.get(model.getTurnId()).selectTile(5,1);
        players.get(model.getTurnId()).putInColumn(0);
        players.get(model.getTurnId()).putInColumn(1);
        players.get(model.getTurnId()).putInColumn(2);

        players.get(model.getTurnId()).setSelectedColumn(2);
        players.get(model.getTurnId()).selectTile(4,3); //n
        players.get(model.getTurnId()).selectTile(4,1);
        players.get(model.getTurnId()).putInColumn(1);
        players.get(model.getTurnId()).putInColumn(0);
        players.get(model.getTurnId()).putInColumn(2);

        players.get(model.getTurnId()).setSelectedColumn(0);
        players.get(model.getTurnId()).selectTile(5,4); //r
        players.get(model.getTurnId()).selectTile(5,6);
        players.get(model.getTurnId()).putInColumn(1);
        players.get(model.getTurnId()).putInColumn(2);
        players.get(model.getTurnId()).putInColumn(0);

        players.get(model.getTurnId()).setSelectedColumn(2);
        players.get(model.getTurnId()).selectTile(4,4); //n
        players.get(model.getTurnId()).selectTile(4,6);
        players.get(model.getTurnId()).putInColumn(1);
        players.get(model.getTurnId()).putInColumn(2);
        players.get(model.getTurnId()).putInColumn(0);

        players.get(model.getTurnId()).setSelectedColumn(4);
        players.get(model.getTurnId()).selectTile(3,7); //r
        players.get(model.getTurnId()).selectTile(4,7);
        players.get(model.getTurnId()).putInColumn(1);
        players.get(model.getTurnId()).putInColumn(0);

        players.get(model.getTurnId()).setSelectedColumn(4);
        players.get(model.getTurnId()).selectTile(3,5); //n
        players.get(model.getTurnId()).selectTile(3,6);
        players.get(model.getTurnId()).putInColumn(1);
        players.get(model.getTurnId()).putInColumn(0);

        players.get(model.getTurnId()).setSelectedColumn(1);
        players.get(model.getTurnId()).selectTile(1,3); //r
        players.get(model.getTurnId()).selectTile(3,3);
        players.get(model.getTurnId()).putInColumn(2);
        players.get(model.getTurnId()).putInColumn(1);
        players.get(model.getTurnId()).putInColumn(0);

        players.get(model.getTurnId()).setSelectedColumn(0);
        players.get(model.getTurnId()).selectTile(1,4); //n
        players.get(model.getTurnId()).selectTile(3,4);
        players.get(model.getTurnId()).putInColumn(0);
        players.get(model.getTurnId()).putInColumn(1);
        players.get(model.getTurnId()).putInColumn(2);

        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                assertTrue(model.getBoard().getTiles()[i][j]!=Tile.EMPTY);
            }
        }

        players.get(model.getTurnId()).setSelectedColumn(1);
        players.get(model.getTurnId()).selectTile(5,6); //r
        players.get(model.getTurnId()).selectTile(5,6);
        players.get(model.getTurnId()).putInColumn(0);

        players.get(model.getTurnId()).setSelectedColumn(0);
        players.get(model.getTurnId()).selectTile(7,4); //n
        players.get(model.getTurnId()).selectTile(7,4);
        players.get(model.getTurnId()).putInColumn(0);

        players.get(model.getTurnId()).setSelectedColumn(2);
        players.get(model.getTurnId()).selectTile(6,3); //r
        players.get(model.getTurnId()).selectTile(6,5);
        players.get(model.getTurnId()).putInColumn(0);
        players.get(model.getTurnId()).putInColumn(1);
        players.get(model.getTurnId()).putInColumn(2);

        players.get(model.getTurnId()).setSelectedColumn(0);
        players.get(model.getTurnId()).selectTile(5,4); //n
        players.get(model.getTurnId()).selectTile(5,5);
        players.get(model.getTurnId()).putInColumn(0);
        players.get(model.getTurnId()).putInColumn(1);

        players.get(model.getTurnId()).setSelectedColumn(2);
        players.get(model.getTurnId()).selectTile(5,2); //r
        players.get(model.getTurnId()).selectTile(5,2);
        players.get(model.getTurnId()).putInColumn(0);

        players.get(model.getTurnId()).setSelectedColumn(3);
        players.get(model.getTurnId()).selectTile(5,1); //n
        players.get(model.getTurnId()).selectTile(5,1);
        players.get(model.getTurnId()).putInColumn(0);


        assertTrue(model.getFirstToEnd()==model.getFirstToStart());

        players.get(model.getTurnId()).setSelectedColumn(0); //r
        players.get(model.getTurnId()).selectTile(1,3);
        players.get(model.getTurnId()).selectTile(1,3);
        players.get(model.getTurnId()).putInColumn(0);

        assertTrue(model.getTurnId()==-1);

        System.out.println(players.get(0).getNickname() + ": " + model.getFinalPoints().get(0));

        for(int i=0; i<6; i++){
            for(int j=0; j<5; j++){
                System.out.print(players.get(0).getShelf().getTiles()[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("Personal Goal: "+players.get(0).getPersonalGoal().getIndex());

        System.out.println(players.get(1).getNickname()+ ": " + model.getFinalPoints().get(1));

        for(int i=0; i<6; i++){
            for(int j=0; j<5; j++){
                System.out.print(players.get(1).getShelf().getTiles()[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("Personal Goal: "+players.get(1).getPersonalGoal().getIndex());

        System.out.println("CommonGoal1 =" +model.getCommonGoals()[0].getIndex());
        System.out.println("CommonGoal2 =" +model.getCommonGoals()[1].getIndex());
    }


}


