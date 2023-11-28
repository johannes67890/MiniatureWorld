import java.util.Random;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.World;

public class Wolf extends Animal implements Actor, DynamicDisplayInformationProvider {

    public Wolf(int hp) {
        super(hp);
    }

    public void act(World world){
        // we generate a random number to deside what action the wolf will take
                    int r = new Random().nextInt(100);
                    if (r < 50){ // 10% chance to try to dig burrow
                        move(getRandomEmptySurroundingTile(world), world);
                        System.out.println("I moved random");
                    } 
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(java.awt.Color.black, "wolf");
    }
    
}
