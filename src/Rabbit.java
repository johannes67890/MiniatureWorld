import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Rabbit implements Actor, DynamicDisplayInformationProvider {


    public void act(World world) {
        List<Location> list = new ArrayList<>(world.getSurroundingTiles());
        world.move(this, list.get(new Random().nextInt(list.size())));
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

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(java.awt.Color.black, "rabbit-large");
    }
}
