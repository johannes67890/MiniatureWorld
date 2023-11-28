import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

public class Rabbit implements Actor, DynamicDisplayInformationProvider {
    private int hp = 10;
    private int age = 0;
    private Burrow home = null;
    private Location location;

    /*
     * Rabbit(Location location){
     * this.location = location;
     * }
     */

    public Rabbit() {
        this.age = 3;
    }
    public void act(World world) {
        int r = new Random().nextInt(100);

        if(world.containsNonBlocking(world.getLocation(this))){
            if(home==null && world.getNonBlocking(world.getLocation(this)) instanceof Burrow) home = (Burrow) world.getNonBlocking(world.getLocation(this));
        }
         
        if (r < 10 && home==null) //10% chance to dig
            digBurrow(world);
        else if (r < 50) //40% chance to eat
            eat(world);
        else{ //50% chance to move
            if(new Random().nextInt(age+1) < 4 ) move(world); //less chance to move the older it is
        }
        reproduce(world);
        if(world.isNight() && home==null && r < 20) die(world);

        if(world.isNight() && world.getCurrentTime() == 19) hp--;

        if(hp <= 0 || age>7) die(world);

        if(world.getCurrentTime()==19) age++;
    }

    public void digBurrow(World world) {
        if (!world.containsNonBlocking(world.getLocation(this))) {
            home = new Burrow();
            world.setTile(world.getLocation(this), home);
        }
    }

    public void move(World world) {
        List<Location> list = new ArrayList<>(world.getEmptySurroundingTiles());
        location = list.get(new Random().nextInt(list.size()));
        world.move(this, location);
    }

    public void die(World world) {
        Location thisLocation = world.getLocation(this);
        world.delete(this);
        world.setTile(thisLocation, new Gravestone());
    }


    private void reproduce (World world) {
        int r = new Random().nextInt(8);
        for (Location tile : world.getSurroundingTiles()) {
            // if(world.getTile(tile) instanceof Rabbit && world.getTile(tile) != this && r == 0 && !(world.getTile(tile) instanceof BabyRabbit)) {                    
            //         world.setTile(getRandomSurroundingTile(world), new BabyRabbit());
            // } else continue;
        }
    }

    public Location getRandomSurroundingTile(World world) {
        List<Location> list = new ArrayList<>(world.getEmptySurroundingTiles());
        return list.get(new Random().nextInt(list.size()));
    }

    public void eat(World world) {
        Object o = null;
        if (world.containsNonBlocking(world.getLocation(this)))
            o = world.getNonBlocking(world.getLocation(this));
        if (o instanceof Grass) {
            world.delete(o);
            hp++;
        }
    }

    @Override
    public DisplayInformation getInformation() {
         return new DisplayInformation(java.awt.Color.black, "rabbit-large");
    }
}
