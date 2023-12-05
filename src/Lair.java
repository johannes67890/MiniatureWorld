import java.util.ArrayList;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.NonBlocking;
import itumulator.world.World;

public class Lair implements Actor, NonBlocking, DynamicDisplayInformationProvider {
    private ArrayList<Animal> animals = new ArrayList<>();

    public void act(World world){
        if(world.isDay()){
            removeAnimals(world);
        }
    }

    public void addAnimal(Animal animal, World world){
        animal.setInLair(true);
        animals.add(animal);
        if(world.isOnTile(animal)){
            world.remove(animal);
        }
    }

    public void removeAnimals(World world){
        if(animals.size() > 0 && world.isTileEmpty(world.getLocation(this))) {
            world.setTile(world.getLocation(this), animals.get(0));
            animals.get(0).setInLair(false);
            animals.remove(0);
        }
    }

    public int getAmountInLair(){
        return animals.size();
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(java.awt.Color.black, "hole");
    }
}
