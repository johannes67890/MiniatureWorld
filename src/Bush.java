import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.World;

public class Bush implements Actor, DynamicDisplayInformationProvider {
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

    public void eatBerries(){
        hasBerries = false;
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
