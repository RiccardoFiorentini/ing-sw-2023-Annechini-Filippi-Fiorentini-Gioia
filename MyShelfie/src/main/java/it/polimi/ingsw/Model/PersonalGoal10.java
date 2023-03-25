package main.java.it.polimi.ingsw.Model;

public class PersonalGoal10 implements PersonalGoal {

    public int getPoints(Shelf shelf) {
        int points = 0;
        int result = 0;
        // ([3,3], [4,1], [5,3], [2,0], [1,1], [0,4])

        if (shelf.getTile(3, 3) == Tile.GREEN1 ||
                shelf.getTile(3, 3) == Tile.GREEN2 ||
                shelf.getTile(3, 3) == Tile.GREEN3)
            points++;

        if (shelf.getTile(4, 1) == Tile.BLUE1 ||
                shelf.getTile(4, 1) == Tile.BLUE2 ||
                shelf.getTile(4, 1) == Tile.BLUE3)
            points++;


        if (shelf.getTile(5, 3) == Tile.PINK1 ||
                shelf.getTile(5, 3) == Tile.PINK2 ||
                shelf.getTile(5, 3) == Tile.PINK3)
            points++;


        if (shelf.getTile(2, 0) == Tile.WHITE1 ||
                shelf.getTile(2, 0) == Tile.WHITE2 ||
                shelf.getTile(2, 0) == Tile.WHITE3)
            points++;

        if (shelf.getTile(1, 1) == Tile.ORANGE1 ||
                shelf.getTile(1, 1) == Tile.ORANGE2 ||
                shelf.getTile(1, 1) == Tile.ORANGE3)
            points++;

        if (shelf.getTile(0, 4) == Tile.CYAN1 ||
                shelf.getTile(0, 4) == Tile.CYAN2 ||
                shelf.getTile(0, 4) == Tile.CYAN3)
            points++;


        switch (points) {
            case 3:
                result = 4;
            case 4:
                result = 6;
            case 5:
                result = 9;
            case 6:
                result = 12;
            default:
                result = points;
        }

        return result;
    }
}
