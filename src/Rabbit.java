import java.util.Random;

import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

public class Rabbit extends Animal {
    private Lair home = null;

    public Rabbit() {
        super(10, 15, 2);
    }

    public void act(World world) {
        // System.out.println("Hunger: " + hunger + ", HP: " + hp);

        if (life(world)) {
            return;
        }

        if (isInLair) {
            return;
        }


        // if the rabbit is on a burrow, and doesn't have a home, set the burrow as its
        // home
        if (world.containsNonBlocking(world.getLocation(this))) {
            if (home == null && world.getNonBlocking(world.getLocation(this)) instanceof Lair) {
                Lair temp = (Lair) world.getNonBlocking(world.getLocation(this));
                if (temp.getType() == "rabbit") {
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
                System.out.println("Rabbit enters home");
                return;
            }
            moveTowards(world.getLocation(home), world);
            System.out.println("Rabbit move to home");
            return;
        }

        // move away from predator
        for (Location location : world.getSurroundingTiles(vision)) {
            if (world.getTile(location) instanceof Predator) {
                moveAway(location, world);
                System.out.println("Rabbit move from predator");
                return;
            }
        }

        // eat
        if (hungry && world.containsNonBlocking(world.getLocation(this))) {
            if (world.getNonBlocking(world.getLocation(this)) instanceof Grass) {
                eat(new Grass(), world.getLocation(this), world);
                System.out.println("Rabit eat");
                return;
            }
        }

        // move towards food
        if (hungry) {
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
        if (new Random().nextInt(5) == 0 && isAdult) {
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
            home = new Lair("rabbit");
            world.setTile(world.getLocation(this), home);
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
