package test;
import org.junit.*;
import itumulator.executable.Program;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import itumulator.world.World;
import main.testReader.Distributer;
import main.testReader.TestReader;
import main.Main;

public class InitTest {
    static World world;
    static Program program;
    static int size,delay,display_size;
    static Distributer distributior;
    static TestReader reader;

    /**
     * Initialize the world and the program
     * 
     * @throws IOException - if the file is not found
     */
    @Before
    public void init() throws IOException {
        distributior = Distributer.test;
        reader = new TestReader(distributior.getUrl());
        size = reader.getWorldSize();
        delay = 100;
        display_size = 800;
        program = new Program(size, display_size, delay);
        world = program.getWorld();
    }


    @Test
    public void testSpawnRandomObj() {       
    }

    @Test
    public void testIsWorldFull(){
        assertEquals(false, Main.isWorldFull(world));

        // // Fill the world
        // for (int i = 0; i < world.getSize() + 1; i++) {
        //     world.add(new Object());
        // }

        // assertEquals(true, Main.isWorldFull(world));
    }
}
