package test;

import java.io.IOException;

import org.junit.Before;

import itumulator.executable.Program;
import itumulator.world.World;
import main.Distributer;
import main.testReader.TestReader;

public class MainTest {
    Distributer distributior = Distributer.test;
    TestReader reader;
    int size = reader.getWorldSize();
    int delay = 100;
    int display_size = 800;
    Program program = new Program(size, display_size, delay);
    World world = program.getWorld();
    
    @Before
    public void setUp() throws IOException {
        reader = new TestReader(distributior.getUrl());
        size = reader.getWorldSize();
        delay = 100;
        display_size = 800;
        program = new Program(size, display_size, delay);
        world = program.getWorld();
    }
    
}
