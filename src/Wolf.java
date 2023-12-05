import java.util.Random;

import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

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

        // if starving
        if (starving) {
            if (food(world)) {
                return;
            } else {
                move(getRandomEmptySurroundingTile(world), world);
                System.out.println("Wolf moved random bc starving");
                return;
            }
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
                    if (temp.getType() == "wolf" && !myPack.getHome(world).equals(temp)) {
                        moveAway(location, world);
                        System.out.println("Wolf moved away from other lair");
                        return;
                    }
                }
            }
        }

        // if hungry
        if (hungry) {
            if (food(world)) {
                return;
            }
        }

        // 50% for random move 50% for no move
        if (new Random().nextBoolean()) {
            move(getRandomEmptySurroundingTile(world), world);
            System.out.println("Wolf move Random");
            return;
        }
        System.out.println("Wolf do nothing");
    }

    // function that first tries to eat else tries to go towards food
    private boolean food(World world) {
        // check around for rabbits
        for (Location location : world.getSurroundingTiles()) {
            if (world.getTile(location) instanceof Rabbit) {
                eat(world.getTile(location), location, world);
                myPack.packEat(this);
                System.out.println("Wolf eat Rabbit");
                return true;
            }
        }
        // go towards rabbit
        for (Location location : world.getSurroundingTiles(vision)) {
            if (world.getTile(location) instanceof Rabbit) {
                moveTowards(location, world);
                System.out.println("Wolf go towards rabbit");
                return true;
            }
        }
        return false;
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

    public int getHp() {
        return hp;
    }

}