package main;

import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Random;

/**
 * The {@link Wolf} is a predator that hunts {@link Rabbit}s, {@link Snake}s and {@link Bear}s in packs. 
 * It eats the {@link Carcass}s from the dead animals. The wolf is a pack animal and will try to stay close to its pack and its leader.
 * If a wolf from another pack is to close it will attack it.
 *
 * @param myPack - The {@link Wolfpack} the wolf belongs to
 *
 * @implNote extends {@link Predator} 
 */
public class Wolf extends Predator {
  private WolfPack myPack;

  public Wolf(WolfPack pack) {
    super(15, 30, 2, 4, new ArrayList<>(asList("Carcass")), 6);
    myPack = pack;
    myPack.addWolf(this);
  }

  public void act(World world) {

    // Increases age when the current time is 19
    if (world.getCurrentTime() == 19) {
      age++;
    }

    System.out.println(hunger);
    // if in lair dont do anything
    if (isInLair) {
      return;
    }

    if (life(world)) {
      return;
    }
    // if night move towards home
    if (world.isNight() && myPack.getHome(world) != null) {
      if (world.getLocation(this).equals(world.getLocation(myPack.getHome(world)))) {
        myPack.addToHome(this, world);
        System.out.println("Wolf enters home");
        return;
      }
      if(moveTowards(world.getLocation(myPack.getHome(world)), world)){
        System.out.println("Wolf moved towards home");
        return;
      } else {
        move(getRandomEmptySurroundingTile(world), world);
        System.out.println("Wolf move random because could not get to home");
        return;
      }
    }

    // if starving
    if (starving) {
      if (food(world)) {
        return;
      }
      if (attackForFood(world)) {
        return;
      }
      move(getRandomEmptySurroundingTile(world), world);
      System.out.println("Wolf move cuz starving");
    }

    // If far way from leader, go towards leader
    if (world.isOnTile(myPack.getLeader())) {
      if (!world.getLocation(myPack.getLeader()).equals(world.getLocation(this))) {
        boolean closeToLeader = false;
        for (Location location : world.getSurroundingTiles(vision)) {
          if (location.equals(world.getLocation(myPack.getLeader()))) {
            closeToLeader = true;
            break;
          }
        }
        if (!closeToLeader) {
          if(moveTowards(world.getLocation(myPack.getLeader()), world)){
            System.out.println("Wolf moved closer to leader");
            return;
          } else {
            move(getRandomEmptySurroundingTile(world), world);
            System.out.println("Wolf move random because could not get to leader");
            return;
          }
        }
      }
    }

    // if hungry
    if (hungry) {
      if (food(world)) {
        return;
      }
      if (attackForFood(world)) {
        return;
      }
    }

    // Attack if wolf from other pack is to close
    for (Location location : world.getSurroundingTiles()) {
      if (world.getTile(location) instanceof Wolf &&
          !myPack.isInPack((Wolf) world.getTile(location))) {
        attack(location, world);
        System.out.println("Wolf attacked wolf");
        return;
      }
    }

    // Move away if wolf from other pack is in sight
    for (Location location : world.getSurroundingTiles(vision)) {
      if (world.getTile(location) instanceof Wolf &&
          !myPack.isInPack((Wolf) world.getTile(location))) {
        if(moveAway(location, world)){
          System.out.println("Wolf moved away from other wolf");
          return;
        }
      }
      if (world.containsNonBlocking(location)) {
        if (world.getNonBlocking(location) instanceof Lair) {
          Lair temp = (Lair) world.getNonBlocking(location);
          if (temp.getAnimalsInLair().getClass().isInstance(Wolf.class) && !myPack.getHome(world).equals(temp)) {
            if(moveAway(location, world)){
              System.out.println("Wolf moved away from other lair");
              return;
            }
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

  // Wolf attack to get food
  @Override
  public boolean attackForFood(World world) {
    // if animal close attack
    for (Location location : world.getSurroundingTiles()) {

      // If bear check that theres other wolfs nearby to help
      if (world.getTile(location) instanceof Bear) {
        int otherWolfs = 0;
        for (Location view : world.getSurroundingTiles(vision)) {
          if (world.getTile(view) instanceof Wolf) {
            otherWolfs++;
          }
        }
        // If theres is atleast 2 other wolfs nearby attack the bear
        if (otherWolfs >= 2) {
          attack(location, world);
          System.out.println(this.getClass().getSimpleName() + " Attacked bear");
          return true;
        }
      } else if (world.getTile(location) instanceof Animal &&
          !world.getTile(location).getClass().equals(this.getClass())) {
        attack(location, world);
        System.out.println(this.getClass().getSimpleName() + " Attack");
        return true;
      }
    }

    // if animal nearby go towards
    for (Location location : world.getSurroundingTiles(vision)) {

      // If bear check that theres other wolfs nearby to help
      if (world.getTile(location) instanceof Bear) {
        int otherWolfs = 0;
        for (Location view : world.getSurroundingTiles(vision)) {
          if (world.getTile(view) instanceof Wolf) {
            otherWolfs++;
          }
        }
        // If theres is atleast 2 other wolfs nearby go towards the bear
        if (otherWolfs >= 2) {
          moveTowards(location, world);
          System.out.println(this.getClass().getSimpleName() + " Move to bear to attack");
          return true;
        }
      }
      else if (world.getTile(location) instanceof Animal &&
          !world.getTile(location).getClass().equals(this.getClass())) {
        moveTowards(location, world);
        System.out.println(this.getClass().getSimpleName() + " Move to attack");
        return true;
      }
    }
    return false;
  }

  public int getHp() {
    return hp;
  }

  @Override
  public int hungerPlus(int amount) {
    hunger += amount;
    myPack.packEat(this);
    return amount;
  }

  @Override
  public DisplayInformation getInformation() {
    if (isAdult)
      return new DisplayInformation(java.awt.Color.black, "wolf");
    return new DisplayInformation(java.awt.Color.black, "wolf-small");
  }
}
