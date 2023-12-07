package main;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * An abstract class for all Animals.
 *
 * @param hp - The health of the animal
 * @param maxAge - The maximum age of the animal
 * @param vision - The vision-radius of the animal (used to look further than surrounding tiles)
 * @param hunger - The hunger of the animal
 * @param isInLair - Whether the animal is in its lair or not
 *
 * @implNote Implements {@link Actor} and {@link DynamicDisplayInformationProvider}
 */
public abstract class Animal
  implements Actor, DynamicDisplayInformationProvider {

  protected int hp, maxHp, age, maxAge, vision, hunger, biteSize;
  protected ArrayList<String> eats;
  protected boolean isAdult = false;
  protected boolean isInLair = false;
  protected boolean starving = false;
  protected boolean hungry = false;

  protected Animal(
    int hp,
    int maxAge,
    int vision,
    int biteSize,
    ArrayList<String> eats
  ) {
    this.age = 0;
    this.hp = hp;
    this.maxHp = hp;
    this.maxAge = maxAge;
    this.vision = vision;
    this.hunger = 10;
    this.biteSize = biteSize;
    this.eats = eats;
  }

  protected boolean life(World world) {
    hunger--;

    // An animal age increases every 19 ticks
    if (world.getCurrentTime() % 19 == 0) {
      age++;
    }

    // An animal loses hp if it is hungry and gains hp if it is full
    if (hunger <= 0) {
      hp--;
    } else if (hunger >= 7 && hp < maxHp) {
      hp++;
    }

    //An animal can be staving or hungry
    starving = hunger <= 5;
    hungry = hunger <= 10;

    // An animal dies if it is too old or has no hp
    if (hp <= 0 || age > maxAge) {
      die(world);
      return true;
    }
    return false;
  }

  protected void takeDamage(int damage) {
    hp -= damage;
  }

  protected void setInLair(Boolean b) {
    isInLair = b;
  }

  /**
   * Finds food and eats it
   * @param world world
   * @return true if food is found
   */

  public boolean food(World world) {
    //try to eat around itself
    for (Location location : world.getSurroundingTiles()) {
      if (world.containsNonBlocking(location)) {
        if (
          eats.contains(world.getNonBlocking(location).getClass().getName())
        ) {
          Eatable food = (Eatable) world.getNonBlocking(location);
          hunger += food.getEaten(biteSize, world);
          return true;
        }
      }
      if (
        world.getTile(location) != null &&
        eats.contains(world.getTile(location).getClass().getName())
      ) {
        Eatable food = (Eatable) world.getTile(location);
        hunger += food.getEaten(biteSize, world);
        return true;
      }
    }
    // Go towards food
    for (Location location : world.getSurroundingTiles(vision)) {
      if (world.getTile(location) != null && ((world.containsNonBlocking(location) && eats.contains(world.getNonBlocking(location).getClass().getName())) || eats.contains(world.getTile(location).getClass().getName()))) {
        moveTowards(location, world);
        return true;
      }
    }
    return false;
  }

  /**
   * Entity dies
   * @param world
   */
  protected void die(World world) {
    Location deadLocation = world.getLocation(this);
    world.delete(this);
    world.setTile(deadLocation, new Carcass(this));
  }

  /**
   * Moves to a location
   * @param location - The location to move to
   * @param world - The World to move in
   */
  protected void move(Location location, World world) {
    if (location != null) {
      if (world.isTileEmpty(location)) {
        try {
          world.move(this, location);
        } catch (Exception e) {
          System.out.println(e.getMessage());
        }
      }
    }
  }

  /**
   * Moves towards a location
   *
   * @param location - The location to move towards
   */
  protected void moveTowards(Location location, World world) {
    int targetX = location.getX();
    int targetY = location.getY();
    int thisX = world.getLocation(this).getX();
    int thisY = world.getLocation(this).getY();
    if (targetX > thisX && targetY > thisY) {
      move(new Location(thisX + 1, thisY + 1), world);
    } else if (targetX > thisX && targetY < thisY) {
      move(new Location(thisX + 1, thisY - 1), world);
    } else if (targetX < thisX && targetY > thisY) {
      move(new Location(thisX - 1, thisY + 1), world);
    } else if (targetX < thisX && targetY < thisY) {
      move(new Location(thisX - 1, thisY - 1), world);
    } else if (targetX == thisX && targetY > thisY) {
      move(new Location(thisX, thisY + 1), world);
    } else if (targetX == thisX && targetY < thisY) {
      move(new Location(thisX, thisY - 1), world);
    } else if (targetX > thisX && targetY == thisY) {
      move(new Location(thisX + 1, thisY), world);
    } else if (targetX < thisX && targetY == thisY) {
      move(new Location(thisX - 1, thisY), world);
    }
  }

  /**
   * Moves away from a location
   *
   * @param location - The location to move towards
   */
  protected void moveAway(Location location, World world) {
    int targetX = location.getX();
    int targetY = location.getY();
    int thisX = world.getLocation(this).getX();
    int thisY = world.getLocation(this).getY();
    if (targetX > thisX && targetY > thisY) {
      move(new Location(thisX - 1, thisY - 1), world);
    } else if (targetX > thisX && targetY < thisY) {
      move(new Location(thisX - 1, thisY + 1), world);
    } else if (targetX < thisX && targetY > thisY) {
      move(new Location(thisX + 1, thisY - 1), world);
    } else if (targetX < thisX && targetY < thisY) {
      move(new Location(thisX + 1, thisY + 1), world);
    } else if (targetX == thisX && targetY > thisY) {
      move(new Location(thisX, thisY - 1), world);
    } else if (targetX == thisX && targetY < thisY) {
      move(new Location(thisX, thisY + 1), world);
    } else if (targetX > thisX && targetY == thisY) {
      move(new Location(thisX - 1, thisY), world);
    } else if (targetX < thisX && targetY == thisY) {
      move(new Location(thisX + 1, thisY), world);
    }
  }

  /**
   * Gets a random empty surrounding tile
   * @return random empty surrounding tile
   */
  protected Location getRandomEmptySurroundingTile(World world) {
    List<Location> list = new ArrayList<>(world.getEmptySurroundingTiles());
    if (list.size() == 0) return null;
    return list.get(new Random().nextInt(list.size()));
  }

  @Override
  public abstract DisplayInformation getInformation();
}
