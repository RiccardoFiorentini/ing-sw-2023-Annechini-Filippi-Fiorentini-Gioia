package test.ModelTest;

import main.java.it.polimi.ingsw.Model.Shelf;
import main.java.it.polimi.ingsw.Model.Tile;
import main.java.it.polimi.ingsw.ModelExceptions.FullColumnException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ShelfTest {
    Shelf shelf;

    @BeforeEach
    public void setUp() {
        shelf = new Shelf();
    }

    @Test
    public void emptyShelf() {
        assertEquals(0,shelf.getGroupsPoints());
    }

    @Test
    public void shelfPoints1() throws FullColumnException {
        shelf.putTile(Tile.BLUE1,0);
        shelf.putTile(Tile.BLUE1,0);
        shelf.putTile(Tile.BLUE1,0);

        shelf.putTile(Tile.GREEN1,0);
        shelf.putTile(Tile.GREEN1,0);
        shelf.putTile(Tile.GREEN1,0);

        shelf.putTile(Tile.BLUE1,1);
        shelf.putTile(Tile.BLUE1,1);

        assertEquals(7,shelf.getGroupsPoints());
    }

    @Test
    public void shelfPoints2() throws FullColumnException {
        shelf.putTile(Tile.BLUE1,0);
        shelf.putTile(Tile.BLUE1,0);
        shelf.putTile(Tile.BLUE1,0);
        shelf.putTile(Tile.BLUE1,0);
        shelf.putTile(Tile.BLUE1,0);
        shelf.putTile(Tile.BLUE1,0);
        assertEquals(8,shelf.getGroupsPoints());
        shelf.putTile(Tile.BLUE1,1);
        assertEquals(8,shelf.getGroupsPoints());
    }

    @Test
    public void fullColTest() throws FullColumnException{
        shelf.putTile(Tile.PINK1,0);
        shelf.putTile(Tile.PINK1,0);
        shelf.putTile(Tile.PINK1,0);
        shelf.putTile(Tile.PINK1,0);
        shelf.putTile(Tile.PINK1,0);
        shelf.putTile(Tile.PINK1,0);
        try{
            shelf.putTile(Tile.PINK1,0);
            fail();
        } catch(FullColumnException e){

        }
    }

}
