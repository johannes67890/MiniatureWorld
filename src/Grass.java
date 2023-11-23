import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.World;

public class Grass implements Actor, DynamicDisplayInformationProvider {
    public void act(World world){

    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(java.awt.Color.green, "grass");
    }
}
