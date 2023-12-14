package test;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import main.Grass;
import main.Rabbit;

public class GrassTest {
    Grass grass;
    World world;
    Program program;

    @Before
    public void setUp() {
        program = new Program(1, 800, 100);
        world = program.getWorld();
        grass = new Grass();
    }

    // We have to test if the grass can dissapear.
    // We decided the only way to do this, is to have a rabbit eat the grass.
    @Test
    public void k1_1b() {

        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, grass);
        Rabbit rabbit = new Rabbit();
        world.setTile(location, rabbit);
        for (int i = 0; i < 5; i++) {
            rabbit.act(world);
        }
        assertEquals(false, world.containsNonBlocking(location));
    }

    // Test if the grass can spread
    @Test
    public void k1_1c() {
        program = new Program(2, 800, 100);
        world = program.getWorld();
        grass = new Grass();
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, grass);
        for(int i = 0; i < 100; i++){
            grass.act(world);
        }
        location = new Location(0, 1);
        assertEquals(true, world.containsNonBlocking(location));
    }

    // Test if an animal can be on the same tile as grass
    @Test
    public void k1_1d() {
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, grass);
        Rabbit rabbit = new Rabbit();
        world.setTile(location, rabbit);
        assertEquals(true, world.containsNonBlocking(location) && world.getTile(location) instanceof Rabbit);
    }
}
