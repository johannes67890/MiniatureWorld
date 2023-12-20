package main;

import java.util.Random;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

/**
 * Carcass is an object that represents a dead animal in the simulation world.
 * It can be eaten by other animals and can spawn {@link Fungus}.
 * 
 * @param mass - The mass of the carcass.
 * @param myFungus - The fungus that is growing inside the carcass.
 * @param hasFungus - True if the carcass has a fungus, false otherwise.
 * @param shouldHaveFungus - If the carcass should have fungus.
 * @implNote Implements {@link Actor} and {@link DynamicDisplayInformationProvider}
 */
public class Carcass extends Eatable implements Actor, DynamicDisplayInformationProvider {

  private int mass;
  private Fungus myFungus = null;
  private boolean hasFungus;
  private boolean shouldHaveFungus = false;

  /**
   * Constructs a Carcass object with the specified animal type and whether it should have a fungus.
   * The mass of the carcass is determined based on the animal type and whether it is an adult or not.
   * @param type the animal type of the carcass
   * @param shouldHaveFungus true if the carcass should have a fungus, false otherwise
   */
  public Carcass(Animal type, boolean shouldHaveFungus) {
    this.shouldHaveFungus = shouldHaveFungus;

    //
    //  Determine the mass of the carcass based on the animal type
    //
    if (type instanceof Rabbit) {
      if (type.isAdult) {
        mass = 5;
      } else {
        mass = 3;
      }
    }
    if (type instanceof Wolf) {
      if (type.isAdult) {
        mass = 10;
      } else {
        mass = 5;
      }
    }
    if (type instanceof Bear) {
      if (type.isAdult) {
        mass = 15;
      } else {
        mass = 8;
      }
    }
    if (type instanceof Snake) {
      if (type.isAdult) {
        mass = 6;
      } else {
        mass = 4;
      }
    }
  }

  /**
   * Performs the actions of the carcass in the simulation world.
   * This includes decaying, spawning fungus, and handling the carcass's death.
   * @param world the simulation world
   */
  public void act(World world) {
    if (shouldHaveFungus) {
      addFungus(world.getLocation(this), world);
      shouldHaveFungus = false;
    }

    // There's a 20% chance the carcass decays
    if (new Random().nextInt(5) == 0) {
      mass--;
    }

    // If no mass, decay totally
    if (mass <= 0) {
      Location deadLocation = world.getLocation(this);
      world.delete(this);
      if (myFungus != null) {
        world.setTile(deadLocation, myFungus);
      }
    }

    // Chance for a fungus to spawn inside the carcass
    if (!hasFungus && new Random().nextInt(5) == 0) {
      addFungus(world.getLocation(this), world);
      System.out.println("Fungus spawned in carcass");
    }
  }

  /**
   * Checks if the carcass has a fungus growing inside it.
   * @return true if the carcass has a fungus, false otherwise
   */
  public boolean hasFungus() {
    return hasFungus;
  }

  public int getMass() {
    return mass;
  }

  /**
   * Gets the amount of the carcass that is eaten by another animal.
   * Updates the mass of the carcass accordingly.
   * @param biteSize the size of the bite taken by the animal
   * @param world the simulation world
   * @return the actual amount of the carcass that is eaten
   */
  public int getEaten(int biteSize, World world) {
    int tempMass = mass;
    mass -= biteSize;
    if (tempMass - biteSize <= 0) {
      return tempMass;
    } else {
      return biteSize;
    }
  }

  /**
   * Adds a fungus to the carcass at the specified location in the simulation world.
   * @param location the location where the fungus should be added
   * @param world the simulation world
   */
  public void addFungus(Location location, World world) {
    myFungus = new Fungus(this, location);
    hasFungus = true;
    world.add(myFungus);
  }

  public Fungus getFungus() {
    return myFungus;
  }

  @Override
  public DisplayInformation getInformation() {
    if (mass <= 5) {
      return new DisplayInformation(java.awt.Color.black, "carcass-small");
    }
    return new DisplayInformation(java.awt.Color.black, "carcass");
  }
}