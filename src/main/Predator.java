package main;

import itumulator.world.Location;
import itumulator.world.World;
/**
 * Predator interface
 * 
 * By implementing this interface, the class will be able to attack other objects.
 */
public interface Predator {
    public abstract void attack(Location location, World world);
}