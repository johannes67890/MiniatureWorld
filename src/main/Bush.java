package main;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.World;

/**
 * Bush class
 * 
 * @param hasBerries - Whether the bush has berries or not
 * @param regrowTime - The time it takes for the bush to regrow berries
 * 
 * @implNote Implements {@link Actor} and {@link DynamicDisplayInformationProvider}
 */
public class Bush extends Eatable implements Actor, DynamicDisplayInformationProvider {
    private boolean hasBerries = true;
    private int regrowTime = 5;
    public void act(World world){
        //regrows berries after some time
        if(!hasBerries && world.isDay()){
            regrowTime--;
            if(regrowTime <= 0){
                hasBerries = true;
                regrowTime = 5;
            }
        }
    }

    public int getEaten(int bideSize, World world){
        hasBerries = false;
        return 2;
    }
    
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
