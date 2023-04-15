package test.ModelTest;

import main.java.it.polimi.ingsw.Model.Board;
import main.java.it.polimi.ingsw.Model.Tile;
import main.java.it.polimi.ingsw.ModelExceptions.NotPickableException;
import main.java.it.polimi.ingsw.ModelExceptions.NotToRefillException;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class BoardTest {
    Board board;

    @Test
    public void boardConstructor2Players() throws NotToRefillException, IOException {
        board = new Board(2);
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if      ((i<4 && j<3 && !(i==3 && j==2)) ||
                        (i<3 && j>4 && !(i==2 && j==5)) ||
                        (i>5 && j<4 && !(i==6 && j==3)) ||
                        (i>4 && j>5 && !(i==5 && j==6)) ||
                        (i==0 || i==8 || j==0 || j==8))

                    assertTrue(board.getTiles()[i][j] == Tile.BLOCKED);

                else
                    assertFalse(board.getTiles()[i][j].isFree());

            }
        }
    }
    @Test
    public void boardConstructor3Players() throws NotToRefillException, IOException {
        board = new Board(3);
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if      ((i<4 && j<3 && !(i==3 && j==2) && !(i==2 && j==2)) ||
                        (i<3 && j>4 && !(i==2 && j==5) && !(i==2 && j==6)) ||
                        (i>5 && j<4 && !(i==6 && j==3) && !(i==6 && j==2)) ||
                        (i>4 && j>5 && !(i==5 && j==6) && !(i==6 && j==6)) ||
                        (i==0 && j==4) || (i==4 && j==0) || (i==4 && j==8) || (i==8 && j==4))

                    assertTrue(board.getTiles()[i][j] == Tile.BLOCKED);

                else {
                    assertFalse(board.getTiles()[i][j].isFree());
                }
            }
        }
    }
    @Test
    public void boardConstructor4Players() throws NotToRefillException, IOException {
        board = new Board(4);
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if      (((i<4 && j<3 && !(i==3 && j==2) && !(i==2 && j==2) && !(i==3 && j==1)) ||
                        (i<3 && j>4 && !(i==2 && j==5) && !(i==2 && j==6) && !(i==1 && j==5)) ||
                        (i>5 && j<4 && !(i==6 && j==3) && !(i==6 && j==2) && !(i==7 && j==3)) ||
                        (i>4 && j>5 && !(i==5 && j==6) && !(i==6 && j==6)) && !(i==5 && j==7)))
                {
                    assertTrue(board.getTiles()[i][j] == Tile.BLOCKED);
                }
                else {
                    assertFalse(board.getTiles()[i][j].isFree());
                }
            }
        }
    }



    @Test
    public void pickTile() throws NotPickableException, NotToRefillException, IOException {
        board = new Board(2);
        Tile pickedTile = board.pickTile(3, 3);
        assertTrue(board.getTiles()[3][3] == Tile.EMPTY);
        assertFalse(pickedTile.isFree());

    }

    @Test
    public void callPickEmptyTileException() throws NotToRefillException, IOException, NotPickableException {
        board = new Board(2);
        Tile pickedTile = board.pickTile(3,3);

        try{
            pickedTile = board.pickTile(3,3);
            fail();
        } catch (NotPickableException e) {
        }
    }

    @Test
    public void callPickBlockedTileException() throws  NotToRefillException, IOException {
        board = new Board(2);
        Tile pickedTile;
        try{
            pickedTile = board.pickTile(2,2);
            fail();
        } catch (NotPickableException e){
        }
    }

    @Test
    public void checkToRefill() throws NotToRefillException, IOException, NotPickableException {
        Tile pickedTile;
        Board board2 = new Board(2);
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(!board2.getTiles()[i][j].isFree() && !(i==3 && j==3) && !(i==4 && j==4))
                        pickedTile = board2.pickTile(i, j);
            }
        }
        assertTrue(board2.checkFill());
    }

    @Test
    public void checkNotToRefill() throws NotToRefillException, IOException, NotPickableException{
        Tile pickedTile;
        Board board2 = new Board(2);

        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++)
                if(!board2.getTiles()[i][j].isFree() && !(i==3 && j==3) && !(i==3 && j==4))
                    pickedTile = board2.pickTile(i, j);
        }
        assertFalse(board2.checkFill());
    }

    @Test
    public void callRefillException() throws IOException, NotPickableException, NotToRefillException {
        board = new Board(2);
        Tile pickedTile;
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++)
                if(!board.getTiles()[i][j].isFree() && !(i==3 && j==3) && !(i==3 && j==4))
                    pickedTile = board.pickTile(i, j);
        }
        try{
            board.refill();
            fail();
        } catch (NotToRefillException e){
        }
    }

    @Test
    public void boardRefillAction() throws NotToRefillException, IOException, NotPickableException {
        Board boardGeneric = new Board(2);
        Tile pickedTile;

        int [][] t2_tmp_Board = new int[9][9];
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(!boardGeneric.getTiles()[i][j].isFree()) {
                    if (!(i == 3 && j == 3) && !(i == 4 && j == 4)) {
                        pickedTile = boardGeneric.pickTile(i, j);
                        t2_tmp_Board[i][j] = -1;
                    }
                    else
                        t2_tmp_Board[i][j] = 1;
                } else
                    t2_tmp_Board[i][j] = 0;
            }
        }

        Tile[] beforeRefillTiles = new Tile[2];
        beforeRefillTiles[0] = boardGeneric.getTiles()[3][3];
        beforeRefillTiles[1] = boardGeneric.getTiles()[4][4];

        boardGeneric.refill();

        assertTrue(boardGeneric.getTiles()[3][3] == beforeRefillTiles[0]);
        assertTrue(boardGeneric.getTiles()[4][4] == beforeRefillTiles[1]);

        for(int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                if(t2_tmp_Board[i][j]==-1)
                    assertFalse(boardGeneric.getTiles()[i][j].isFree());
            }
        }

    }



}
