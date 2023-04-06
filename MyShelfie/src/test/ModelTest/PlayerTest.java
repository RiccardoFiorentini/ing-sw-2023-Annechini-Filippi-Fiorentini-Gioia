package test.ModelTest;

import main.java.it.polimi.ingsw.Model.Model;
import main.java.it.polimi.ingsw.Model.Player;
import main.java.it.polimi.ingsw.Model.Tile;
import main.java.it.polimi.ingsw.ModelExceptions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static main.java.it.polimi.ingsw.Model.Tile.EMPTY;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerTest {
    Model model;
    List<Player> AllPlayer;
    boolean[][] pickableTiles;

    @BeforeEach
    public void stetUp(){
        AllPlayer = new ArrayList<Player>();
        AllPlayer.add(new Player("Admin1"));
        AllPlayer.add(new Player("Admin2"));
        AllPlayer.add(new Player("Admin3"));
        AllPlayer.add(new Player("Admin4"));

        pickableTiles = new boolean[9][9];

        pickableTiles[0][3] = true;
        pickableTiles[0][4] = true;
        pickableTiles[1][3] = true;
        pickableTiles[1][5] = true;
        pickableTiles[2][2] = true;
        pickableTiles[2][6] = true;
        pickableTiles[3][1] = true;
        pickableTiles[3][7] = true;
        pickableTiles[3][8] = true;
        pickableTiles[4][0] = true;
        pickableTiles[4][8] = true;
        pickableTiles[5][0] = true;
        pickableTiles[5][1] = true;
        pickableTiles[5][7] = true;
        pickableTiles[6][2] = true;
        pickableTiles[6][6] = true;
        pickableTiles[7][3] = true;
        pickableTiles[7][5] = true;
        pickableTiles[8][4] = true;
        pickableTiles[8][5] = true;

        try {
            this.model = new Model(0, AllPlayer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NotToRefillException e) {
            throw new RuntimeException(e);
        } catch (WrongTurnException e) {
            throw new RuntimeException(e);
        }

        int i = 0;

        for (Player p: AllPlayer) {
            p.setModel(this.model);
            p.setTurnId(i);
            i++;
        }

    }

    @Test
    public void testBeginTurn(){

        try {
            AllPlayer.get(model.getTurnId()).beginTurn();
        } catch (WrongTurnException e) {
            throw new RuntimeException(e);
        }

        for(int i = 0; i < 9; i++){
            for(int j = 0; j<9; j++){
                assertSame(AllPlayer.get(model.getTurnId()).getPickableTiles()[i][j], pickableTiles[i][j]);
            }
        }

        assertTrue(AllPlayer.get(model.getTurnId()).getNumPickableTiles() == 0 &&
                AllPlayer.get(model.getTurnId()).getSelectedColumn() == -1 &&
                AllPlayer.get(model.getTurnId()).getX1() == -1 &&
                AllPlayer.get(model.getTurnId()).getX2() == -1 &&
                AllPlayer.get(model.getTurnId()).getY1() == -1 &&
                AllPlayer.get(model.getTurnId()).getY2() == -1);

        Tile [] buffer = new Tile[]{EMPTY, EMPTY, EMPTY};

        assertSame(AllPlayer.get(model.getTurnId()).getPickedTiles()[0], buffer[0]);
        assertSame(AllPlayer.get(model.getTurnId()).getPickedTiles()[1], buffer[1]);
        assertSame(AllPlayer.get(model.getTurnId()).getPickedTiles()[2], buffer[2]);
    }

    @Test
    public void testCorrectActions(){

        try {
            AllPlayer.get(model.getTurnId()).beginTurn();
        } catch (WrongTurnException e) {
            throw new RuntimeException(e);
        }

        for(int i = 0; i < 9; i++){
            for(int j = 0; j<9; j++){
                assertSame(AllPlayer.get(model.getTurnId()).getPickableTiles()[i][j], pickableTiles[i][j]);
            }
        }

        assertTrue(AllPlayer.get(model.getTurnId()).getNumPickableTiles() == 0 &&
                        AllPlayer.get(model.getTurnId()).getSelectedColumn() == -1 &&
                        AllPlayer.get(model.getTurnId()).getX1() == -1 &&
                        AllPlayer.get(model.getTurnId()).getX2() == -1 &&
                        AllPlayer.get(model.getTurnId()).getY1() == -1 &&
                        AllPlayer.get(model.getTurnId()).getY2() == -1);

        Tile [] buffer = new Tile[]{EMPTY, EMPTY, EMPTY};

        assertSame(AllPlayer.get(model.getTurnId()).getPickedTiles()[0], buffer[0]);
        assertSame(AllPlayer.get(model.getTurnId()).getPickedTiles()[1], buffer[1]);
        assertSame(AllPlayer.get(model.getTurnId()).getPickedTiles()[2], buffer[2]);

        try {
            AllPlayer.get(model.getTurnId()).setSelectedColumn(0);
        } catch (WrongTurnException e) {
            throw new RuntimeException(e);
        } catch (WrongPhaseException e) {
            throw new RuntimeException(e);
        }

        assertTrue(AllPlayer.get(model.getTurnId()).getSelectedColumn() == 0 &&
                        AllPlayer.get(model.getTurnId()).getNumPickableTiles() == 3);

        pickableTiles[1][5] = false;
        pickableTiles[2][2] = false;
        pickableTiles[2][6] = false;
        pickableTiles[3][1] = false;
        pickableTiles[3][7] = false;
        pickableTiles[3][8] = false;
        pickableTiles[4][0] = false;
        pickableTiles[4][8] = false;
        pickableTiles[5][0] = false;
        pickableTiles[5][1] = false;
        pickableTiles[5][7] = false;
        pickableTiles[6][2] = false;
        pickableTiles[6][6] = false;
        pickableTiles[7][3] = false;
        pickableTiles[7][5] = false;
        pickableTiles[8][4] = false;
        pickableTiles[8][5] = false;

        buffer[0] = this.model.getBoard().getTiles()[0][3];
        buffer[1] = this.model.getBoard().getTiles()[0][4];

        try {
            AllPlayer.get(model.getTurnId()).selectTile(0,3);

            for(int i = 0; i < 9; i++){
                for(int j = 0; j<9; j++){
                    assertSame(AllPlayer.get(model.getTurnId()).getPickableTiles()[i][j], pickableTiles[i][j]);
                }
            }

            assertTrue(AllPlayer.get(model.getTurnId()).getX1() == 0 &&
                    AllPlayer.get(model.getTurnId()).getY1() == 3);
            AllPlayer.get(model.getTurnId()).selectTile(0,4);

            assertSame(AllPlayer.get(model.getTurnId()).getPickedTiles()[0], buffer[0]);
            assertSame(AllPlayer.get(model.getTurnId()).getPickedTiles()[1], buffer[1]);
            assertSame(AllPlayer.get(model.getTurnId()).getPickedTiles()[2], buffer[2]);

        } catch (NotPickableException e) {
            throw new RuntimeException(e);
        } catch (WrongPhaseException e) {
            throw new RuntimeException(e);
        } catch (WrongTurnException e) {
            throw new RuntimeException(e);
        }

        try {

            AllPlayer.get(model.getTurnId()).putInColumn(0);
            AllPlayer.get(model.getTurnId()).putInColumn(1);

            //model.getTurnId()-1 works only if the current player is not player #0. it's just for testing
            assertTrue(AllPlayer.get(model.getTurnId()-1).getShelf().getTile(5, 0) == buffer[0] &&
                        AllPlayer.get(model.getTurnId()-1).getShelf().getTile(4, 0) == buffer[1]);

        } catch (FullColumnException e) {
            throw new RuntimeException(e);
        } catch (NotToRefillException e) {
            throw new RuntimeException(e);
        } catch (WrongTurnException e) {
            throw new RuntimeException(e);
        } catch (WrongPhaseException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testOneTile(){

        try {
            AllPlayer.get(model.getTurnId()).beginTurn();
        } catch (WrongTurnException e) {
            throw new RuntimeException(e);
        }

        for(int i = 0; i < 9; i++){
            for(int j = 0; j<9; j++){
                assertSame(AllPlayer.get(model.getTurnId()).getPickableTiles()[i][j], pickableTiles[i][j]);
            }
        }

        assertTrue(AllPlayer.get(model.getTurnId()).getNumPickableTiles() == 0 &&
                AllPlayer.get(model.getTurnId()).getSelectedColumn() == -1 &&
                AllPlayer.get(model.getTurnId()).getX1() == -1 &&
                AllPlayer.get(model.getTurnId()).getX2() == -1 &&
                AllPlayer.get(model.getTurnId()).getY1() == -1 &&
                AllPlayer.get(model.getTurnId()).getY2() == -1);

        Tile [] buffer = new Tile[]{EMPTY, EMPTY, EMPTY};

        assertSame(AllPlayer.get(model.getTurnId()).getPickedTiles()[0], buffer[0]);
        assertSame(AllPlayer.get(model.getTurnId()).getPickedTiles()[1], buffer[1]);
        assertSame(AllPlayer.get(model.getTurnId()).getPickedTiles()[2], buffer[2]);

        try {
            AllPlayer.get(model.getTurnId()).setSelectedColumn(0);
        } catch (WrongTurnException e) {
            throw new RuntimeException(e);
        } catch (WrongPhaseException e) {
            throw new RuntimeException(e);
        }

        assertTrue(AllPlayer.get(model.getTurnId()).getSelectedColumn() == 0 &&
                AllPlayer.get(model.getTurnId()).getNumPickableTiles() == 3);

        pickableTiles[0][3] = false;
        pickableTiles[0][4] = false;
        pickableTiles[1][3] = false;
        pickableTiles[1][5] = false;
        pickableTiles[2][2] = false;
        pickableTiles[2][6] = false;
        pickableTiles[3][1] = false;
        pickableTiles[3][7] = false;
        pickableTiles[4][0] = false;
        pickableTiles[5][0] = false;
        pickableTiles[5][1] = false;
        pickableTiles[5][7] = false;
        pickableTiles[6][2] = false;
        pickableTiles[6][6] = false;
        pickableTiles[7][3] = false;
        pickableTiles[7][5] = false;
        pickableTiles[8][4] = false;
        pickableTiles[8][5] = false;

        buffer[0] = this.model.getBoard().getTiles()[4][8];

        try {
            AllPlayer.get(model.getTurnId()).selectTile(4,8);

            for(int i = 0; i < 9; i++){
                for(int j = 0; j<9; j++){
                    assertSame(AllPlayer.get(model.getTurnId()).getPickableTiles()[i][j], pickableTiles[i][j]);
                }
            }

            assertTrue(AllPlayer.get(model.getTurnId()).getX1() == 4 &&
                    AllPlayer.get(model.getTurnId()).getY1() == 8);
            AllPlayer.get(model.getTurnId()).selectTile(4,8);

            assertSame(AllPlayer.get(model.getTurnId()).getPickedTiles()[0], buffer[0]);
            assertSame(AllPlayer.get(model.getTurnId()).getPickedTiles()[1], buffer[1]);
            assertSame(AllPlayer.get(model.getTurnId()).getPickedTiles()[2], buffer[2]);

        } catch (NotPickableException e) {
            throw new RuntimeException(e);
        } catch (WrongPhaseException e) {
            throw new RuntimeException(e);
        } catch (WrongTurnException e) {
            throw new RuntimeException(e);
        }

        try {

            AllPlayer.get(model.getTurnId()).putInColumn(0);

            //model.getTurnId()-1 works only if the current player is not player #0. it's just for testing
            assertTrue(AllPlayer.get(model.getTurnId()-1).getShelf().getTile(5, 0) == buffer[0]);

        } catch (FullColumnException e) {
            throw new RuntimeException(e);
        } catch (NotToRefillException e) {
            throw new RuntimeException(e);
        } catch (WrongTurnException e) {
            throw new RuntimeException(e);
        } catch (WrongPhaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testBeginTurn2() throws NotPickableException {

        model.getBoard().pickTile(0, 3);
        model.getBoard().pickTile(0, 4);
        model.getBoard().pickTile(7, 3);
        model.getBoard().pickTile(6, 3);
        model.getBoard().pickTile(5, 3);
        model.getBoard().pickTile(4, 3);

        pickableTiles[0][3] = false;
        pickableTiles[0][4] = false;
        pickableTiles[7][3] = false;
        pickableTiles[6][3] = false;
        pickableTiles[5][3] = false;
        pickableTiles[4][3] = false;

        pickableTiles[1][4] = true;
        pickableTiles[5][2] = true;
        pickableTiles[4][2] = true;
        pickableTiles[3][3] = true;
        pickableTiles[4][4] = true;
        pickableTiles[5][4] = true;
        pickableTiles[6][4] = true;
        pickableTiles[7][4] = true;


        try {
            AllPlayer.get(model.getTurnId()).beginTurn();
        } catch (WrongTurnException e) {
            throw new RuntimeException(e);
        }

        for(int i = 0; i < 9; i++){
            for(int j = 0; j<9; j++){
                assertSame(AllPlayer.get(model.getTurnId()).getPickableTiles()[i][j], pickableTiles[i][j]);
            }
        }

        assertTrue(AllPlayer.get(model.getTurnId()).getNumPickableTiles() == 0 &&
                AllPlayer.get(model.getTurnId()).getSelectedColumn() == -1 &&
                AllPlayer.get(model.getTurnId()).getX1() == -1 &&
                AllPlayer.get(model.getTurnId()).getX2() == -1 &&
                AllPlayer.get(model.getTurnId()).getY1() == -1 &&
                AllPlayer.get(model.getTurnId()).getY2() == -1);

        Tile [] buffer = new Tile[]{EMPTY, EMPTY, EMPTY};

        assertSame(AllPlayer.get(model.getTurnId()).getPickedTiles()[0], buffer[0]);
        assertSame(AllPlayer.get(model.getTurnId()).getPickedTiles()[1], buffer[1]);
        assertSame(AllPlayer.get(model.getTurnId()).getPickedTiles()[2], buffer[2]);
    }


}
