package main;

import java.util.Random;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

public class Rabbit extends Animal {
    private final int adultAge = 3;
    private Lair home = null;

    public Rabbit() {
        super(10, 15, 2);
    }

    public void act(World world) {
        System.out.println("Hunger: " + hunger + ", HP: " + hp);

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

        // if the rabbit is on a burrow, and doesn't have a home, set the burrow as its
        // home
        if (world.containsNonBlocking(world.getLocation(this))) {
            if (home == null && world.getNonBlocking(world.getLocation(this)) instanceof Lair)
                home = (Lair) world.getNonBlocking(world.getLocation(this));
        }

        // if night move towards home
        if (world.isNight() && home != null) {
            if (world.getLocation(this).equals(world.getLocation(home))) {
                home.addAnimal(this, world);
                System.out.println("Rabbit enters home");
                return;
            }
            moveTowards(world.getLocation(home), world);
            System.out.println("Rabbit move to home");
            return;
        }

        // move away from predator
        if (world.getSurroundingTiles().size() == 8) {
            for (Location location : world.getSurroundingTiles(vision)) {
                if (world.getTile(location) instanceof Predator) {
                    moveAway(location, world);
                    System.out.println("Rabbit move from predator");
                    return;
                }
            }
        }

        // eat
        if (hunger <= 8 && world.containsNonBlocking(world.getLocation(this))) {
            if (world.getNonBlocking(world.getLocation(this)) instanceof Grass) {
                eat(new Grass(), world.getLocation(this), world);
                System.out.println("Rabit eat");
                return;
            }
        }

        // move towards food
        if (hunger < 7) {
            for (Location location : world.getSurroundingTiles(vision)) {
                if (world.getTile(location) instanceof Grass) {
                    moveTowards(location, world);
                    System.out.println("Rabbit move to food");
                    return;
                }
            }
        }

        // dig burrow
        if (home == null && new Random().nextBoolean()) {
            if (digBurrow(world)) {
                System.out.println("Rabbit dug");
                return;
            }
        }

        // reproduce
        if (new Random().nextInt(5) == 0) {
            if (reproduce(world)) {
                System.out.println("Rabbit repoduce");
                return;
            }
        }

        // 50% for random move 50% for no move
        if (new Random().nextBoolean()) {
            System.out.println("Rabbit move Random");
            move(getRandomEmptySurroundingTile(world), world);
            return;
        }
        System.out.println("Rabbit do nothing");

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
