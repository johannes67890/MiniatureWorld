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

  Carcass(Animal type) {
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
  }

  public void act(World world) {
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
    if(myFungus==null && new Random().nextInt(5)==0){
        myFungus=new Fungus(this, world);
        world.add(myFungus);
        System.out.println("Fungus spawned in carcass");
    }
  }

  public boolean hasFungus(){
    if(myFungus==null){
        return false;
    }
    return true;
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

  public void addFungus(Fungus fungus, World world) {
    myFungus = fungus;
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
