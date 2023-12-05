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
public class Grass implements Actor, DynamicDisplayInformationProvider, NonBlocking {
    Random r = new Random(); // Random number generator

    public void act(World world) {
       spread(world);
    }
    
    /**
     * Spreads the grass
     * This method is called in {@link #act} and is used to spread the grass to empty tiles, with a 10% chance of spreading.
     */
    void spread(World world) {
         if (world.isDay() && r.nextInt(10) == 0) {
            List<Location> availableTiles = new ArrayList<>();
            for (Location l : world.getEmptySurroundingTiles()) {
                if (!(world.getTile(l) instanceof NonBlocking)) { // If the tile is not non-blocking (No two non-blocking tiles can be on to each other)
                    availableTiles.add(l);
                }
            }
            if (availableTiles.size() > 0) { 
                Location place = availableTiles.get(r.nextInt(availableTiles.size()));
                world.setTile(place, new Grass());
            }
        }
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(java.awt.Color.green, "grass");
    }
}
