package main;

import java.util.ArrayList;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

/**
 * WolfPack class
 * 
 * @param pack - The pack of wolves
 * @param home - The lair of the pack
 * @param leader - The leader of the pack
 * 
 * @implNote Implements {@link Actor}, {@link DynamicDisplayInformationProvider} and {@link NonBlocking}
 */
public class WolfPack implements Actor, DynamicDisplayInformationProvider, NonBlocking {
    private ArrayList<Wolf> pack = new ArrayList<>();
    private Lair home = null;
    private Wolf leader;

    public void act(World world) {

        if(leader==null){
            leader = getStrongest();
        }

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
    
    public void setLeader(Wolf wolf) {
        leader = wolf;
    }

    public ArrayList<Wolf> getPack() {
        return pack;
    }

    /**
     * Gets the strongest wolf in the pack (by hp)
     * @return The strongest wolf in the pack
     */
    public Wolf getStrongest() {
        Wolf strongest = pack.get(0);
        for (Wolf wolf : pack) {
            if (wolf.getHp() > strongest.getHp()) {
                strongest = wolf;
            }
        }
        return strongest;
    }

    
    public Location getLeaderLocation(World world) {
        if(leader==null){
            return world.getLocation(pack.get(0));
        }
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
