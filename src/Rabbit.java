import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Rabbit implements Actor, DynamicDisplayInformationProvider {
    private int hp = 3;


    public void act(World world) {
        List<Location> list = new ArrayList<>(world.getEmptySurroundingTiles());
        world.move(this, list.get(new Random().nextInt(list.size())));
        // if(hp <= 0) die(world);
        // if(world.getTile(world.getLocation(this)) instanceof Grass) eat(world, world.getLocation(this));
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

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(java.awt.Color.black, "rabbit-large");
    }
}
