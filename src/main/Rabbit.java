package main;

import java.util.Random;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

/**
 * Rabbit class
 * 
 * @param adultAge - The age at which the rabbit is considered an adult (used for DisplayInformation see {@link #getInformation})
 * @param home - The lair of the rabbit
 * @implNote extends {@link Animal}
 */
public class Rabbit extends Animal {
    private final int adultAge = 3;
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

        // if its night the rabbit will lose health
        if (world.isNight()) {
            hp--;
        }

        // if the rabbit is on a lair, and doesn't have a home, set the burrow as its
        // home
        if (world.containsNonBlocking(world.getLocation(this))) {
            if (home == null && world.getNonBlocking(world.getLocation(this)) instanceof Lair)
                home = (Lair) world.getNonBlocking(world.getLocation(this));
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
        if (new Random().nextInt(5) == 0) {
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
            return true;
        }
        return false;
    }

    private boolean reproduce(World world) {
        for (Location tile : world.getSurroundingTiles()) {
            if (world.getTile(tile) instanceof Rabbit && world.getTile(tile) != this
                    && getRandomEmptySurroundingTile(world) != null) {
                world.setTile(getRandomEmptySurroundingTile(world), new Rabbit());
                return true;
            }
        }
        return false;
    }

    @Override
    protected void hunger() {
        if (!isInLair) {
            hunger--;
        }
    }

    @Override
    public DisplayInformation getInformation() {
        if (age <= adultAge)
            return new DisplayInformation(java.awt.Color.black, "rabbit-small");
        return new DisplayInformation(java.awt.Color.black, "rabbit-large");
    }
}
