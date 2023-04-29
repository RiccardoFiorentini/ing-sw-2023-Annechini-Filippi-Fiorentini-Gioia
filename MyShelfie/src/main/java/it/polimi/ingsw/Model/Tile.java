package main.java.it.polimi.ingsw.Model;

import java.io.Serializable;

/**
 * @author Fiorentini Riccardo
 * */
public enum Tile implements Serializable {
    GREEN1(TileColor.GREEN),
    GREEN2(TileColor.GREEN),
    GREEN3(TileColor.GREEN),
    BLUE1(TileColor.BLUE),
    BLUE2(TileColor.BLUE),
    BLUE3(TileColor.BLUE),
    PINK1(TileColor.PINK),
    PINK2(TileColor.PINK),
    PINK3(TileColor.PINK),
    WHITE1(TileColor.WHITE),
    WHITE2(TileColor.WHITE),
    WHITE3(TileColor.WHITE),
    ORANGE1(TileColor.ORANGE),
    ORANGE2(TileColor.ORANGE),
    ORANGE3(TileColor.ORANGE),
    CYAN1(TileColor.CYAN),
    CYAN2(TileColor.CYAN),
    CYAN3(TileColor.CYAN),
    EMPTY(TileColor.EMPTY),
    BLOCKED(TileColor.BLOCKED);

    final TileColor color;

    Tile(TileColor color){
        this.color = color;
    }

    public TileColor getColor(){
        return color;
    }

    /**
     * This method checks if the two tiles are the same color
     * @author Alessandro Annechini
     * @param tile The other tile to check
     * @return True if the two tiles are the same color, false otherwise
     */
    boolean equals(Tile tile){
        return color==tile.getColor();
    }
    /**
     * This method checks if the tile is empty
     * @author Alessandro Annechini
     * @return True if the tile is empty, false otherwise
     */
    public boolean isFree(){
        return color==TileColor.EMPTY || color==TileColor.BLOCKED;
    }
}
