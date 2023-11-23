
import static org.junit.Assert.assertEquals;

import data.Distributer;
import itumulator.executable.Program;
import testReader.TestReader;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!");
        TestReader testReader = new TestReader(Distributer.t1_2cde.getUrl());
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;

import itumulator.world.Location;
import itumulator.world.World;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("T0.1\\t1-1a.txt"));
        int size = scanner.nextInt();
        int delay = 100;
        int display_size = 800;
        Program program = new Program(size, display_size, delay);
        World world = program.getWorld();
        Location place = new Location(0, 0);
        world.setTile(place, new Grass());
        program.show();
    }
}


