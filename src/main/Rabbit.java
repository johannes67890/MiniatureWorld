package main;

import java.util.Random;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;
import main.testReader.ClassTypes;

/**
 * Rabbit class
 * 
 * @param adultAge - The age at which the rabbit is considered an adult (used for DisplayInformation see {@link #getInformation})
 * @param home - The lair of the rabbit
 * @implNote extends {@link Animal}
 */
public class Rabbit extends Animal {
    private Lair home = null;

    public Rabbit() {
        super(10, 15, 2);
    }

    public void act(World world) {
        if (life(world)) {
            return;
        }

        if (isInLair) {
            return;
        }


        // if the rabbit is on a lair, and doesn't have a home, set the burrow as it's home.
        if (world.containsNonBlocking(world.getLocation(this))) {
            if (home == null && world.getNonBlocking(world.getLocation(this)) instanceof Lair) {
                Lair temp = (Lair) world.getNonBlocking(world.getLocation(this));
                if (temp.getAnimals().getClass().isInstance(ClassTypes.rabbit)) {
                    home = temp;
                }
            }
        }

        //if old chance to do nothing
        if(new Random().nextInt(100-age*3) == 0){
            return;
        }

        // if night move towards home
        if (world.isNight() && home != null) {
            if (world.getLocation(this).equals(world.getLocation(home))) {
                home.addAnimal(this, world);
                return;
            }
            moveTowards(world.getLocation(home), world);
            return;
        }

        // move away from predator
        if (world.getSurroundingTiles().size() == 8) {
            for (Location location : world.getSurroundingTiles(vision)) {
                if (world.getTile(location) instanceof Predator) {
                    moveAway(location, world);
                    return;
                }
            }
        }

        // eat
        if(hunger<=2){
            findFood(Grass.class, world);
        }

        if (hunger <= 8 && world.containsNonBlocking(world.getLocation(this))) {
            if (world.getNonBlocking(world.getLocation(this)) instanceof Grass) {
                eat(new Grass(), world.getLocation(this), world);
                return;
            }
        }

        if(hunger<=7){
            findFood(Grass.class, world);
        }

        // dig burrow
        if (home == null && new Random().nextBoolean()) {
            if (digBurrow(world)) {
                return;
            }
        }

        // reproduce
        if (new Random().nextInt(5) == 0 && isAdult) {
            if (reproduce(world)) {
                return;
            }
        }

        // 50% for random move 50% for no move
        if (new Random().nextBoolean()) {
            move(getRandomEmptySurroundingTile(world), world);
            return;
        }
    }

    private boolean digBurrow(World world) {
        if (!world.containsNonBlocking(world.getLocation(this))) {
            home = new Lair();
            world.setTile(world.getLocation(this), home);
            home.addAnimal(this, world);
            return true;
        }
        return false;
    }

    private boolean reproduce(World world) {
        for (Location location : world.getSurroundingTiles()) {
            if (world.getTile(location) instanceof Rabbit) {
                Rabbit temp = (Rabbit) world.getTile(location);
                if (temp.isAdult) {
                    world.setTile(getRandomEmptySurroundingTile(world), new Rabbit());
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void hunger() {
        if (!isInLair && hunger > 0) {
            hunger--;
        }
    }

    @Override
    public DisplayInformation getInformation() {
        if (isAdult) 
            return new DisplayInformation(java.awt.Color.black, "rabbit-large");
        return new DisplayInformation(java.awt.Color.black, "rabbit-small");
    }
}
