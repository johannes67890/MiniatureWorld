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

  private int meat;
  private Fungus myFungus = null;

  Carcass(Animal type) {
    if (type instanceof Rabbit) {
      if (type.isAdult) {
        meat = 5;
      } else {
        meat = 3;
      }
    }
    if (type instanceof Wolf) {
      if (type.isAdult) {
        meat = 10;
      } else {
        meat = 5;
      }
    }
    if (type instanceof Bear) {
      if (type.isAdult) {
        meat = 15;
      } else {
        meat = 8;
      }
    }
  }

  public void act(World world) {
    System.out.println("Meat: "+meat);

    if(new Random().nextInt(5)==0){
        meat--;
    }

    if (meat <= 0) {
      Location deadLocation = world.getLocation(this);
      world.delete(this);
      if (myFungus != null) {
        world.setTile(deadLocation, myFungus);
      }
    }

    if(myFungus==null && new Random().nextInt(1)==0){
        myFungus=new Fungus(this, world);
        world.add(myFungus);
        System.out.println("Fungus spawned");
    }
  }

  public boolean hasFungus(){
    if(myFungus==null){
        return false;
    }
    return true;
  }

  public int getEaten(int biteSize, World world) {
    int tempMeat = meat;
    meat -= biteSize;
    if (tempMeat - biteSize < 0) {
      return tempMeat;
    } else {
      return biteSize;
    }
  }

  public void addFungus(Fungus fungus, World world) {
    myFungus = fungus;
  }

  @Override
  public DisplayInformation getInformation() {
    if (meat <= 8) return new DisplayInformation(
      java.awt.Color.black,
      "carcass-small"
    );
    return new DisplayInformation(java.awt.Color.black, "carcass");
  }
}
