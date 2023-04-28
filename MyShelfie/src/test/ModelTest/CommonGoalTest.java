package test.ModelTest;

import main.java.it.polimi.ingsw.Model.*;
import main.java.it.polimi.ingsw.ModelExceptions.FullColumnException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CommonGoalTest {
    Shelf shelf;
    CommonGoal cg;

    @BeforeEach
    public void setUp() {
        shelf = new Shelf();
    }

    @Test
    public void cg1_1() throws FullColumnException {
        cg=new CommonGoal1(4);
        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.ORANGE1,0);
        shelf.putTile(Tile.ORANGE1,0);
        shelf.putTile(Tile.ORANGE1,0);
        shelf.putTile(Tile.ORANGE1,0);
        shelf.putTile(Tile.ORANGE1,1);
        shelf.putTile(Tile.ORANGE1,1);
        shelf.putTile(Tile.ORANGE1,1);
        assertFalse(cg.check(shelf));

        shelf.putTile(Tile.BLUE2,2);
        shelf.putTile(Tile.BLUE2,2);
        shelf.putTile(Tile.BLUE2,2);
        shelf.putTile(Tile.BLUE2,2);
        shelf.putTile(Tile.BLUE2,3);
        shelf.putTile(Tile.BLUE2,3);
        shelf.putTile(Tile.BLUE2,3);
        assertFalse(cg.check(shelf));

        shelf.putTile(Tile.ORANGE1,1);
        assertTrue(cg.check(shelf));
    }

    @Test
    public void cg2_1() throws FullColumnException {
        cg=new CommonGoal2(4);

        assertFalse(cg.check(shelf));
        for(int i=0;i<3;i++){
            shelf.putTile(Tile.BLUE1,i);
            shelf.putTile(Tile.WHITE1,i);
            shelf.putTile(Tile.PINK1,i);
            shelf.putTile(Tile.CYAN1,i);
            shelf.putTile(Tile.GREEN1,i);
        }
        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.WHITE1,0);
        assertFalse(cg.check(shelf));

        shelf.putTile(Tile.ORANGE1,1);
        assertFalse(cg.check(shelf));

        shelf.putTile(Tile.ORANGE1,2);
        assertTrue(cg.check(shelf));
    }

    @Test
    public void cg3_1() throws FullColumnException{
        cg=new CommonGoal3(4);
        assertFalse(cg.check(shelf));
        for(int i=0;i<4;i++){
            shelf.putTile(Tile.WHITE1,i);
            shelf.putTile(Tile.WHITE2,i);
            shelf.putTile(Tile.WHITE3,i);
        }
        assertFalse(cg.check(shelf));

        shelf.putTile(Tile.WHITE1,0);
        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.WHITE1,1);
        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.WHITE1,2);
        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.WHITE1,3);
        assertTrue(cg.check(shelf));
    }

    @Test
    public void cg4_1() throws FullColumnException{
        cg=new CommonGoal4(4);
        assertFalse(cg.check(shelf));

        shelf.putTile(Tile.BLUE1,0);
        shelf.putTile(Tile.BLUE1,0);

        shelf.putTile(Tile.GREEN1,1);
        shelf.putTile(Tile.BLUE1,1);
        shelf.putTile(Tile.BLUE1,1);

        shelf.putTile(Tile.PINK1,2);
        shelf.putTile(Tile.PINK1,3);
        shelf.putTile(Tile.PINK1,3);

        shelf.putTile(Tile.GREEN1,4);
        shelf.putTile(Tile.PINK1,4);

        shelf.putTile(Tile.ORANGE1,2);
        shelf.putTile(Tile.ORANGE1,2);
        shelf.putTile(Tile.ORANGE1,2);

        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.ORANGE2,2);
        assertTrue(cg.check(shelf));
    }

    @Test
    public void cg5_1() throws FullColumnException{
        cg=new CommonGoal5(4);
        assertFalse(cg.check(shelf));
        for(int i=0;i<6;i++)
            shelf.putTile(Tile.BLUE1,0);
        assertFalse(cg.check(shelf));
        for(int i=0;i<6;i++)
            shelf.putTile(Tile.GREEN1,1);
        assertFalse(cg.check(shelf));

        shelf.putTile(Tile.BLUE1,2);
        shelf.putTile(Tile.WHITE1,2);
        shelf.putTile(Tile.GREEN1,2);
        shelf.putTile(Tile.GREEN1,2);
        shelf.putTile(Tile.GREEN1,2);
        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.ORANGE1,2);
        assertFalse(cg.check(shelf));

        shelf.putTile(Tile.BLUE1,3);
        shelf.putTile(Tile.WHITE1,3);
        shelf.putTile(Tile.GREEN1,3);
        shelf.putTile(Tile.GREEN1,3);
        shelf.putTile(Tile.GREEN1,3);
        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.BLUE1,3);
        assertTrue(cg.check(shelf));

    }

    @Test
    public void cg6_1() throws FullColumnException{
        cg = new CommonGoal6(4);
        assertFalse(cg.check(shelf));
        for(int i=0;i<3;i++) shelf.putTile(Tile.BLUE1,0);
        for(int i=0;i<3;i++) shelf.putTile(Tile.GREEN1,1);
        for(int i=0;i<3;i++) shelf.putTile(Tile.WHITE1,2);
        for(int i=0;i<3;i++) shelf.putTile(Tile.CYAN1,3);
        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.ORANGE1,4);
        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.CYAN1,4);
        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.ORANGE1,4);
        assertTrue(cg.check(shelf));
    }

    @Test
    public void cg7_1() throws FullColumnException{
        cg = new CommonGoal7(4);
        assertFalse(cg.check(shelf));
        for(int i=0;i<4;i++)
            shelf.putTile(Tile.PINK1,i);
        for(int i=0;i<4;i++)
            shelf.putTile(Tile.BLUE1,i);

        shelf.putTile(Tile.BLUE1,0);
        shelf.putTile(Tile.GREEN1,1);
        shelf.putTile(Tile.PINK1,2);
        shelf.putTile(Tile.BLUE1,3);

        for(int i=0;i<4;i++)
            shelf.putTile(Tile.WHITE1,i);
        for(int i=0;i<4;i++)
            shelf.putTile(Tile.GREEN1,i);

        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.PINK1,4);
        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.BLUE1,4);
        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.CYAN1,4);
        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.WHITE1,4);
        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.GREEN1,4);
        assertTrue(cg.check(shelf));
    }

    @Test
    public void cg8_1() throws FullColumnException{
        cg = new CommonGoal8(4);
        assertFalse(cg.check(shelf));
        for(int i=0;i<6;i++) shelf.putTile(Tile.BLUE1,0);
        for(int i=0;i<5;i++) shelf.putTile(Tile.BLUE1,4);
        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.GREEN1,4);
        assertFalse(cg.check(shelf));

        shelf=new Shelf();
        assertFalse(cg.check(shelf));
        for(int i=0;i<6;i++) shelf.putTile(Tile.BLUE1,0);
        for(int i=0;i<5;i++) shelf.putTile(Tile.BLUE1,4);
        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.BLUE1,4);
        assertTrue(cg.check(shelf));
    }

    @Test
    public void cg9_1() throws FullColumnException {
        cg=new CommonGoal9(4);
        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.GREEN1,0);
        shelf.putTile(Tile.GREEN1,0);
        shelf.putTile(Tile.GREEN1,0);
        shelf.putTile(Tile.GREEN1,0);
        shelf.putTile(Tile.GREEN1,0);
        shelf.putTile(Tile.GREEN1,0);
        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.GREEN2,1);
        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.BLUE2,1);
        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.GREEN2,4);
        assertTrue(cg.check(shelf));
    }

    @Test
    public void cg10_1() throws FullColumnException{
        cg = new CommonGoal10(4);
        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.GREEN1,0);
        shelf.putTile(Tile.GREEN1,0);
        shelf.putTile(Tile.GREEN1,0);
        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.ORANGE1,1);
        shelf.putTile(Tile.GREEN1,1);
        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.GREEN1,2);
        shelf.putTile(Tile.GREEN1,2);
        shelf.putTile(Tile.GREEN1,2);
        assertTrue(cg.check(shelf));
    }

    @Test
    public void cg11_1() throws FullColumnException{
        cg = new CommonGoal11(4);
        assertFalse(cg.check(shelf));
        for(int i=0;i<5;i++){
            for(int j=0;j<i;j++)
                shelf.putTile(Tile.GREEN1,i);
        }
        assertFalse(cg.check(shelf));
        for(int i=0;i<4;i++){
            shelf.putTile(Tile.ORANGE1,i);
        }
        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.WHITE1,4);
        assertFalse(cg.check(shelf));

        shelf=new Shelf();
        assertFalse(cg.check(shelf));
        for(int i=0;i<5;i++){
            for(int j=0;j<i;j++)
                shelf.putTile(Tile.GREEN1,i);
        }
        assertFalse(cg.check(shelf));
        for(int i=0;i<4;i++){
            shelf.putTile(Tile.ORANGE1,i);
        }
        assertFalse(cg.check(shelf));
        shelf.putTile(Tile.ORANGE2,4);
        assertTrue(cg.check(shelf));

    }

    @Test
    public void cg12_1() throws FullColumnException{
        cg = new CommonGoal12(4);
        assertFalse(cg.check(shelf));
        for(int i=0;i<5;i++){
            for(int j=0;j<i;j++)
                shelf.putTile(Tile.GREEN1,i);
        }
        assertFalse(cg.check(shelf));
        for(int i=0;i<5;i++){
            shelf.putTile(Tile.GREEN1,i);
        }
        assertTrue(cg.check(shelf));
        shelf.putTile(Tile.GREEN1,0);
        assertFalse(cg.check(shelf));
    }

    @Test
    public void pullPointsTest(){
        cg = new CommonGoal1(4);
        assertEquals(8,cg.getPointsLeft());
        assertEquals(8,cg.pullPoints());
        assertEquals(6,cg.getPointsLeft());
        assertEquals(6,cg.pullPoints());
        assertEquals(4,cg.getPointsLeft());
        assertEquals(4,cg.pullPoints());
        assertEquals(2,cg.getPointsLeft());
        assertEquals(2,cg.pullPoints());
        assertEquals(0,cg.getPointsLeft());

        cg = new CommonGoal1(3);
        assertEquals(8,cg.getPointsLeft());
        assertEquals(8,cg.pullPoints());
        assertEquals(6,cg.getPointsLeft());
        assertEquals(6,cg.pullPoints());
        assertEquals(4,cg.getPointsLeft());
        assertEquals(4,cg.pullPoints());
        assertEquals(0,cg.getPointsLeft());

        cg = new CommonGoal1(2);
        assertEquals(8,cg.getPointsLeft());
        assertEquals(8,cg.pullPoints());
        assertEquals(4,cg.getPointsLeft());
        assertEquals(4,cg.pullPoints());
        assertEquals(0,cg.getPointsLeft());
    }
}
