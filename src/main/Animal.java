package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

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
public abstract class Animal implements Actor, DynamicDisplayInformationProvider {

    protected int hp, maxHp, age, maxAge, vision, hunger;
    protected boolean isAdult = false;
    protected boolean isInLair = false;

    protected boolean life(World world){
        hunger();

        // An animal age increases every 19 ticks
        if (world.getCurrentTime() % 19 == 0) {
            age++;
        }

        // An animal loses hp if it is hungry and gains hp if it is not
        if (hunger <= 0) {
            hp--;
        } else if (hunger >= 7 && hp<maxHp) {
            hp++;
        }

        // An animal dies if it is too old or has no hp
        if (hp <= 0 || age > maxAge) {
            die(world);
            return true;
        }

        return false;
    }

    protected Animal(int hp, int maxAge, int vision) {
        this.age = 0;
        this.hp = hp;
        this.maxHp = hp;
        this.maxAge = maxAge;
        this.vision = vision;
        this.hunger = 10;

    }

    protected void hunger(){
        hunger--;
    }

    protected void takeDamage(int damage){
        hp-=damage;
    }
    
    protected void setInLair(Boolean b){
        isInLair=b;
    }

    /**
     * Eats food
     * @param <T> food type
     * @param food food class
     * @param location location of food
     */
    public <T> void eat(T food, Location location, World world) {
        if (world.getTile(location).getClass().equals(food.getClass())) {
            hunger += 3;
            world.delete(world.getTile(location));
            move(location, world);
        } else if (world.containsNonBlocking(world.getLocation(this))) {
            if (world.getNonBlocking(location).getClass().equals(food.getClass())) {
                hunger += 3;
                world.delete(world.getNonBlocking(location));
            }
        }
    }

    /**
     * Finds food and eats it
     * @param <T> food type
     * @param food food class
     * @param world world
     * @return true if food is found
     */
    public <T> boolean findFood(T food, World world){
        // Check around for food
        for (Location location : world.getSurroundingTiles()) {
            if (world.getTile(location) != null && world.getTile(location).getClass().equals(food.getClass())) { // if food is found
                eat(world.getTile(location), location, world);
                return true;
            }
        }
        // Go towards food
        for (Location location : world.getSurroundingTiles(vision)) {
            if (world.getTile(location) != null && world.getTile(location).getClass().equals(food.getClass())) { // if food is found
                moveTowards(location, world);
                return true;
            }
        }
        return false;

    };

    /**
     * Entity dies
     * @param world 
     */
    protected void die(World world) {
        world.delete(this);
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
        if (list.size() == 0)
            return null;
        return list.get(new Random().nextInt(list.size()));
    }

    @Override
    abstract public DisplayInformation getInformation();

}
