package test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import main.*;

public class SnakeTest {
    Program program;
    World world;

    @Before
    public void setUp() {
        program = new Program(2, 800, 100);
        world = program.getWorld();
    }

    // Test if a snake can die
    @Test
    public void test1() {
        Snake snake = new Snake();
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, snake);
        assertEquals("main.Snake", world.getTile(location).getClass().getName());
        snake.die(world);
        assertEquals("main.Carcass", world.getTile(location).getClass().getName());
    }

    // Test if a snake can eat carcasses
    @Test
    public void test2() {
        Snake snake = new Snake();
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, snake);
        location = new Location(0, 1);
        Carcass carcass = new Carcass(new Rabbit(), false);
        world.setTile(location, carcass);
        assertEquals(15, snake.getHunger());
        snake.food(world);
        carcass.act(world);
        assertEquals(18, snake.getHunger());
        location = new Location(1, 1);
        Carcass carcass2 = new Carcass(new Bear(location), false);
        world.setTile(location, carcass2);
        snake.food(world);
        assertEquals(26, snake.getHunger());
    }

    // Test if a snake can reproduce
    @Test
    public void test3() {
        // Make a bush to control where snake egg spawns
        Bush bush = new Bush();
        Location location = new Location(1, 0);
        world.setCurrentLocation(location);
        world.setTile(location, bush);
        Snake snake = new Snake();
        location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, snake);
        Snake snake2 = new Snake();
        location = new Location(0, 1);
        world.setTile(location, snake2);
        // Check that the number of entities in the world is 3: 2 snakes and 1 bush
        assertEquals(3, world.getEntities().size());
        // Set both snakes to be adults so they can reproduce
        snake.isAdult = true;
        snake2.isAdult = true;
        snake.reproduce(world);
        // Check that the number of entities in the world is 4: 2 snakes, 1 bush and 1 snake egg
        assertEquals(4, world.getEntities().size());
        location = new Location(1, 1);
        // Check that the snake egg is on expected tile
        assertEquals("SnakeEgg", world.getTile(location).getClass().getSimpleName());
        SnakeEgg snakeegg = (SnakeEgg) world.getTile(location);
        // Run the snake eggs act method 15 times to hatch the snake egg
        for(int i = 0; i < 15; i++){
            snakeegg.act(world);
        }
        // Check that the snake egg has hatched
        assertEquals("Snake", world.getTile(location).getClass().getSimpleName());
    }

    // Test if a snake can poison animals
    @Test
    public void test4() {
        Snake snake = new Snake();
        Location location = new Location(0, 0);
        world.setCurrentLocation(location);
        world.setTile(location, snake);
        location = new Location(0, 1);
        Rabbit rabbit = new Rabbit();
        world.setTile(location, rabbit);
        // Set the rabbit's hunger to 5 so it can not regenerate health
        rabbit.hungerPlus(-10);
        // The rabbit starts with 10 hp
        assertEquals(10, rabbit.getHp());
        // After the snake attacks, it should have 9 hp and 4 poison
        snake.attack(location, world);
        assertEquals(9, rabbit.getHp());
        assertEquals(4, rabbit.getPoison());
        // After one step, it should have 8 hp and 3 poison
        rabbit.life(world);
        assertEquals(8, rabbit.getHp());
        assertEquals(3, rabbit.getPoison());
        // After two steps, it should have 7 hp and 2 poison
        rabbit.life(world);
        assertEquals(7, rabbit.getHp());
        assertEquals(2, rabbit.getPoison());
        // After three steps, it should have 6 hp and 1 poison
        rabbit.life(world);
        assertEquals(6, rabbit.getHp());
        assertEquals(1, rabbit.getPoison());
        // After four steps, it should have 5 hp and 0 poison
        rabbit.life(world);
        assertEquals(5, rabbit.getHp());
        assertEquals(0, rabbit.getPoison());
    }
}
