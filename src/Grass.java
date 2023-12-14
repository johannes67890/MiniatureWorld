import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

public class Grass implements Actor, DynamicDisplayInformationProvider, NonBlocking {
    Random r = new Random(); // Random number generator

    /**
     * Makes the grass spread 
     * 
     * @param world - The world to act in.
     * 
     */
    public void act(World world) {
       spread(world);
    }
    
    /**
     * 
     */
    void spread(World world) {
         if (world.isDay() && r.nextInt(10) == 0) {
                List<Location> availableTiles = new ArrayList<>();
                for (Location l : world.getEmptySurroundingTiles()) {
                    if (!(world.getTile(l) instanceof NonBlocking)) {
                        availableTiles.add(l);
                    }
                }
                if (availableTiles.size() > 0) {
                    Location place = availableTiles.get(r.nextInt(availableTiles.size()));
                    world.setTile(place, new Grass());
                }
        }
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(java.awt.Color.green, "grass");
    }
}
