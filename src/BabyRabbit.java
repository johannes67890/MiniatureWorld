import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.World;

public class BabyRabbit extends Rabbit implements  Actor, DynamicDisplayInformationProvider  {
    private int age = 0;
    BabyRabbit() {
        super();
    }

    @Override
    public void act(World world) {
        super.act(world);
        if(this.age >= 3) {
            world.delete(world.getLocation(this));
            world.setTile(world.getLocation(this), new Rabbit());
        }
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(java.awt.Color.black, "rabbit-small");
    }

}
