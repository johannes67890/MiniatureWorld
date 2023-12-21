package main;

import static java.util.Arrays.asList;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;
import java.util.ArrayList;
import java.util.Random;

/**
 * Snakes in the simulation world eat {@link Carcass} and can lay {@link SnakeEgg}s.
 * Snakes are predators and can attack {@link Bear}s and {@link Wolf}s.
 * @implNote extends {@link Predator}
 */
public class Snake extends Predator {

  private boolean isSleeping = false;

  public Snake() {
    super(10, 20, 2, 50, new ArrayList<>(asList("Carcass")), 1);
  }

  public void act(World world) {

    // Increases age when the current time is 19
    if (world.getCurrentTime() == 19) {
      age++;
    }
    
    //sleep at night
    isSleeping = world.isNight();
    if (isSleeping) {
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
      if (attackForFood(world)) {
        return;
      }
      move(getRandomEmptySurroundingTile(world), world);
    }

    //try to reproduce
    if (new Random().nextInt(10) == 0 && isAdult) {
      if (reproduce(world)) {
        return;
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

    // 50% for random move 50% for no move
    if (new Random().nextBoolean()) {
      move(getRandomEmptySurroundingTile(world), world);
      return;
    }
  }

  public boolean reproduce(World world) {
    for (Location location : world.getSurroundingTiles()) {
      if (world.getTile(location) instanceof Snake && world.getTile(location) != this) {
        Snake temp = (Snake) world.getTile(location);
        if (temp.isAdult && world.getEmptySurroundingTiles().size()!=0) {
          world.setTile(getRandomEmptySurroundingTile(world), new SnakeEgg());
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void attack(Location location, World world) {
    Animal target = (Animal) world.getTile(location);
    target.takeDamage(damage);
    target.setPoison(4);
  }

  @Override
  public DisplayInformation getInformation() {
    if (isAdult && isSleeping) {
      return new DisplayInformation(java.awt.Color.black, "snake-sleeping");
    }
    if (isAdult) {
      return new DisplayInformation(java.awt.Color.black, "snake");
    }
    if(isSleeping){
      return new DisplayInformation(java.awt.Color.black, "snake-small-sleeping");
    }
    return new DisplayInformation(java.awt.Color.black, "snake-small");
  }
}
