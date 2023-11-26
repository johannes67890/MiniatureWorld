import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Rabbit implements Actor, DynamicDisplayInformationProvider {
    public int age;

    public Rabbit() {
        this.age = 3;
    }
    public void act(World world) {
        List<Location> list = new ArrayList<>(world.getEmptySurroundingTiles());
        world.move(this, list.get(new Random().nextInt(list.size())));
        reproduce(world);
        // if(hp <= 0) die(world);
    }

    public void digHole(World world, Location location) {
        if(world.getTile(location) instanceof Grass) {
            world.remove(location);
            world.setTile(location, new Hole());
        }
        new Hole();
    }

    public void move(World world, Location location) {
        if(world.getTile(location) instanceof Hole) throw new RuntimeException("Rabbit cannot move to a hole");
        world.move(this, location);
    }

    // public void die(World world) {
    //     world.remove(world.getLocation(this));
    // }

    // public void eat(World world, Location location) {
    //     if(world.getTile(location) instanceof Grass) {
    //         world.remove(location);
    //     }
    // }
    private void reproduce (World world) {
        int r = new Random().nextInt(8);
        for (Location tile : world.getSurroundingTiles()) {
            if(world.getTile(tile) instanceof Rabbit && world.getTile(tile) != this && r == 0 && !(world.getTile(tile) instanceof BabyRabbit)) {
                System.out.println("Rabbit is reproducing");
                    
                    world.setTile(getRandomSurroundingTile(world), new BabyRabbit());
                

            } else continue;
        }
    }

    public Location getRandomSurroundingTile(World world) {
        List<Location> list = new ArrayList<>(world.getEmptySurroundingTiles());
        return list.get(new Random().nextInt(list.size()));
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(java.awt.Color.black, "rabbit-large");
    }
}
