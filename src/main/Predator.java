package main;

import itumulator.world.Location;
import itumulator.world.World;
import java.util.ArrayList;

/**
 * The abstract class Predator determins the general behaviour of predators in the world.
 * A predator is an animal that eats other animals. Predators is used by {@link Wolf} and {@link Bear}.
 * 
 * @param damage - The damage the predator does when attacking.
 * @implNote extends {@link Animal} 
 */
abstract public class Predator extends Animal {

  protected int damage;

  protected Predator(
    int hp,
    int maxAge,
    int vision,
    int biteSize,
    ArrayList<String> eats,
    int damage
  ) {
    super(hp, maxAge, vision, biteSize, eats);
    this.damage = damage;
  }

  //Predators attack to get food
  public boolean attackForFood(World world) {
    //if animal close attack
    for (Location location : world.getSurroundingTiles()) {
      if (
        world.getTile(location) instanceof Animal &&
        !world.getTile(location).getClass().equals(this.getClass())
      ) {
        attack(location, world);
        System.out.println(this.getClass().getSimpleName() + " Attack");
        return true;
      }
    }

    //if animal nearby go towards
    for (Location location : world.getSurroundingTiles(vision)) {
      if (world.getTile(location) instanceof Animal &&
        !world.getTile(location).getClass().equals(this.getClass())) {
        if(moveTowards(location, world)){
          moveTowards(location, world);
          System.out.println(this.getClass().getSimpleName() + " Move to attack");
          return true;
        } else return false;
      }
    }
    return false;
  }

  protected void attack(Location location, World world) {
    Animal target = (Animal) world.getTile(location);
    target.takeDamage(damage);
  }


}
