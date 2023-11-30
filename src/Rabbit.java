import java.util.Random;

import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

public class Rabbit extends Animal {
    private final int adultAge = 3;
    private Burrow home = null;
    private boolean inBurrow = false;

    public Rabbit() {
        super(10, 15, 2);
    }

    public void act(World world) {
        moved = false;
        if (!inBurrow) {
            // if its night the rabbit will lose health
            if (world.isNight())
                hp--;

            // move
            // move towards home
            if (world.isNight() && home != null) {
                if (world.getLocation(this).equals(world.getLocation(home))) {
                    home.addRabbit(this, world);
                    return;
                }
                moveTowards(world.getLocation(home), world);
                moved = true;
                System.out.println("I move to home");

            }
            // move away from predator
            if (!moved && world.getSurroundingTiles().size()==8) {
                for (Location location : world.getSurroundingTiles(vision)) {
                    if (world.getTile(location) instanceof Predator) {
                        moveAway(location, world);
                        moved = true;
                        break;
                    }
                }
            }
            // move towards food
            if (!moved && hunger <= 7) {
                for (Location location : world.getSurroundingTiles(vision)) {
                    if (world.getTile(location) instanceof Grass) {
                        moveTowards(location, world);
                        moved = true;
                        break;
                    }
                }
            }
            // 50% for random move 50% for no move
            if (!moved && new Random().nextBoolean()) {
                move(getRandomEmptySurroundingTile(world), world);
            }

            // if the rabbit is on a burrow, and doesn't have a home, set the burrow as its
            // home
            if (world.containsNonBlocking(world.getLocation(this))) {
                if (home == null && world.getNonBlocking(world.getLocation(this)) instanceof Burrow)
                    home = (Burrow) world.getNonBlocking(world.getLocation(this));
            }

            // Do some action
            if (home == null && new Random().nextInt(10) == 0) {
                digBurrow(world);
            } else if (hunger <= 8) {
                eat(new Grass(), world.getLocation(this), world);
            } else if (new Random().nextInt(10) == 0) {
                reproduce(world);
            }
        }
    }

    private void digBurrow(World world) {
        if (!world.containsNonBlocking(world.getLocation(this))) {
            home = new Burrow();
            world.setTile(world.getLocation(this), home);
        }
    }

    private void reproduce(World world) {
        for (Location tile : world.getSurroundingTiles()) {
            if (world.getTile(tile) instanceof Rabbit && world.getTile(tile) != this
                    && getRandomEmptySurroundingTile(world) != null) {
                world.setTile(getRandomEmptySurroundingTile(world), new Rabbit());
                break;
            }
        }
    }

    public void setInBurrow(Boolean b) {
        inBurrow = b;
    }

    @Override
    protected void hunger(){
        if(!inBurrow){
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
