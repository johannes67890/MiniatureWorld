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
 * Represents a grass in the world.
 * Grass objects can spread to empty tiles in the world with a 10% chance during the day.
 * Grass can be eaten by {@link Rabbit}, and when eaten, they are deleted from the world and provide a food value of 3.
 * 
 * @implNote extends {@link Eatable} Implements {@link Actor}, {@link Nonblocking} and {@link DynamicDisplayInformationProvider}
 */
public class Grass extends Eatable implements Actor, DynamicDisplayInformationProvider, NonBlocking {

    /**
     * Causes the grass to act in the world.
     * This method is called by the world to perform actions for the grass object.
     * It calls the spread method to spread the grass to empty tiles.
     * 
     * @param world the world in which the grass exists
     */
    public void act(World world) {
       spread(world);
    }
    
    /**
     * Spreads the grass to empty tiles in the world.
     * This method is called by the act method and is used to spread the grass to empty tiles,
     * with a 10% chance of spreading during the day.
     * 
     * @param world the world in which the grass exists
     */
    private void spread(World world) {
         if (world.isDay() && new Random().nextInt(10) == 0) {
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
    }

    /**
     * Gets the food value when the grass is eaten.
     * This method is called when the grass is eaten by another actor.
     * It deletes the grass from the world and returns the food value of 3.
     * 
     * @param bideSize the size of the actor that is eating the grass
     * @param world the world in which the grass exists
     * @return the food value of the grass
     */
    public int getEaten(int bideSize, World world) {
        world.delete(this);
        return 3;
    }

    /**
     * Gets the display information for the grass.
     * This method is called to get the display information for the grass object,
     * including the color and name.
     * 
     * @return the display information for the grass
     */
    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(java.awt.Color.green, "grass");
    }
}
