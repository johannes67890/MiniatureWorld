
package main;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.World;

/**
 * The Bush Class
 * 
 * The bush can have berries and has a regrow time for the berries.
 * 
 * @param hasBerries - Whether the bush has berries or not
 * @param regrowTime - The time it takes for the bush to regrow berries
 * 
 * @implNote Implements {@link Actor} and {@link DynamicDisplayInformationProvider}
 */
public class Bush extends Eatable implements Actor, DynamicDisplayInformationProvider {
    private boolean hasBerries = true;
    private int regrowTime = 8;

    public void act(World world){
        //regrows berries after some time
        if(!hasBerries && world.isDay()){
            regrowTime--;
            if(regrowTime <= 0){
                hasBerries = true;
                regrowTime = 8;
            }
        }
    }
    /*
     * Returns a amount of hunger gained after eating the berries. (The default return is 2)
     */
    public int getEaten(int biteSize, World world){
        if(hasBerries){
            hasBerries = false;
            return 3;
        }
        return 0;
    }
    
    /*
     * Returns whether the bush has berries or not.
     */
    public boolean getHasBerries(){
        return hasBerries;
    }

    @Override
    public DisplayInformation getInformation() {
        if(hasBerries){
            return new DisplayInformation(java.awt.Color.red, "bush-berries");
        }
        return new DisplayInformation(java.awt.Color.green, "bush");
    }
}
