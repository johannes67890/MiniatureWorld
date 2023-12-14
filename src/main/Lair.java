package main;

import java.util.ArrayList;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.NonBlocking;
import itumulator.world.World;

/**
 * Lair class
 * 
 * @param animals - ArrayList of {@link Animal}'s that is in the lair.
 * 
 * @implNote Implements {@link Actor}, {@link NonBlocking} and {@link DynamicDisplayInformationProvider}
 */
public class Lair implements Actor, NonBlocking, DynamicDisplayInformationProvider {
    private ArrayList<Animal> animals = new ArrayList<>();

    public void act(World world) {
        if (world.isDay()) {
            removeAnimals(world);
        }
    }
    /**
     * Adds an animal to the lair
     * @param animal - The animal to add
     */
    public void addAnimal(Animal animal, World world){
        animal.setInLair(true);
        animals.add(animal);
        if (world.isOnTile(animal)) {
            world.remove(animal);
        }
    }
    /**
     * Removes an animal from the lair
     */
    public void removeAnimals(World world){
        if(animals.size() > 0 && world.isTileEmpty(world.getLocation(this))) {
            world.setTile(world.getLocation(this), animals.get(0));
            animals.get(0).setInLair(false);
            animals.remove(0);
        }
    }

    public ArrayList<Animal> getAnimals() {
        return animals;
    }

    @Override
    public DisplayInformation getInformation() {
        if(this.getAnimals().getClass().isInstance(Rabbit.class)){
            return new DisplayInformation(java.awt.Color.black, "hole-small");
        }else if(this.getAnimals().getClass().isInstance(Wolf.class)){
            return new DisplayInformation(java.awt.Color.black, "hole");
        }
        return new DisplayInformation(java.awt.Color.black, "hole-small");

    }
}
