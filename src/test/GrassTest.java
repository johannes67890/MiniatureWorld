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
        program = new Program(2, 800, 100);
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
        rabbit.food(world);
        assertEquals(false, world.containsNonBlocking(location));
    }

    // Test if the grass can spread
    @Test
    public void k1_1c() {
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, grass);
        for(int i = 0; i < 3; i++){
            grass.spread(world);
        }
        //the grass should now cover the whole 2x2 area. We test to see if it is in one of those tiles
        location = new Location(0, 1);
        assertEquals("Grass", world.getNonBlocking(location).getClass().getSimpleName());
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
