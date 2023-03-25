package main.java.it.polimi.ingsw.Model;

public class PersonalGoal5 implements PersonalGoal {

    public int getPoints(Shelf shelf) {
        int points = 0;
        int result = 0;
        // ([5,3], [3,1], [4,4], [3,2], [0,5], [1,1])

        if (shelf.getTile(5, 3) == Tile.GREEN1 ||
                shelf.getTile(5, 3) == Tile.GREEN2 ||
                shelf.getTile(5, 3) == Tile.GREEN3)
            points++;

        if (shelf.getTile(3, 1) == Tile.BLUE1 ||
                shelf.getTile(3, 1) == Tile.BLUE2 ||
                shelf.getTile(3, 1) == Tile.BLUE3)
            points++;


        if (shelf.getTile(4, 4) == Tile.PINK1 ||
                shelf.getTile(4, 4) == Tile.PINK2 ||
                shelf.getTile(4, 4) == Tile.PINK3)
            points++;


        if (shelf.getTile(3, 2) == Tile.WHITE1 ||
                shelf.getTile(3, 2) == Tile.WHITE2 ||
                shelf.getTile(3, 2) == Tile.WHITE3)
            points++;

        if (shelf.getTile(0, 5) == Tile.ORANGE1 ||
                shelf.getTile(0, 5) == Tile.ORANGE2 ||
                shelf.getTile(0, 5) == Tile.ORANGE3)
            points++;

        if (shelf.getTile(1, 1) == Tile.CYAN1 ||
                shelf.getTile(1, 1) == Tile.CYAN2 ||
                shelf.getTile(1, 1) == Tile.CYAN3)
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
