package main;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class SnakeEgg implements Actor, DynamicDisplayInformationProvider {

  private int age = 0;

  public void act(World world) {
    age++;
    if (age >= 15) {
      Location deadLocation = world.getLocation(this);
      world.delete(this);
      world.setTile(deadLocation, new Snake());
    }
  }

  @Override
  public DisplayInformation getInformation() {
    return new DisplayInformation(java.awt.Color.white, "snake-egg");
  }
}
