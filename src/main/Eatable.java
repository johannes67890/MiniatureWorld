package main;

import itumulator.world.World;
/**
 * "Eatable" represents an object that can be eaten by an animal.
 */
abstract public class Eatable {
    abstract protected int getEaten(int biteSize, World world);
}
