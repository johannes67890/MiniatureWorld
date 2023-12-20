package test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import main.*;

public class BearTest {
    Program program;
    World world;
    Bear bear;

    @Before
    public void setUp() {
        program = new Program(2, 800, 100);
        world = program.getWorld();
        bear = new Bear(new Location(0, 0));
        
    }

    // Test if a bear can hunt and eat other animals and eat berries
    // Also covers k2-7a
    @Test
    public void k2_5b() {
        Rabbit rabbit = new Rabbit();
        Bush bush = new Bush();
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, bear);
        location = new Location(0, 1);
        world.setTile(location, bush);
        location = new Location(1, 0);
        world.setTile(location, rabbit);
        assertEquals(15, bear.getHunger());
        bear.food(world);
        assertEquals(18, bear.getHunger());
        for(int i = 0; i < 2; i++){
            bear.attackForFood(world);
            rabbit.life(world);
        }
        assertEquals("Carcass", world.getTile(location).getClass().getSimpleName());
        bear.food(world);
        assertEquals(21, bear.getHunger());
    }

    // Test if a rabbit runs away from a bear
    @Test
    public void k2_5c(){
        program = new Program(3, 800, 100);
        world = program.getWorld();
        Rabbit rabbit = new Rabbit();
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, bear);
        location = new Location(1, 1);
        world.setCurrentLocation(location);
        world.setTile(location, rabbit);
        rabbit.moveAwayFromPredator(world);
        //rabbit should have moved away from bear, and the tile is now empty
        assertEquals(null, world.getTile(location));
    }

    // Test if the bear protects its territory
    @Test
    public void k2_6a(){
        Rabbit rabbit = new Rabbit();
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, bear);
        bear.setTerritory(location, world);
        location = new Location(0, 1);
        world.setTile(location, rabbit);
        assertEquals(true, bear.protectTerritory(world));
    }

    // Test if wolves attack bears only if they are enough wolves
    @Test
    public void k2_8a(){
        WolfPack wolfPack = new WolfPack();
        world.add(wolfPack);
        Wolf wolf = new Wolf(wolfPack);
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, bear);
        location = new Location(0, 1);
        world.setCurrentLocation(location);
        world.setTile(location, wolf);
        assertEquals(false, wolf.attackForFood(world));
        Wolf wolf2 = new Wolf(wolfPack);
        location = new Location(1, 0);
        world.setCurrentLocation(location);
        world.setTile(location, wolf2);
        assertEquals(false, wolf.attackForFood(world));
        Wolf wolf3 = new Wolf(wolfPack);
        location = new Location(1, 1);
        world.setCurrentLocation(location);
        world.setTile(location, wolf3);
        assertEquals(true, wolf.attackForFood(world));
    }
}
