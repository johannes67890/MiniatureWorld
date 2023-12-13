package main;

import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;
import main.testReader.ClassTypes;

/**
 * Wolf class
 * 
 * @param damage - The damage the wolf does
 * @param myPack - The {@link Wolfpack} the wolf belongs to
 * 
 * @implNote Implements {@link Predator} and extends {@link Animal}
 */
public class Wolf extends Animal implements Predator {

    private final int damage = 6;
    private WolfPack myPack;

    public Wolf(WolfPack pack) {
        super(15, 30, 2);
        myPack = pack;
        myPack.addWolf(this);
    }

    public void act(World world) {
        System.out.println("Hunger: " + hunger + ", HP: " + hp);
        if (life(world)) {
            return;
        }

        // if in lair dont do anything
        if (isInLair) {
            return;
        }

        // if starving
        if (hunger <= 2) {
            findFood(Rabbit.class, world);
        }

        // if night move towards home
        if (world.isNight() && myPack.getHome(world) != null) {
            if (world.getLocation(this).equals(world.getLocation(myPack.getHome(world)))) {
                myPack.addToHome(this, world);
                System.out.println("Wolf enters home");
                return;
            }
            moveTowards(world.getLocation(myPack.getHome(world)), world);
            System.out.println("Wolf move to home");
            return;
        }

        // If far way from leader, go towards leader
        if (world.isOnTile(myPack.getLeader())) {
            if (!world.getLocation(myPack.getLeader()).equals(world.getLocation(this))) {
                boolean closeToLeader = false;
                for (Location location : world.getSurroundingTiles(vision)) {
                    if (location.equals(world.getLocation(myPack.getLeader()))) {
                        closeToLeader = true;
                        break;
                    }
                }
                if (!closeToLeader) {
                    moveTowards(world.getLocation(myPack.getLeader()), world);
                    System.out.println("Wolf moved closer to leader");
                    return;
                }
            }
        } 

        // if hungry
        if (hunger <= 7) {
            findFood(Rabbit.class, world);
        }

        // Attack if wolf is to close
        for (Location location : world.getSurroundingTiles()) {
            if (world.getTile(location) instanceof Wolf && !myPack.isInPack((Wolf) world.getTile(location))) {
                attack(location, world);
                System.out.println("Wolf attacked wolf");
                return;
            }
        }

        // Move away from other wolfs if in sight
        for (Location location : world.getSurroundingTiles(vision)) {
            if (world.getTile(location) instanceof Wolf && !myPack.isInPack((Wolf) world.getTile(location))) {
                moveAway(location, world);
                System.out.println("Wolf moved away from other wolf");
                return;
            }
            if (world.containsNonBlocking(location)) {
                if (world.getNonBlocking(location) instanceof Lair) {
                    Lair temp = (Lair) world.getNonBlocking(location);
                    if (temp.getType() == ClassTypes.wolf && !myPack.getHome(world).equals(temp)) {
                        moveAway(location, world);
                        System.out.println("Wolf moved away from other lair");
                        return;
                    }
                }
            }
        }
    }

    public int getHp() {
        return hp;
    }

    @Override
    protected void hunger() {
        if (!isInLair && hunger > 0) {
            hunger--;
        }
    }

    public void attack(Location location, World world) {
        Animal target = (Animal) world.getTile(location);
        target.takeDamage(damage);
    }

    public void addHunger(int x) {
        hunger += x;
    }

    @Override
    public DisplayInformation getInformation() {
        if (isAdult)
            return new DisplayInformation(java.awt.Color.black, "wolf");
        return new DisplayInformation(java.awt.Color.black, "wolf-small");
    }

}