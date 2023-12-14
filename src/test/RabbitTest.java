package test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import main.*;

public class RabbitTest {

    Rabbit rabbit;
    World world;
    Program program;

    @Before
    public void setUp()  {
        program = new Program(1, 800, 100);
        world = program.getWorld();
        rabbit = new Rabbit();
    }

    // Test if a rabbit can die of old age
    @Test
    public void k1_2b() {
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, rabbit);
        for (int i = 0; i < 16; i++) {
            rabbit.act(world);
        }
        assertEquals("main.Carcass", world.getTile(location).getClass().getName());
    }

    // Test if a rabbit can eat grass, which can increase its hunger level
    @Test
    public void k1_2c() {
        Grass grass = new Grass();
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, rabbit);
        world.setTile(location, grass);
        assertEquals(15, rabbit.getHunger());
        for (int i = 0; i < 4; i++) {
            rabbit.act(world);
        }
        assertEquals(11, rabbit.getHunger());
        rabbit.act(world);
        assertEquals(13, rabbit.getHunger());

    }

    // Test if a rabbit can reproduce
    @Test
    public void k1_2e() {
        program = new Program(2, 800, 100);
        world = program.getWorld();
        Rabbit rabbit2 = new Rabbit();
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, rabbit);
        location = new Location(0, 1);
        world.setCurrentLocation(location);
        world.setTile(location, rabbit2);
        while(world.getEntities().size() < 3){
            rabbit.act(world);
        }
        assertEquals(3, world.getEntities().size());
    }

    // Test if a rabbit can dig a hole
    @Test
    public void k1_2f() {
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, rabbit);
        while(!(world.containsNonBlocking(location))){
            rabbit.act(world);
        }
        assertEquals("main.Lair", world.getNonBlocking(location).getClass().getName());
    }
    // Test if a rabbit can go into lair at night
    // Also covers k1-3b
    @Test
    public void k1_2g() {
        Lair lair = new Lair("rabbit");
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, rabbit);
        world.setTile(location, lair);
        world.setNight();
        rabbit.act(world);
        assertEquals(true, world.getTile(location) instanceof Lair);
    }
    
}
