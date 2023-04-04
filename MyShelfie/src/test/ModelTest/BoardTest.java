package test.ModelTest;

import main.java.it.polimi.ingsw.Model.Board;
import main.java.it.polimi.ingsw.Model.Tile;
import main.java.it.polimi.ingsw.ModelExceptions.NotToRefillException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class BoardTest {

    @Test
    public void BoardConstructor2Players() throws NotToRefillException, IOException {
        Board b2 = new Board(2);
        assertTrue(b2.getTiles()[5][0]==Tile.BLOCKED &&
                b2.getTiles()[6][6]==Tile.BLOCKED &&
                b2.getTiles()[6][2]==Tile.BLOCKED &&
                b2.getTiles()[8][5]==Tile.BLOCKED &&
                b2.getTiles()[0][3]==Tile.BLOCKED &&
                b2.getTiles()[2][2]==Tile.BLOCKED &&
                b2.getTiles()[2][6]==Tile.BLOCKED &&
                b2.getTiles()[3][8]==Tile.BLOCKED &&


                b2.getTiles()[0][4]==Tile.BLOCKED &&
                b2.getTiles()[1][5]==Tile.BLOCKED &&
                b2.getTiles()[3][1]==Tile.BLOCKED &&
                b2.getTiles()[4][0]==Tile.BLOCKED &&
                b2.getTiles()[7][3]==Tile.BLOCKED &&
                b2.getTiles()[8][4]==Tile.BLOCKED &&
                b2.getTiles()[5][8]==Tile.BLOCKED &&
                b2.getTiles()[4][8]==Tile.BLOCKED
        );
    }
    @Test
    public void BoardConstructor3Players() throws NotToRefillException, IOException {
        Board b3 = new Board(3);
        assertFalse(b3.getTiles()[5][0].isFree());
        assertFalse(b3.getTiles()[6][6].isFree());
        assertFalse (b3.getTiles()[6][2].isFree()); 
        assertFalse (b3.getTiles()[8][5].isFree()); 
        assertFalse (b3.getTiles()[0][3].isFree()); 
        assertFalse (b3.getTiles()[2][2].isFree()); 
        assertFalse (b3.getTiles()[2][6].isFree()); 
        assertFalse (b3.getTiles()[3][8].isFree());
        assertSame(b3.getTiles()[0][4], Tile.BLOCKED);
        assertSame(b3.getTiles()[1][5], Tile.BLOCKED);
        assertSame(b3.getTiles()[3][1], Tile.BLOCKED);
        assertSame(b3.getTiles()[4][0], Tile.BLOCKED);
        assertSame(b3.getTiles()[7][3], Tile.BLOCKED);
        assertSame(b3.getTiles()[8][4], Tile.BLOCKED);
        assertSame(b3.getTiles()[5][8], Tile.BLOCKED);
        assertSame(b3.getTiles()[4][8], Tile.BLOCKED);

    }
    @Test
    public void BoardConstructor4Players() throws NotToRefillException, IOException {
        Board b4 = new Board(4);
        assertFalse(b4.getTiles()[5][0].isFree());
        assertFalse(b4.getTiles()[6][6].isFree());
        assertFalse(b4.getTiles()[6][2].isFree());
        assertFalse(b4.getTiles()[8][5].isFree());
        assertFalse(b4.getTiles()[0][3].isFree());
        assertFalse(b4.getTiles()[2][2].isFree());
        assertFalse(b4.getTiles()[2][6].isFree());
        assertFalse(b4.getTiles()[3][8].isFree());
        assertFalse(b4.getTiles()[0][4].isFree());
        assertFalse(b4.getTiles()[1][5].isFree());
        assertFalse(b4.getTiles()[3][1].isFree());
        assertFalse(b4.getTiles()[4][0].isFree());
        assertFalse(b4.getTiles()[7][3].isFree());
        assertFalse(b4.getTiles()[8][4].isFree());
        assertFalse(b4.getTiles()[5][7].isFree());
        assertFalse(b4.getTiles()[4][8].isFree());
    }

    @Test
    public void BoardRefill() throws NotToRefillException, IOException {
        for(int numPlayers=2; numPlayers<5; numPlayers++){
            Board board = new Board(numPlayers);
            switch(numPlayers){
                case 2:

                case 3:

                case 4:

            }


            //Tests if board is correctly refilled
        }
    }

    @Test
    public void CheckRefill(){
        //Tests if refill check works
    }

    @Test
    public void CheckRefillCornerCases(){
        //Tests refill corner cases
    }




}
