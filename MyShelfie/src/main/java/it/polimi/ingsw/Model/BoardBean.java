package main.java.it.polimi.ingsw.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BoardBean implements Serializable {
    private Map<Tile, Integer> tilesRemaining;
    private Tile[][] tiles;

    /**
     * Class' constructor
     * @author Pasquale Gioia
     * @param tilesRemaining is the bag
     * @param tiles is board's matrix
     */
    public BoardBean(Map<Tile, Integer> tilesRemaining, Tile[][] tiles){
        this.tilesRemaining = new HashMap<>(tilesRemaining);
        this.tiles = tiles.clone();
    }

    public Tile[][] getTiles(){ return tiles; }

    public Map<Tile, Integer> getTilesRemaining(){ return tilesRemaining; }

}

