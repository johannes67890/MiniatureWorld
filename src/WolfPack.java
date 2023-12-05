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

        // find leader
        leader = pack.get(0);
        for (Wolf wolf : pack) {
            if (wolf.getHp() > leader.getHp()) {
                leader = wolf;
            }
        }

        // Set lair
        if (home == null) {
            if (!world.containsNonBlocking(world.getLocation(getLeader()))) {
                home = new Lair();
                world.setTile(world.getLocation(getLeader()), home);
            }
        }

        // Reproduce in lair
        if (home != null) {
            if (world.getCurrentTime() == 14 && home.getAmountInLair() >= 2) {
                Wolf newWolf = new Wolf(this);
                world.add(newWolf);
                home.addAnimal(newWolf, world);
                System.out.println("Pack reproduced");
            }
        }
    }

    public void addWolf(Wolf wolf) {
        pack.add(wolf);
    }

    public Wolf getLeader() {
        if (leader == null) {
            return pack.get(0);
        }
        return leader;
    }

    public void addToHome(Wolf wolf, World world) {
        home.addAnimal(wolf, world);
    }

    public Location getHomeLocation(World world) {
        if(home == null){
            return null;
        }
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
