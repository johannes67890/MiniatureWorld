package test;
import org.junit.Test;
import itumulator.executable.Program;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.io.IOException;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import main.testReader.TestReader;
import main.Distributer;

public class InitTest {
    
    public InitTest() throws IOException {
        Distributer distributior = Distributer.test;
        TestReader reader = new TestReader(distributior.getUrl());

    }

    @Test
    public void init() {
        System.out.println("Hello World!");
    }
}
