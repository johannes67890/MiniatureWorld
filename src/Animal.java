import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public abstract class Animal implements Actor, DynamicDisplayInformationProvider {

    protected int hp, maxHp, age, maxAge, vision, hunger;
    protected boolean moved;

    public void act(World world) {
        //this might not work

        //TODO FIX ENERGI

        hunger();

        if (world.getCurrentTime() % 19 == 0) {
            age++;
        }

        if (hunger <= 0) {
            hp--;
        } else if (hunger >= 7) {
            hp++;
        }

        if (hp <= 0 || age > maxAge) {
            die(world);
        }
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

    protected void die(World world) {
        world.delete(this);
    }

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

    protected Location getRandomEmptySurroundingTile(World world) {
        List<Location> list = new ArrayList<>(world.getEmptySurroundingTiles());
        if (list.size() == 0)
            return null;
        return list.get(new Random().nextInt(list.size()));
    }

    protected <T> void eat(T food, Location location, World world) {
        boolean eat = false;
        if (world.getTile(location).getClass().equals(food.getClass())) {
            eat = true;
        } else if (world.containsNonBlocking(world.getLocation(this))) {
            if (world.getNonBlocking(location).getClass().equals(food.getClass())) {
                eat = true;
            }
        }
        if (eat) {
            world.delete(world.getTile(location));
            hunger += 3;
            move(location, world);
        }
    }

    @Override
    abstract public DisplayInformation getInformation();

}
