import java.util.Set;

import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

public class Bear extends Predator {
    private Location territoryC;
    private Set<Location> territory;

    public Bear(Location territoryC, World world) {
        super(30, 30, 2, 6);
        if (territoryC == null) {
            this.territoryC = world.getLocation(this);
        } else {
            this.territoryC = territoryC;
        }
        this.territory = world.getSurroundingTiles(this.territoryC, 2);
    }

    public void act(World world) {
        // if starving
        if (hunger <= 2) {
            if(food(world)){
                System.out.println("Bear food");
                return;
            }
        }

        // check around to attack
        for (Location location : territory) {
            for (Location view : world.getSurroundingTiles()) {
                if (location.equals(view) && world.getTile(view) instanceof Animal) {
                    attack(view, damage, world);
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
            if(food(world)){
                System.out.println("Bear food");
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

        life(world);
    }

    private boolean food(World world) {
        // check around for rabbit and berries
        for (Location location : world.getSurroundingTiles()) {
            if (world.getTile(location) instanceof Rabbit) {
                eat(world.getTile(location), location, world);
                System.out.println("I eat Rabbit");
                return true;
            } else if (world.getTile(location) instanceof Bush) {
                Bush target = (Bush) world.getTile(location);
                if (target.getHasBerries()) {
                    hunger += 2;
                    target.eatBerries();
                    System.out.println("I ate berry");
                    return true;
                }
            }
        }

        // go towards rabbit or berries
        for (Location location : world.getSurroundingTiles(vision)) {
            if (world.getTile(location) instanceof Rabbit) {
                moveTowards(location, world);
                return true;
            } else if (world.getTile(location) instanceof Bush) {
                Bush target = (Bush) world.getTile(location);
                if (target.getHasBerries()) {
                    moveTowards(location, world);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(java.awt.Color.black, "bear");
    }

}
