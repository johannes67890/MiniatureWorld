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
        program = new Program(2, 800, 100);
        world = program.getWorld();
        rabbit = new Rabbit();
    }

    // Test if a rabbit can die
    @Test
    public void k1_2b() {
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, rabbit);
        assertEquals("main.Rabbit", world.getTile(location).getClass().getName());
        rabbit.die(world);
        assertEquals("main.Carcass", world.getTile(location).getClass().getName());
    }

    // Test if a rabbit can eat grass, which can increase its hunger level 
    // and test that the rabbit dies without food
    @Test
    public void k1_2c() {
        Grass grass = new Grass();
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, rabbit);
        world.setTile(location, grass);
        //default value of hunger level for rabbit is 15
        assertEquals(15, rabbit.getHunger());
        //grass provides a hunger value of 3
        rabbit.food(world);
        //simulate the rabbit not getting food for 27 steps
        //first 18 steps should get the hunger level to 0, next 9 steps should get the hp to 0
        assertEquals(18, rabbit.getHunger());
        for(int i = 0; i < 27; i++){
            rabbit.life(world);
        }
        //the rabbit should now have died of starvation
        assertEquals("main.Carcass", world.getTile(location).getClass().getName());
    }

    // We determined that our way of making k1-2d was not possible to test, as it is a random event.

    // Test if a rabbit can reproduce
    @Test
    public void k1_2e() {
        Rabbit rabbit2 = new Rabbit();
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, rabbit);
        location = new Location(0, 1);
        world.setCurrentLocation(location);
        world.setTile(location, rabbit2);
        //set both rabbits to adult, so they are allowed to reproduce
        rabbit.isAdult = true;
        rabbit2.isAdult = true;
        rabbit.reproduce(world);
        assertEquals(3, world.getEntities().size());
    }

    // Test if a rabbit can dig a hole
    @Test
    public void k1_2f() {
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, rabbit);
        rabbit.digBurrow(world);
        assertEquals("Lair", world.getNonBlocking(location).getClass().getSimpleName());
    }
    // Test if a rabbit can go into lair at night
    // Also covers k1-3b
    @Test
    public void k1_2g() {
        Lair lair = new Lair();
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, rabbit);
        world.setTile(location, lair);
        world.setNight();
        rabbit.act(world);
        assertEquals(false, world.getTile(location) instanceof Rabbit);
    }
    
}
