package main;

import java.util.Random;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Carcass
  extends Eatable
  implements Actor, DynamicDisplayInformationProvider {

  private int mass;
  private Fungus myFungus = null;
  private boolean hasFungus;
  private boolean shouldHaveFungus = false;

  Carcass(boolean shouldHaveFungus, Animal type) {
    this.shouldHaveFungus = shouldHaveFungus;

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

  public void act(World world) {
if(shouldHaveFungus){
      addFungus(world);
      shouldHaveFungus = false;
    }



    //theres a 20% chance the carcass decays
    if(new Random().nextInt(5)==0){
        mass--;
    }

    //if no mass decay totally
    if (mass <= 0) {
      Location deadLocation = world.getLocation(this);
      world.delete(this);
      if (myFungus != null) {
        world.setTile(deadLocation, myFungus);
      }
    }

    //change for a fungus to spawn inside carcass
    if(!hasFungus && new Random().nextInt(5)==0){
        addFungus(world);
        System.out.println("Fungus spawned in carcass");
    }
  }

  public boolean hasFungus(){
    return hasFungus;
  }

  public int getEaten(int biteSize, World world) {
    int tempMass = mass;
    mass -= biteSize;
    if (tempMass - biteSize < 0) {
      return tempMass;
    } else {
      return biteSize;
    }
  }

  public void addFungus(World world) {
    myFungus = new Fungus(this, world.getLocation(this));
    hasFungus = true;
    world.add(myFungus);
  }

  @Override
  public DisplayInformation getInformation() {
    if (mass <= 5) return new DisplayInformation(
      java.awt.Color.black,
      "carcass-small"
    );
    return new DisplayInformation(java.awt.Color.black, "carcass");
  }
}