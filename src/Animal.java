import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import itumulator.world.Location;
import itumulator.world.World;

public abstract class Animal {
    protected int hp, age;

    protected Animal(int hp) {
        this.hp = hp;
        this.age = 0;
    }

    protected void die(World world) {
        world.delete(this);
    }

    protected void move(Location location, World world) {
        if(location!=null){
            if(world.isTileEmpty(location)){
                world.move(this, location);
            }
        }
    }

    protected Location getRandomEmptySurroundingTile(World world) {
        List<Location> list = new ArrayList<>(world.getEmptySurroundingTiles());
        if (list.size() == 0) return null;
        return list.get(new Random().nextInt(list.size()));
    }

    protected <T> void eat(T food, World world) {
        if (world.containsNonBlocking(world.getLocation(this))){
            Object object = world.getNonBlocking(world.getLocation(this));
            if(object.getClass().equals(food.getClass())){
                world.delete(object);
                hp++;
            }
        }
    }

}
