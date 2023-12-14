import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.NonBlocking;
import itumulator.world.World;



public class Gravestone implements Actor, DynamicDisplayInformationProvider {

    public void act(World world) {
        if(world.getCurrentTime() == 19) world.delete(this);;
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(java.awt.Color.gray, "gravestone", true);
    }
}

