package main;

import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Random;
import main.testReader.ClassTypes;

/**
 * Wolf class
 *
 * @param damage - The damage the wolf does
 * @param myPack - The {@link Wolfpack} the wolf belongs to
 *
 * @implNote Implements {@link Predator} and extends {@link Animal}
 */
public class Wolf extends Predator {
  private WolfPack myPack;

  public Wolf(WolfPack pack) {
    super(15, 30, 2,4, new ArrayList<>(asList("main.Carcass")), 6);
    myPack = pack;
    myPack.addWolf(this);
  }

  public void act(World world) {
    // if in lair dont do anything
    if (isInLair) {
      return;
    }


    if (life(world)) {
      return;
    }

    // if starving
    if (starving) {
      if (food(world)) {
        return;
      }
      if (attackForFood(world)){
        return;
      }
      move(getRandomEmptySurroundingTile(world), world);
      System.out.println("Wolf move cuz starving");
    }

    // if night move towards home
    if (world.isNight() && myPack.getHome(world) != null) {
      if (
        world.getLocation(this).equals(world.getLocation(myPack.getHome(world)))
      ) {
        myPack.addToHome(this, world);
        System.out.println("Wolf enters home");
        return;
      }
      moveTowards(world.getLocation(myPack.getHome(world)), world);
      System.out.println("Wolf move to home");
      return;
    }

    // If far way from leader, go towards leader
    if (world.isOnTile(myPack.getLeader())) {
      if (
        !world.getLocation(myPack.getLeader()).equals(world.getLocation(this))
      ) {
        boolean closeToLeader = false;
        for (Location location : world.getSurroundingTiles(vision)) {
          if (location.equals(world.getLocation(myPack.getLeader()))) {
            closeToLeader = true;
            break;
          }
        }
        if (!closeToLeader) {
          moveTowards(world.getLocation(myPack.getLeader()), world);
          System.out.println("Wolf moved closer to leader");
          return;
        }
      }
    }

    // if hungry
    if (hungry) {
      if (food(world)) {
        return;
      }
      if (attackForFood(world)){
        return;
      }
    }

    // Attack if wolf from other pack is to close
    for (Location location : world.getSurroundingTiles()) {
      if (
        world.getTile(location) instanceof Wolf &&
        !myPack.isInPack((Wolf) world.getTile(location))
      ) {
        attack(location, world);
        System.out.println("Wolf attacked wolf");
        return;
      }
    }

    // Move away if wolf from other pack is in sight
    for (Location location : world.getSurroundingTiles(vision)) {
      if (
        world.getTile(location) instanceof Wolf &&
        !myPack.isInPack((Wolf) world.getTile(location))
      ) {
        moveAway(location, world);
        System.out.println("Wolf moved away from other wolf");
        return;
      }
      if (world.containsNonBlocking(location)) {
        if (world.getNonBlocking(location) instanceof Lair) {
          Lair temp = (Lair) world.getNonBlocking(location);
          if (temp.getAnimalsInLair().getClass().isInstance(Wolf.class) && !myPack.getHome(world).equals(temp)) {
            moveAway(location, world);
            System.out.println("Wolf moved away from other lair");
            return;
          }
        }
      }
    }
    
    // 50% for random move 50% for no move
    if (new Random().nextBoolean()) {
      move(getRandomEmptySurroundingTile(world), world);
      System.out.println("Wolf move random");
      return;
    }
    System.out.println("Wolf do nothing");
  }

  public int getHp() {
    return hp;
  }

  @Override
  public void hungerPlus(int amount){
    hunger+=amount;
    myPack.packEat(this);
  }

  @Override
  public DisplayInformation getInformation() {
    if (isAdult) return new DisplayInformation(java.awt.Color.black, "wolf");
    return new DisplayInformation(java.awt.Color.black, "wolf-small");
  }
}
