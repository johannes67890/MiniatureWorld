
import static org.junit.Assert.assertEquals;
import itumulator.executable.Program;
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;

import itumulator.world.Location;
import itumulator.world.World;
import testReader.TestReader;

public class Main {

    public static void main(String[] args) throws IOException {
        Distributer distributior = Distributer.t1_1a;
        TestReader reader = new TestReader(distributior.getUrl());
        int size = reader.getWorldSize();
        int delay = 100;
        int display_size = 800;
        Program program = new Program(size, display_size, delay);
        World world = program.getWorld();
        Location place = new Location(size/2, size/2);
        world.setTile(place, new Grass());
        program.show();
    }
}


