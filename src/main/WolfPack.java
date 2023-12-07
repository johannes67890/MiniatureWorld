package main;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import java.util.ArrayList;

/**
 * WolfPack class
 *
 * @param pack - The pack of wolves
 * @param home - The lair of the pack
 * @param leader - The leader of the pack
 *
 * @implNote Implements {@link Actor}, {@link DynamicDisplayInformationProvider} and {@link NonBlocking}
 */
public class WolfPack
  implements Actor, DynamicDisplayInformationProvider, NonBlocking {

  private ArrayList<Wolf> pack = new ArrayList<>();
  private Lair home = null;
  private Wolf leader;

  public void act(World world) {
    leader = getStrongest();

    // Set lair
    if (home == null) {
      if (!world.containsNonBlocking(world.getLocation(getLeader()))) {
        home = new Lair("wolf");
        world.setTile(world.getLocation(getLeader()), home);
      }
    }

    // Reproduce in lair
    if (home != null) {
      int adultsInLair = 0;
      for (Animal wolf : home.getAnimals()) {
        if (wolf.isAdult) {
          adultsInLair++;
        }
      }
      if (world.getCurrentTime() == 18 && adultsInLair >= 2) {
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

  public Wolf getLeader() {
    if (leader == null) {
      return pack.get(0);
    }
    return leader;
  }

  public void addToHome(Wolf wolf, World world) {
    home.addAnimal(wolf, world);
  }

  public boolean isInPack(Wolf wolf) {
    if (pack.contains(wolf)) {
      return true;
    }
    return false;
  }

  public void packEat(Wolf mySelf) {
    for (Wolf wolf : pack) {
      if (!wolf.equals(mySelf)) {
        wolf.addHunger(1);
      }
    }
  }

  public Lair getHome(World world) {
    return home;
  }

  public DisplayInformation getInformation() {
    return new DisplayInformation(java.awt.Color.black);
  }
}
