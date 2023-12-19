package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

/**
 * Grass class
 * 
 * @implNote Implements {@link Actor}, {@link Nonblocking} and {@link DynamicDisplayInformationProvider}
 */
public class Grass extends Eatable implements Actor, DynamicDisplayInformationProvider, NonBlocking {

    public void act(World world) {
        // if it is day, there is a 10% chance of spreading to other tiles
        if(new Random().nextInt(10) == 0 && world.isDay()){
            spread(world);
        }
    }
    
    /**
     * Spreads the grass
     * This method is called in {@link #act} and is used to spread the grass to empty tiles, with a 10% chance of spreading.
     */
    public void spread(World world) {
        List<Location> availableTiles = new ArrayList<>();
        for (Location location : world.getEmptySurroundingTiles()) {
            if (!(world.getTile(location) instanceof NonBlocking)) { // If the tile is not non-blocking (No two non-blocking tiles can be on to each other)
                availableTiles.add(location);
            }
        }
        if (availableTiles.size() > 0) { 
            Location place = availableTiles.get(new Random().nextInt(availableTiles.size()));
            world.setTile(place, new Grass());
        }
    }

    
    public int getEaten(int biteSize, World world) {
        world.delete(this);
        return 3;
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(java.awt.Color.green, "grass");
    }


}
