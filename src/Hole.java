import itumulator.executable.DisplayInformation;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import itumulator.world.Location;

public class Hole extends Rabbit implements NonBlocking {
    Hole(Location location, World world){
        super(location, world);
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(java.awt.Color.black, "hole");
    }
}
