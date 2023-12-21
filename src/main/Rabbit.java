package main;

import static java.util.Arrays.asList;

import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.Random;

/**
 * Rabbits in the simulation world eat {@link Grass}, dig burrows/{@link Lair}'s and can reproduce.
 * Rabbits are lowers in the foodchain in the world and can be eaten by {@link Wolf}s.
 *
 * @param adultAge - The age at which the rabbit is considered an adult (used for DisplayInformation see {@link #getInformation})
 * @param home - The {@link Lair} of the rabbit
 * @implNote extends {@link Animal}
 */
public class Rabbit extends Animal {

  private Lair home = null;

  public Rabbit() {
    super(10, 15, 2, 1, new ArrayList<>(asList("Grass")));
  }

  public void act(World world) {

    // Increases age when the current time is 19
    if (world.getCurrentTime() == 19) {
      age++;
    }

    if (isInLair) {
      return;
    }

    if (life(world)) {
      return;
    }
    // if the rabbit is on a lair, and doesn't have a home, set the burrow as its
    // home
    if (world.containsNonBlocking(world.getLocation(this))) {
      if (
        home == null &&
        world.getNonBlocking(world.getLocation(this)) instanceof Lair
      ) {
        Lair temp = (Lair) world.getNonBlocking(world.getLocation(this));
        if (temp.getType().equals("Rabbit")) {
          home = temp;
        }
      }
    }

    //if old chance to do nothing
    if (new Random().nextInt(100 - age * 3) == 0) {
      return;
    }

    // if starving eat
    if (starving) {
      if (food(world)) {
        return;
      }
    }

    // if night move towards home
    if (world.isNight() && home != null) {
      if (world.getLocation(this).equals(world.getLocation(home))) {
        home.addAnimal(this, world);
        return;
      }
      if(moveTowards(world.getLocation(home), world)){
        return;
      } else {
        move(getRandomEmptySurroundingTile(world), world);
        return;
      }
    }

    // move away from predator
    if(moveAwayFromPredator(world)){
      return;
    }

    // eat
    if (hungry) {
      if (food(world)) {
        return;
      }
    }

    // dig burrow
    if (home == null && new Random().nextBoolean()) {
      if (digBurrow(world)) {
        return;
      }
    }

    // 25% chance to reproduce
    if (new Random().nextInt(4) == 0 && isAdult) {
      if (reproduce(world)) {
        return;
      }
    }

    // 66.6% for random move 33.3% for no move
    if (new Random().nextInt(3) != 0) {
      move(getRandomEmptySurroundingTile(world), world);
      return;
    }
  }

  public boolean moveAwayFromPredator(World world) {
      for (Location location : world.getSurroundingTiles(vision)) {
        if (world.getTile(location) instanceof Predator) {
          if(moveAway(location, world)){
            return true;
          } else {
            move(getRandomEmptySurroundingTile(world), world);
            return true;
          }
        }
      }
    return false;
  }

  public boolean digBurrow(World world) {
    if (!world.containsNonBlocking(world.getLocation(this))) {
      home = new Lair();
      world.setTile(world.getLocation(this), home);
      return true;
    }
    return false;
  }

  public boolean reproduce(World world) {
    for (Location location : world.getSurroundingTiles()) {
      if (world.getTile(location) instanceof Rabbit && world.getTile(location) != this) {
        Rabbit temp = (Rabbit) world.getTile(location);
        if (temp.isAdult && world.getEmptySurroundingTiles().size() != 0) {
          world.setTile(getRandomEmptySurroundingTile(world), new Rabbit());
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public boolean food(World world) {
    //try to eat grass
    if (world.containsNonBlocking(world.getLocation(this))) {
      if (world.getNonBlocking(world.getLocation(this)) instanceof Grass) {
        Grass food = (Grass) world.getNonBlocking(world.getLocation(this));
        hungerPlus(food.getEaten(biteSize, world));
        return true;
      }
    }

    // Go towards food
    for (Location location : world.getSurroundingTiles(vision)) {
      if (
        world.getTile(location) != null &&
        (
          (
            world.containsNonBlocking(location) &&
            eats.contains(world.getNonBlocking(location).getClass().getSimpleName())
          ) ||
          eats.contains(world.getTile(location).getClass().getSimpleName())
        )
      ) {
        moveTowards(location, world);
        return true;
      }
    }
    return false;
  }


  @Override
  public DisplayInformation getInformation() {
    if (isAdult) return new DisplayInformation(
      java.awt.Color.black,
      "rabbit-large"
    );
    return new DisplayInformation(java.awt.Color.black, "rabbit-small");
  }
}