import java.util.Random;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Rabbit extends Animal implements Actor, DynamicDisplayInformationProvider {
    private final int adultAge = 3;
    private Burrow home = null;
    private boolean inBurrow = false;
    private boolean oneMoreStepPLZZ = false;

    public Rabbit(int hp) {
        super(hp);
    }

    public void act(World world) {
        if (oneMoreStepPLZZ) {
            oneMoreStepPLZZ = false;
        } else {

            if (hp <= 0 || age > 20) {
                die(world);
            }

            if (world.getCurrentTime() % 19 == 0) {
                age++;
            }

            if (!inBurrow) {

                // if its night the rabbit will lose health
                if (world.isNight())
                    hp--;

                // if the rabbit is on a burrow, and doesn't have a home, set the burrow as its
                // home
                if (world.containsNonBlocking(world.getLocation(this))) {
                    if (home == null && world.getNonBlocking(world.getLocation(this)) instanceof Burrow)
                        home = (Burrow) world.getNonBlocking(world.getLocation(this));
                }

                if (world.isNight() && home != null) {
                    moveTowardsHome(world);
                    System.out.println("I move to home");
                } else {
                    // we generate a random number to deside what action the rabbit will take
                    int r = new Random().nextInt(100);
                    if (r < 10 && home == null){ // 10% chance to try to dig burrow
                        digBurrow(world);
                        System.out.println("I dug");
                    } 
                    else if (r < 40){ // 30% chance to try to eat
                        eat(new Grass(), world);
                        System.out.println("I eat");
                    }
                    else if (r < 50){
                        reproduce(world); // 10% chance to try to reproduce
                        System.out.println("I Reproduce");
                    }
                    else { // 50% chance to move
                        System.out.println("I move random");
                        if (new Random().nextInt(age + 1) < 4) // less chance to move the older it is
                            move(getRandomEmptySurroundingTile(world), world);
                    }
                }
            }
        }
    }

    private void digBurrow(World world) {
        if (!world.containsNonBlocking(world.getLocation(this))) {
            home = new Burrow();
            world.setTile(world.getLocation(this), home);
        }
    }

    private void moveTowardsHome(World world) {
        int homeX = world.getLocation(home).getX();
        int homeY = world.getLocation(home).getY();
        int rabbitX = world.getLocation(this).getX();
        int rabbitY = world.getLocation(this).getY();
        if (homeX > rabbitX && homeY > rabbitY) {
            move(new Location(rabbitX + 1, rabbitY + 1), world);
        } else if (homeX > rabbitX && homeY < rabbitY) {
            move(new Location(rabbitX + 1, rabbitY - 1), world);
        } else if (homeX < rabbitX && homeY > rabbitY) {
            move(new Location(rabbitX - 1, rabbitY + 1), world);
        } else if (homeX < rabbitX && homeY < rabbitY) {
            move(new Location(rabbitX - 1, rabbitY - 1), world);
        } else if (homeX == rabbitX && homeY > rabbitY) {
            move(new Location(rabbitX, rabbitY + 1), world);
        } else if (homeX == rabbitX && homeY < rabbitY) {
            move(new Location(rabbitX, rabbitY - 1), world);
        } else if (homeX > rabbitX && homeY == rabbitY) {
            move(new Location(rabbitX + 1, rabbitY), world);
        } else if (homeX < rabbitX && homeY == rabbitY) {
            move(new Location(rabbitX - 1, rabbitY), world);
        } else if (homeX == rabbitX && homeY == rabbitY) {
            home.addRabbit(this, world);
        }
    }

    private void reproduce(World world) {
        for (Location tile : world.getSurroundingTiles()) {
            if (world.getTile(tile) instanceof Rabbit && world.getTile(tile)!=this && getRandomEmptySurroundingTile(world)!=null) {
                world.setTile(getRandomEmptySurroundingTile(world), new Rabbit(10));
                break;
            }
        }
    }

    public void setInBurrow(Boolean b) {
        inBurrow = b;
        oneMoreStepPLZZ = true;
    }

    @Override
    public DisplayInformation getInformation() {
        if (age <= adultAge)
            return new DisplayInformation(java.awt.Color.black, "rabbit-small");
        return new DisplayInformation(java.awt.Color.black, "rabbit-large");
    }
}
