package main;

import java.util.Set;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;
import java.util.Random;
/**
 * Bear class
 * 
 * @param territoryC - The center of the territory
 * @param territory - The territory of the bear
 * @implNote Implements {@link Predator} adn extends {@link Animal}

 */
public class Bear extends Animal implements Predator {
    private Location territoryC;
    private Set<Location> territory;

    private final int damage = 6;

    public Bear(Location territoryC) {
        super(30, 30, 2);
    }



    public void act(World world) {
        setTerritory(territoryC, world);
        if(life(world)){
            return;
        }

        // if starving
        if (hunger <= 2) {
            findFood(Rabbit.class, world);
            findFood(Bush.class, world);
        }

        // check around to attack
        for (Location location : territory) {
            for (Location view : world.getSurroundingTiles()) {
                if (location.equals(view) && world.getTile(view) instanceof Animal) {
                    attack(view, world);
                    System.out.println("Bear attack");
                    return;
                }
            }
        }

        // check territory and gotowards
        for (Location location : territory) {
            for (Location view : world.getSurroundingTiles(vision)) {
                if (location.equals(view) && world.getTile(view) instanceof Animal) {
                    moveTowards(view, world);
                    System.out.println("Bear move to attack");
                    return;
                }
            }
        }

        //if hungry
        if(hunger<=7){
            findFood(Rabbit.class, world);
           findFood(Bush.class, world);
        }

        //move from center
        if(world.getLocation(this).equals(territoryC)){
            move(getRandomEmptySurroundingTile(world), world);
            System.out.println("Bear move from C");
            return;
        }

        //move towards center
        moveTowards(territoryC, world);
        System.out.println("Bear move towards C");

        
    }

    public void setTerritory(Location territoryC, World world){
        if (territoryC == null) {
            this.territoryC = new Location(new Random().nextInt(world.getSize()), new Random().nextInt(world.getSize()));
        } else {
            this.territoryC = territoryC;
        }
        this.territory = world.getSurroundingTiles(this.territoryC, 2);
    }

    public void attack(Location location, World world) {
        Animal target = (Animal) world.getTile(location);
        target.takeDamage(damage);
    }

    @Override
    public DisplayInformation getInformation() {
        if (isAdult)
            return new DisplayInformation(java.awt.Color.black, "bear");
        return new DisplayInformation(java.awt.Color.black, "bear-small");
    }

}
