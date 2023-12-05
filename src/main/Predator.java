package main;

import itumulator.world.Location;
import itumulator.world.World;

public interface Predator {
    public abstract void attack(Location location, World world);
}