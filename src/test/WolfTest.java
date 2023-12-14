package test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import main.*;

public class WolfTest {
    Program program;
    World world;
    Wolf wolf;
    WolfPack wolfPack;
    @Before
    public void setUp() {
        program = new Program(1, 800, 100);
        world = program.getWorld();
        WolfPack wolfPack = new WolfPack();
        wolf = new Wolf(wolfPack);
    }

    // Test if a wolf can die, in this case of old age
    @Test
    public void k1_1b() {
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, wolf);
        for (int i = 0; i < 29; i++) {
            wolf.act(world);
        }
        assertEquals("main.Carcass", world.getTile(location).getClass().getName());
    }

    // Test if wolves can hunt and eat rabbits
    @Test
    public void k1_1c() {
        program = new Program(2, 800, 100);
        world = program.getWorld();
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, wolf);
        location = new Location(0, 1);
        Rabbit rabbit = new Rabbit();
        world.setTile(location, rabbit);
        assertEquals(15, wolf.getHunger());
        // Wait for enough steps until the wolf is hungry enough to hunt
        for (int i = 0; i < 6; i++) {
            wolf.act(world);
            rabbit.act(world);
        }
        // Final check that the food is at the expected value before hunting
        assertEquals(9, wolf.getHunger());
        wolf.act(world);
        // Check if the wolf has eaten the rabbit and increases its hunger level
        assertEquals(12, wolf.getHunger());
    }

    // Test if wolves can have a lair and reproduce
    @Test
    public void k2_3a_1() {
        program = new Program(2, 800, 100);
        world = program.getWorld();
        wolfPack = new WolfPack();
        world.add(wolfPack);
        world.setNight();
        wolf = new Wolf(wolfPack);
        Wolf wolf2 = new Wolf(wolfPack);
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, wolf);
        location = new Location(0, 1);
        world.setCurrentLocation(location);
        world.setTile(location, wolf2);
        // Set the age of the wolves to 3, as children can't reproduce
        wolf.setAge(3);
        wolf2.setAge(3);
        assertEquals(2, wolfPack.getPack().size());
        for (int i = 0; i < 10; i++) {
            world.step();
            wolf.act(world);
            wolf2.act(world);
            wolfPack.act(world);
        }
        assertEquals(3, wolfPack.getPack().size());
    }


}
