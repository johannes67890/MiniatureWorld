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
    public void act(World world) {
        List<Location> list = new ArrayList<>(world.getSurroundingTiles());
        world.move(this, list.get(new Random().nextInt(list.size())));
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(java.awt.Color.black, "rabbit-large");
    }
}
