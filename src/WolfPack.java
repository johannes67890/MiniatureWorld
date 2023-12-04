import java.util.ArrayList;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

public class WolfPack implements Actor, DynamicDisplayInformationProvider, NonBlocking {
    private ArrayList<Wolf> pack = new ArrayList<>();
    private Lair home = null;
    private Wolf leader;

    public void act(World world) {

        if (home == null) {
            if (!world.containsNonBlocking(getLeaderLocation(world))) {
                home = new Lair();
                world.setTile(getLeaderLocation(world), home);
            }
        }
    }

    public void addWolf(Wolf wolf) {
        pack.add(wolf);
    }

    public Location getLeaderLocation(World world) {
        return world.getLocation(leader);
    }

    public void addToHome(Wolf wolf, World world) {
        home.addAnimal(wolf, world);
    }

    public Location getHomeLocation(World world) {
        return world.getLocation(home);
    }

    public boolean hasHome() {
        if (home == null) {
            return false;
        }
        return true;
    }

    public DisplayInformation getInformation() {
        return new DisplayInformation(java.awt.Color.black);
    }

}
