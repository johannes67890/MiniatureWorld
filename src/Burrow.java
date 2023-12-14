import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.NonBlocking;

public class Burrow implements NonBlocking, DynamicDisplayInformationProvider {


    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(java.awt.Color.black, "hole");
    }
}