
package main;

import static java.util.Arrays.asList;

import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

/**
 * The Bear class represents a bear in the simulation.
 * It is a predator that hunts other animals for food.
 * It has a territory that the bear garuds.
 * 
 * @param territoryC - The center of the territory
 * @param territory - The territory of the bear
 * @implNote Implements {@link Predator} and extends {@link Animal}.
 */
public class Bear extends Predator {

  private Location territoryC;
  private Set<Location> territory;
  private boolean isSleeping = false;

  public Bear(Location territoryC) {
    super(
      30,
      30,
      2,
      6,
      new ArrayList<>(asList("Carcass", "Bush")),
      8
    );
    this.territoryC = territoryC;
  }

  /**
   * Sets the territory of the bear.
   * @param territoryC - The center of the territory (Location)
   * @param world - The simmulation world
   */
  public void setTerritory(Location territoryC, World world){
        if (territoryC == null) {
      this.territoryC =
        new Location(
          new Random().nextInt(world.getSize()),
          new Random().nextInt(world.getSize())
        );
    }
    this.territory = world.getSurroundingTiles(this.territoryC, 2);
    }

  /**
   * The Bear actions and behaviour.
   * 
   * @param world - The world simmulation.
   */
  public void act(World world) {
    setTerritory(territoryC, world); // set territory
    
    //sleep at night
    isSleeping = world.isNight();
    if (isSleeping) {
      return;
    }
    // if dead
    if (life(world)) {
      return;
    }

    // if starving, find ot attack for food. Else move randomly to find food
    if (starving) {
      if (food(world)) {
        return;
      }
      if (attackForFood(world)) {
        return;
      }
      move(getRandomEmptySurroundingTile(world), world);
    }

    // check around to attack if other animal is in terratory
    for (Location location : territory) {
      for (Location view : world.getSurroundingTiles()) {
        if (location.equals(view) && world.getTile(view) instanceof Animal) {
          attack(view, world);
          System.out.println("Bear attack");
          return;
        }
      }
    }

    // check territory and gotowards animal if in terratory
    for (Location location : territory) {
      for (Location view : world.getSurroundingTiles(vision)) {
        if (location.equals(view) && world.getTile(view) instanceof Animal) {
          moveTowards(view, world);
          System.out.println("Bear move to attack");
          return;
        }
      }
    }

    //if hungry
    if (hungry) {
      if (food(world)) {
        return;
      }
      if (attackForFood(world)) {
        return;
      }
    }

    //move from center
    if (world.getLocation(this).equals(territoryC)) {
      move(getRandomEmptySurroundingTile(world), world);
      System.out.println("Bear move from C");
      return;
    }

    //move towards center
    moveTowards(territoryC, world);
    System.out.println("Bear move towards C");
  }

  @Override
  public DisplayInformation getInformation() {
    if (isAdult && isSleeping) {
      return new DisplayInformation(java.awt.Color.black, "bear-sleeping");
    }
    if (isAdult) {
      return new DisplayInformation(java.awt.Color.black, "bear");
    }
    if(isSleeping){
      return new DisplayInformation(java.awt.Color.black, "bear-small-sleeping");
    }
    return new DisplayInformation(java.awt.Color.black, "bear-small");
  }
}
