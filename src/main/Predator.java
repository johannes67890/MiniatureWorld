package main;

import itumulator.world.Location;
import itumulator.world.World;
import java.util.ArrayList;

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

  //Predetars attack to get food
  protected boolean attackForFood(World world) {
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
        moveTowards(location, world);
        System.out.println(this.getClass().getSimpleName() + " Move to attack");
        return true;
      }
    }
    return false;
  }

  protected void attack(Location location, World world) {
    Animal target = (Animal) world.getTile(location);
    target.takeDamage(damage);
  }


}
