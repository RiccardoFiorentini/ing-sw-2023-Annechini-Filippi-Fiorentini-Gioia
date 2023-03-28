package main.java.it.polimi.ingsw.Model;

public class PersonalGoal3 implements PersonalGoal {

    public int getPoints(Shelf shelf) {
        int points = 0;
        int result = 0;
        // ([3,1], [1,0], [2,2], [0,5], [1,3], [3,4])

        if (shelf.getTile(3, 1) == Tile.GREEN1 ||
                shelf.getTile(3, 1) == Tile.GREEN2 ||
                shelf.getTile(3, 1) == Tile.GREEN3)
            points++;

        if (shelf.getTile(1, 0) == Tile.BLUE1 ||
                shelf.getTile(1, 0) == Tile.BLUE2 ||
                shelf.getTile(1, 0) == Tile.BLUE3)
            points++;

        if (shelf.getTile(2, 2) == Tile.PINK1 ||
                shelf.getTile(2, 2) == Tile.PINK2 ||
                shelf.getTile(2, 2) == Tile.PINK3)
            points++;

        if (shelf.getTile(0, 5) == Tile.WHITE1 ||
                shelf.getTile(0, 5) == Tile.WHITE2 ||
                shelf.getTile(0, 5) == Tile.WHITE3)
            points++;

        if (shelf.getTile(1, 3) == Tile.ORANGE1 ||
                shelf.getTile(1, 3) == Tile.ORANGE2 ||
                shelf.getTile(1, 3) == Tile.ORANGE3)
            points++;

        if (shelf.getTile(3, 4) == Tile.CYAN1 ||
                shelf.getTile(3, 4) == Tile.CYAN2 ||
                shelf.getTile(3, 4) == Tile.CYAN3)
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