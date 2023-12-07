package main;

import itumulator.world.World;

abstract public class Eatable {
    abstract protected int getEaten(int biteSize, World world);
}
