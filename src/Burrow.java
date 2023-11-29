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
        rabbit.setInBurrow(true);
        rabbits.add(rabbit);
        world.remove(rabbit);
        System.out.println("Im in burrow");
    }

    public void removeRabbits(World world){
        if(rabbits.size() > 0 && world.isTileEmpty(world.getLocation(this))) {
            world.setTile(world.getLocation(this), rabbits.get(0));
            rabbits.get(0).setInBurrow(false);
            rabbits.remove(0);
            System.out.println("Im out of burrow");
        }
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(java.awt.Color.black, "hole");
    }
}
