package main;

import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

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
        if (life(world)) {
            return;
        }

        //if in lair dont do anything
        if(isInLair){
            return;
        }

        // if starving
        if (hunger <= 2) {
            findFood(Rabbit.class, world);
        }

        // if night move towards home
        if (world.isNight() && myPack.hasHome()) {
            if (world.getLocation(this).equals(myPack.getHomeLocation(world))) {
                myPack.addToHome(this, world);
                System.out.println("Wolf enters home");
                return;
            }
            moveTowards(myPack.getHomeLocation(world), world);
            System.out.println("Wolf move to home");
            return;
        }

        // If far way from leader, go towards leader
        if (!myPack.getLeaderLocation(world).equals(world.getLocation(this))) {
            boolean closeToLeader = false;
            for (Location location : world.getSurroundingTiles(vision)) {
                if (location.equals(myPack.getLeaderLocation(world))) {
                    closeToLeader = true;
                    break;
                }
            }
            if (!closeToLeader) {
                moveTowards(myPack.getLeaderLocation(world), world);
                System.out.println("Wolf moved closer to leader");
                return;
            }
        } 

        // if hungry
        if (hunger <= 7) {
            findFood(Rabbit.class, world);
        }

        //if leader move
        if(myPack.getLeaderLocation(world).equals(world.getLocation(this))){
            move(getRandomEmptySurroundingTile(world), world);
        }
    }

    public int getHp(){
        return hp;
    }

    @Override
    protected void hunger() {
        if (!isInLair) {
            hunger--;
        }
    }

    public void attack(Location location, World world) {
        Animal target = (Animal) world.getTile(location);
        target.takeDamage(damage);
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(java.awt.Color.black, "wolf");
    }
}