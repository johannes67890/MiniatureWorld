package test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import main.*;

public class CarcassTest {
    Program program;
    World world;

    @Before
    public void setUp() {
        program = new Program(2, 800, 100);
        world = program.getWorld();
    }

    // Test if an animal leaves a carcass when it dies
    // and if the the amount of mass is different for different animals
    @Test
    public void k3_1b() {
        Rabbit rabbit = new Rabbit();
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, rabbit);
        rabbit.die(world);
        //after the rabbit dies, there should be a carcass in its place
        assertEquals("Carcass", world.getTile(location).getClass().getSimpleName());
        //the mass of the carcass should be 3
        Carcass carcass = (Carcass) world.getTile(location);
        assertEquals(3, carcass.getMass());
        //we will now test with a wolf, which should have a mass of 5
        Wolf wolf = new Wolf(new WolfPack());
        location = new Location(0, 1);
        world.setCurrentLocation(location);
        world.setTile(location, wolf);
        wolf.die(world);
        carcass = (Carcass) world.getTile(location);
        assertEquals(5, carcass.getMass());
        //we will now test with an adult wolf which should have a mass of 10
        Wolf wolf2 = new Wolf(new WolfPack());
        location = new Location(1, 1);
        world.setCurrentLocation(location);
        world.setTile(location, wolf2);
        wolf2.isAdult = true;
        wolf2.die(world);
        carcass = (Carcass) world.getTile(location);
        assertEquals(10, carcass.getMass());
    }

    // Test if a carcass dissapears after a bit
    // Also test if a carcass dissapears after being eaten
    @Test
    public void k3_1c() {
        Carcass carcass = new Carcass(new Rabbit(), false);
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, carcass);
        while(world.getTile(location) == carcass){
            carcass.act(world);
        }
        assertEquals(true , world.getTile(location).getClass().getSimpleName() != "Carcass");
        //we will now test if a carcass dissapears after being eaten
        Wolf wolf = new Wolf(new WolfPack());
        location = new Location(1, 1);
        world.setCurrentLocation(location);
        world.setTile(location, wolf);
        Carcass carcass2 = new Carcass(new Rabbit(), false);
        location = new Location(0, 1);
        world.setCurrentLocation(location);
        world.setTile(location, carcass2);
        wolf.food(world);
        assertEquals(true , world.getTile(location).getClass().getSimpleName() != "Carcass");
    }

    // Test if a fungus can grow on a carcass
    @Test
    public void k3_2a() {
        Location location = new Location(0, 0);
        Carcass carcass = new Carcass(new Bear(location), false);
        world.setCurrentLocation(location);
        world.setTile(location, carcass);
        //There should only be the carcass entity in the world
        assertEquals(1, world.getEntities().size());
        carcass.addFungus(location, world);
        //There should now be a fungus entity in the world
        assertEquals(2, world.getEntities().size());
        while(world.getTile(location) == carcass){
            carcass.act(world);
            carcass.getFungus().act(world);
        }
        assertEquals("Fungus", world.getTile(location).getClass().getSimpleName());
    }

    // Test if a fungus can spread
    @Test
    public void k3_2b() {
        Location location = new Location(0, 0);
        Carcass carcass = new Carcass(new Bear(location), false);
        world.setCurrentLocation(location);
        world.setTile(location, carcass);
        carcass.addFungus(location, world);
        Carcass carcass2 = new Carcass(new Bear(location), false);
        location = new Location(0, 1);
        world.setCurrentLocation(location);
        world.setTile(location, carcass2);
        //There should only be the two carcasses and one fungus
        assertEquals(3, world.getEntities().size());
        carcass.getFungus().spread(world);
        assertEquals(4, world.getEntities().size());
        Fungus fungus = carcass.getFungus();
        Fungus fungus2 = carcass2.getFungus();
        //we will now wait until both carcasses have disappeared
        while(world.getTile(location) == carcass2){
            carcass2.act(world);
            fungus2.act(world);
        }
        location = new Location(0, 0);
        while(world.getTile(location) == carcass){
            carcass.act(world);
            fungus.act(world);
        }
        //chech if a fungus entity is on the tile, without a carcass
        assertEquals("Fungus", world.getTile(location).getClass().getSimpleName());
        //the fungi no longer have any place to spread to, so they die after some time
        while(world.getTile(location) == fungus){
            fungus.act(world);
        }
        assertEquals(null, world.getTile(location));
    }
}
