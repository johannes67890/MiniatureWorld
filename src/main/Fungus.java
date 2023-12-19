package main;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import java.util.Random;

/**
 * "Fungus" represents a fungus in carcasses in the world.
 * Fungus can decay carcasses, spread, and slowly die if left in the world.
 * 
 * @param size - The size of the fungus.
 * @param reach - The reach of the fungus.
 * @param myCarcass - The carcass associated with the fungus.
 * @param location - The location of the fungus in the world.
 * @implNote Implements {@link Actor} and {@link DynamicDisplayInformationProvider}
 */
public class Fungus implements Actor, DynamicDisplayInformationProvider {

  private int size = 1;
  private int reach = 3;
  private Carcass myCarcass;
  private Location location;

  /**
   * Constructs a new Fungus object with the given carcass and location.
   * @param carcass the carcass associated with the fungus
   * @param location the location of the fungus in the world
   */
  public Fungus(Carcass carcass, Location location) {
    this.myCarcass = carcass;
    this.location = location;
  }

  /**
   * Performs the actions of the fungus in the world.
   * @param world the world in which the fungus exists
   */
  public void act(World world) {
    // 50% chance to decay its carcass
    if (new Random().nextBoolean() && myCarcass != null) {
      size += myCarcass.getEaten(1, world);
    }

    // if big enough spread
    if (size > 5) {
      spread(world);
    }

    // if it's out in the world slowly die
    if (world.getTile(location) instanceof Fungus) {
      size--;
    }

    // if no size die
    if (size <= 0) {
      world.delete(this);
    }
  }

  /**
   * Spreads the fungus to nearby carcasses in the world.
   * @param world the world in which the fungus exists
   */
  private void spread(World world) {
    for (Location location : world.getSurroundingTiles(location, reach)) {
      if (
        world.getTile(location) != null &&
        world.getTile(location).getClass().equals(Carcass.class)
      ) {
        Carcass carcass = (Carcass) world.getTile(location);
        if (!carcass.hasFungus()) {
          carcass.addFungus(world.getLocation(carcass), world);
          System.out.println("Fungus spread");
        }
      }
    }
  }

  /**
   * Retrieves the display information for the fungus.
   * @return the display information for the fungus
   */
  @Override
  public DisplayInformation getInformation() {
    if (size < 10) {
      return new DisplayInformation(java.awt.Color.blue, "fungi-small");
    } else {
      return new DisplayInformation(java.awt.Color.blue, "fungi");
    }
  }
}