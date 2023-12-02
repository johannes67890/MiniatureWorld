import java.util.Random;

import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

public class Wolf extends Animal implements Predator{

    private final int damage = 6;

    public Wolf() {
        super(15, 30, 1);
    }

    public void act(World world) {

        if(life(world)){
            return;
        }

        // we generate a random number to deside what action the wolf will take
        int r = new Random().nextInt(100);
        if (r < 20) {
            move(getRandomEmptySurroundingTile(world), world);
            System.out.println("I moved random");
        } else if (r < 40) {
            System.out.println("I tried to eat");
            for(Location location : world.getSurroundingTiles()){
                if(world.getTile(location) instanceof Rabbit){
                    eat(world.getTile(location), location, world);
                    System.out.println("I eat");
                    break;
                }
            }
        }

        life(world);
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
