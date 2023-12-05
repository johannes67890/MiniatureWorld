import java.util.Random;
import java.util.Set;

import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

public class Bear extends Animal implements Predator {
    private Location territoryC;
    private Set<Location> territory;

    private final int damage = 6;

    public Bear(Location territoryC, World world) {
        super(30, 30, 2);
        if (territoryC == null) {
            this.territoryC = new Location(new Random().nextInt(world.getSize()), new Random().nextInt(world.getSize()));
        } else {
            this.territoryC = territoryC;
        }
        this.territory = world.getSurroundingTiles(this.territoryC, 2);
    }

    public void act(World world) {

        if(life(world)){
            return;
        }

        // if starving
        if (starving) {
            if(food(world)){
                return;
            } else{
                move(getRandomEmptySurroundingTile(world), world);
                System.out.println("Bear moved random bc starving");
                return;
            }
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
        if(hungry){
            if(food(world)){
                return;
            }
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

    //function that first tries to eat else tries to go towards food
    private boolean food(World world) {
        // check around for rabbit and berries
        for (Location location : world.getSurroundingTiles()) {
            if (world.getTile(location) instanceof Rabbit) {
                eat(world.getTile(location), location, world);
                System.out.println("Bear eat Rabbit");
                return true;
            } else if (world.getTile(location) instanceof Bush) {
                Bush target = (Bush) world.getTile(location);
                if (target.getHasBerries()) {
                    hunger += 2;
                    target.eatBerries();
                    System.out.println("Bear eat berry");
                    return true;
                }
            }
        }

        // go towards rabbit or berries
        for (Location location : world.getSurroundingTiles(vision)) {
            if (world.getTile(location) instanceof Rabbit) {
                moveTowards(location, world);
                System.out.println("bear go towards rabbit");
                return true;
            } else if (world.getTile(location) instanceof Bush) {
                Bush target = (Bush) world.getTile(location);
                if (target.getHasBerries()) {
                    moveTowards(location, world);
                    System.out.println("bear go towards berry");
                    return true;
                }
            }
        }
        return false;
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
