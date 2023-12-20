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
 * @param hp       - The health of the animal
 * @param maxAge   - The maximum age of the animal
 * @param vision   - The vision-radius of the animal (used to look further than
 *                 surrounding tiles)
 * @param hunger   - The hunger of the animal
 * @param isInLair - Whether the animal is in its lair or not
 *
 * @implNote Implements {@link Actor} and
 *           {@link DynamicDisplayInformationProvider}
 */
public abstract class Animal
    implements Actor, DynamicDisplayInformationProvider {

  protected int hp, maxHp, age, maxAge, vision, hunger, biteSize;
  protected ArrayList<String> eats;
  public boolean isAdult = false;
  protected boolean isInLair = false;
  protected boolean starving = false;
  protected boolean hungry = false;
  protected int poison = 0;

  protected Animal(
      int hp,
      int maxAge,
      int vision,
      int biteSize,
      ArrayList<String> eats) {
    this.age = 0;
    this.hp = hp;
    this.maxHp = hp;
    this.maxAge = maxAge;
    this.vision = vision; // The vision-radius of the animal (used to look further than surrounding tiles)
    this.hunger = 15;
    this.biteSize = biteSize; // The amount of hunger the animal gets from eating
    this.eats = eats; // The types of food the animal eats
  }

  /**
   * The generel life cycle of an animal in the simmulation. 
   * The animal loses hp if it is hungry, poisened and gains hp if it eats. 
   * An animal dies if it is too old or has no hp.
   * An animals age increases after one night and day (19 ticks).
   * @param world - The world the animal is in
   * @return true if the animal dies
   */
  public boolean life(World world) {
    hunger--;

    // if age over 1 the animal becomes adult
    isAdult = age >= 1;

    // if animal is poisoned take damage
    if (poison > 0) {
      hp--;
      poison--;
    }

    // An animal age increases every 19 ticks
    if (world.getCurrentTime() % 19 == 0 && world.getCurrentTime() != 0) {
      age++;
    }

    // An animal loses hp if it is hungry and gains hp if it is full
    if (hunger <= 0) {
      hp--;
    } else if (hunger >= 8 && hp < maxHp) {
      hp++;
    }

    // An animal can be staving or hungry
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
   * Animals find and eat food in the simmulaiton. An animal can eat food around itself or move towards food. 
   * The animal moves towwords food if it is in its vision radius.
   * The animal checks if the food that surrounds the animal is eatable from the "eats" list.
   * 
   * @param world world
   * @return true if food is found
   */
  public boolean food(World world) {
    //
    // try to eat around itself.
    //
    for (Location location : world.getSurroundingTiles()) {
      // if there is food around itself, that is can eat.
      if (world.containsNonBlocking(location)) {
        if (eats.contains(world.getNonBlocking(location).getClass().getSimpleName())) {
          Eatable food = (Eatable) world.getNonBlocking(location);
          hungerPlus(food.getEaten(biteSize, world)); // eat the food & update animal stats
          return true;
        }
      }
      if (world.getTile(location) != null &&
        eats.contains(world.getTile(location).getClass().getSimpleName())) {
        Eatable food = (Eatable) world.getTile(location);
        if(hungerPlus(food.getEaten(biteSize, world)) > 0){
          return true;
        }
      }
    }
    //
    // Go towards food, within vision radius.
    //
    for (Location location : world.getSurroundingTiles(vision)) {
      if (world.getTile(location) != null && ((world.containsNonBlocking(location)
          && eats.contains(world.getNonBlocking(location).getClass().getSimpleName()))
          || eats.contains(world.getTile(location).getClass().getSimpleName()))) {
        moveTowards(location, world);
        return true;
      }
    }
    return false;
  }

  /**
   * Animal dies and get deleted from the world. Afterward a carcass is placed on the tile where the animal died.
   * 
   * @param world - The world the animal is in
   */
  public void die(World world) {
    Location deadLocation = world.getLocation(this);
    world.delete(this);
    world.setTile(deadLocation, new Carcass(this, false));
  }

  /**
   * Moves to a location
   * 
   * @param location - The location to move to
   * @param world    - The World to move in
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
  protected boolean moveAway(Location location, World world) {
    if(world.getSurroundingTiles().size() < 8){
      return false;
    }
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
    return true;
  }

  public int hungerPlus(int amount) {
    hunger += amount;
    return amount;
  }

  /**
   * Gets a random empty surrounding tile
   * 
   * @return random empty surrounding tile
   */
  protected Location getRandomEmptySurroundingTile(World world) {
    List<Location> list = new ArrayList<>(world.getEmptySurroundingTiles());
    if (list.size() == 0)
      return null;
    return list.get(new Random().nextInt(list.size()));
  }

  public int getHunger() {
    return hunger;
  }

  public int getHp() {
    return hp;
  }

  public void setPoison(int poison){
    this.poison = poison;
  }

  public int getPoison(){
    return poison;
  }

  @Override
  public abstract DisplayInformation getInformation();
}
