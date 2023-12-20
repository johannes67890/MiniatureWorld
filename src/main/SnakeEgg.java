package main;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

/**
 * SnakeEgg gets laid by {@link Snake} and hatch into a new {@link Snake}.
 * @implNote Implements {@link Actor}, {@link Nonblocking} and {@link DynamicDisplayInformationProvider}
 */
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
