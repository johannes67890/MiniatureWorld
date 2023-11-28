import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Rabbit implements Actor, DynamicDisplayInformationProvider {
    private int hp = 10;
    public int age = 0;
    private final int adultAge = 3;
    private Burrow home = null;
    private boolean inBurrow = false;

    public Rabbit() {
        this.age = 3;
    }

    public void act(World world) {
        if (world.isNight() && home == null) {
            die(world);
            return;
        }

        int r = new Random().nextInt(100);
        if (world.isDay() && !inBurrow) {
            if (world.containsNonBlocking(world.getLocation(this))) {
                if (home == null && world.getNonBlocking(world.getLocation(this)) instanceof Burrow)
                    home = (Burrow) world.getNonBlocking(world.getLocation(this));
            } // if the rabbit is on a burrow, and doesn't have a hom, set the burrow as its
              // home
            if (r < 10 && home == null) // 10% chance to dig
                digBurrow(world);
            else if (r < 50) // 40% chance to eat
                eat(world);
            else { // 50% chance to move
                if (new Random().nextInt(age + 1) < 4)
                    move(world); // less chance to move the older it is
            }
            // reproduce(world);
        } else if (world.isNight() && !inBurrow) {
            // if(home != null){ // if the rabbit has a home, move towards it if it's night
            int homeX = world.getLocation(home).getX();
            int homeY = world.getLocation(home).getY();
            int rabbitX = world.getLocation(this).getX();
            int rabbitY = world.getLocation(this).getY();
            if (homeX > rabbitX && homeY > rabbitY) {
                world.move(this, new Location(rabbitX + 1, rabbitY + 1));
            } else if (homeX > rabbitX && homeY < rabbitY) {
                world.move(this, new Location(rabbitX + 1, rabbitY - 1));
            } else if (homeX < rabbitX && homeY > rabbitY) {
                world.move(this, new Location(rabbitX - 1, rabbitY + 1));
            } else if (homeX < rabbitX && homeY < rabbitY) {
                world.move(this, new Location(rabbitX - 1, rabbitY - 1));
            } else if (homeX == rabbitX && homeY > rabbitY) {
                world.move(this, new Location(rabbitX, rabbitY + 1));
            } else if (homeX == rabbitX && homeY < rabbitY) {
                world.move(this, new Location(rabbitX, rabbitY - 1));
            } else if (homeX > rabbitX && homeY == rabbitY) {
                world.move(this, new Location(rabbitX + 1, rabbitY));
            } else if (homeX < rabbitX && homeY == rabbitY) {
                world.move(this, new Location(rabbitX - 1, rabbitY));
            } else if (homeX == rabbitX && homeY == rabbitY) {
                inBurrow = true;
                home.addRabbit(this, world);
            }
        }

        // if(world.isNight()) hp--;

        if (hp <= 0 || age > 20)
            die(world);

        if (world.getCurrentTime() == 19)
            age++;

    }

    public void digBurrow(World world) {
        if (!world.containsNonBlocking(world.getLocation(this))) {
            home = new Burrow();
            world.setTile(world.getLocation(this), home);
        }
    }

    public void move(World world) {
        world.move(this, getRandomSurroundingTile(world));

    }

    public void die(World world) {
        world.delete(this);
    }

    private void reproduce(World world) {
        int r = new Random().nextInt(8);
        for (Location tile : world.getSurroundingTiles()) {
            Location l = getRandomSurroundingTile(world);
            if (world.getTile(tile) instanceof Rabbit && world.getTile(tile) != this && r == 0 && l!=null
                    && !(world.getTile(tile) instanceof BabyRabbit)) {
                System.out.println("Rabbit is reproducing");
                    world.setTile(l, new BabyRabbit());
            } else
                continue;
        }
    }

    public Location getRandomSurroundingTile(World world) {
        List<Location> list = new ArrayList<>(world.getEmptySurroundingTiles());
        if (list.size() > 0)
            return list.get(new Random().nextInt(list.size()));
        else
            return null;
    }

    public void eat(World world) {
        Object o = null;
        if (world.containsNonBlocking(world.getLocation(this)))
            o = world.getNonBlocking(world.getLocation(this));
        if (o instanceof Grass) {
            world.delete(o);
            hp++;
        }
    }

    public void setInBurrowFalse() {
        inBurrow = false;
        System.out.println("TEST");
    }

    @Override
    public DisplayInformation getInformation() {
        if (age <= adultAge)
            return new DisplayInformation(java.awt.Color.black, "rabbit-small");
        return new DisplayInformation(java.awt.Color.black, "rabbit-large");
    }
}
