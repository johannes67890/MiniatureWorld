import java.util.Random;

import itumulator.executable.DisplayInformation;
import itumulator.world.World;

public class Bear extends Animal implements Predator {

    public Bear() {
        super(30, 30, 1);
    }

    public void act(World world){
        // we generate a random number to deside what action the wolf will take
                    int r = new Random().nextInt(100);
                    if (r < 50){ // 50% chance to try to dig burrow
                        move(getRandomEmptySurroundingTile(world), world);
                        System.out.println("I moved random");
                    } 
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(java.awt.Color.black, "bear");
    }
    
}
