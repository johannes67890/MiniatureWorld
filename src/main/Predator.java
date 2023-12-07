package main;

import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;
import java.util.ArrayList;

public class Predator extends Animal {

  protected int damage;

  protected Predator(
    int hp,
    int maxAge,
    int vision,
    int bideSize,
    ArrayList<String> eats,
    int damage
  ) {
    super(hp, maxAge, vision, bideSize, eats);
    this.damage = damage;
  }

  public void act(World world) {}

  //Predetars attack to get food
  public boolean attackForFood(World world) {
    //if animal close attack
    for (Location location : world.getSurroundingTiles()) {
      if (
        world.getTile(location) instanceof Animal &&
        !world.getTile(location).getClass().equals(this.getClass())
      ) {
        attack(location, world);
        System.out.println(this.getClass().getName() + " Attack");
        return true;
      }
    }

    //if animal nearby go towards
    for (Location location : world.getSurroundingTiles(vision)) {
      if (world.getTile(location) instanceof Animal &&
        !world.getTile(location).getClass().equals(this.getClass())) {
        moveTowards(location, world);
        System.out.println(this.getClass().getName() + " Move to attack");
        return true;
      }
    }
    return false;
  }

  public void attack(Location location, World world) {
    Animal target = (Animal) world.getTile(location);
    target.takeDamage(damage);
  }

  @Override
  public DisplayInformation getInformation() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException(
      "Unimplemented method 'getInformation'"
    );
  }
}
