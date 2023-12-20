package main;

import java.util.ArrayList;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.NonBlocking;
import itumulator.world.World;

/**
 * Lair is a class that represents a burrow in the world. A lair can contain {@link Animal}'s. and is used by {@link Wolf} lairs and {@link Rabbit} burrows.
 * 
 * @param animals - ArrayList of {@link Animal}'s that is in the lair.
 * @param  type - The type of animal that is in the lair. (default: Rabbit)
 * 
 * @implNote Implements {@link Actor}, {@link NonBlocking} and {@link DynamicDisplayInformationProvider}
 */
public class Lair implements Actor, NonBlocking, DynamicDisplayInformationProvider {
    private ArrayList<Animal> animals = new ArrayList<>();
    private String type = "Rabbit";

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
    private void removeAnimals(World world){
        if(animals.size() > 0 && world.isTileEmpty(world.getLocation(this))) {
            world.setTile(world.getLocation(this), animals.get(0));
            animals.get(0).setInLair(false);
            animals.remove(0);
        }
    }
    // Get current animals in lair
    public ArrayList<Animal> getAnimalsInLair() {
        return animals;
    }

    // get current aniaml type in lair
    public String getType() {
        return type;
    }

    // set current animal type in lair
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public DisplayInformation getInformation() {
        if(this.getAnimalsInLair().getClass().isInstance(Rabbit.class)){
            return new DisplayInformation(java.awt.Color.black, "hole-small");
        }else if(this.getAnimalsInLair().getClass().isInstance(Wolf.class)){
            return new DisplayInformation(java.awt.Color.black, "hole");
        }
        return new DisplayInformation(java.awt.Color.black, "hole-small");

    }
}
