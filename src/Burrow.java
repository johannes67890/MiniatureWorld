import java.util.ArrayList;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.NonBlocking;
import itumulator.world.World;

public class Burrow implements Actor, NonBlocking, DynamicDisplayInformationProvider {
    private ArrayList<Rabbit> rabbits = new ArrayList<>();

    public void act(World world){
        if(world.isDay()){
            removeRabbits(world);
        }
    }

    public void addRabbit(Rabbit rabbit, World world){
        rabbits.add(rabbit);
        world.remove(rabbit);
    }

    public void removeRabbits(World world){
        if(rabbits.size() > 0 && world.isTileEmpty(world.getLocation(this))) {
            world.setTile(world.getLocation(this), rabbits.get(0));
            rabbits.get(0).setInBurrowFalse();
            System.out.println("out of burrow");
            rabbits.remove(0);
        }
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(java.awt.Color.black, "hole");
    }
}
