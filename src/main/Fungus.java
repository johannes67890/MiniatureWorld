package main;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import java.util.Random;

public class Fungus implements Actor, DynamicDisplayInformationProvider {

  private int size = 1;
  private int reach = 3;
  private Carcass myCarcass = null;
  private Location location;

  public Fungus(Carcass carcass, World world) {
    this.myCarcass = carcass;
    this.location = world.getLocation(myCarcass);
  }

  public void act(World world) {
    //50% chance to decay its carcass
    if (new Random().nextBoolean() && myCarcass != null) {
      eat(world);
    }

    //if big enough spread
    if (size > 5) {
      spread(world);
    }

    //if its out in the world
    if (!world.)) {
      size--;
    }

    //if no say die
    if(size<=0){
        world.delete(this);
    }
  }

  public void spread(World world) {
    for (Location location : world.getSurroundingTiles(location, reach)) {
      if (
        world.getTile(location) != null &&
        world.getTile(location).getClass().equals(Carcass.class)
      ) {
        Carcass carcass = (Carcass) world.getTile(location);
        if (!carcass.hasFungus()) {
          carcass.addFungus(new Fungus(carcass, world), world);
          System.out.println("Fungus spread");
        }
      }
    }
  }

  private void eat(World world) {
    size += myCarcass.getEaten(1, world);
  }

  @Override
  public DisplayInformation getInformation() {
    if (size < 10) {
      return new DisplayInformation(java.awt.Color.blue, "fungi-small");
    } else {
      return new DisplayInformation(java.awt.Color.blue, "fungi");
    }
  }
}
