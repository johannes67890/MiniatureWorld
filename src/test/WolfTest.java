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
        program = new Program(2, 800, 100);
        world = program.getWorld();
        WolfPack wolfPack = new WolfPack();
        wolf = new Wolf(wolfPack);
    }

    // Test if a wolf can die
    @Test
    public void k1_1b() {
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, wolf);
        wolf.die(world);
        assertEquals("Carcass", world.getTile(location).getClass().getSimpleName());
    }

    // Test if wolves can hunt and eat rabbits
    @Test
    public void k1_1c() {
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, wolf);
        location = new Location(0, 1);
        Rabbit rabbit = new Rabbit();
        world.setTile(location, rabbit);
        assertEquals(15, wolf.getHunger());
        for(int i = 0; i < 2; i++){
            wolf.attackForFood(world);
            rabbit.life(world);
        }
        assertEquals("Carcass", world.getTile(location).getClass().getSimpleName());
        wolf.food(world);
        assertEquals(18, wolf.getHunger());
    }

    // Test if wolves can have a lair and reproduce
    @Test
    public void k2_3a_1() {
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

        // make both wolves adults so they can reproduce
        wolf.isAdult = true;
        wolf2.isAdult = true;
        // check if the pack size is 2 as expected
        assertEquals(2, wolfPack.getPack().size());
        wolfPack.makeLair(world);
        wolfPack.addToHome(wolf, world);
        wolfPack.addToHome(wolf2, world);
        wolfPack.reproduce(world);
        assertEquals(3, wolfPack.getPack().size());
    }

    // Test if wolves from different packs move away if they see each other but are not next to each other
    @Test
    public void k2_3a_2() {
        program = new Program(4, 800, 100);
        world = program.getWorld();
        WolfPack wolfPack2 = new WolfPack();
        world.add(wolfPack2);
        Wolf wolf2 = new Wolf(wolfPack2);
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, wolf);
        location = new Location(2, 2);
        world.setCurrentLocation(location);
        world.setTile(location, wolf2);
        wolf2.act(world);
        //wolf2 should have moved away from wolf, as they can see eachother
        assertEquals(null, world.getTile(location));
    }

    // Test if wolves from different packs attack each other if they are next to each other
    @Test
    public void k2_3a_3() {
        WolfPack wolfPack2 = new WolfPack();
        world.add(wolfPack2);
        Wolf wolf2 = new Wolf(wolfPack2);
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, wolf);
        location = new Location(0, 1);
        world.setCurrentLocation(location);
        world.setTile(location, wolf2);
        assertEquals(15, wolf.getHp());
        wolf2.act(world);
        //wolf2 should have attacked wolf now, and wolf should have lower HP than before
        assertEquals(9, wolf.getHp());
    }
    
    // Test if rabbits runs away from wolves
    @Test
    public void k2_4a() {
        program = new Program(3, 800, 100);
        world = program.getWorld();
        Rabbit rabbit = new Rabbit();
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, wolf);
        location = new Location(1, 1);
        world.setCurrentLocation(location);
        world.setTile(location, rabbit);
        rabbit.moveAwayFromPredator(world);
        //rabbit should have moved away from wolf, and the tile is now empty
        assertEquals(null, world.getTile(location));
    }
}
