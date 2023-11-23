import itumulator.executable.Program;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
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
        Location place = new Location(size/2, size/2);
        world.setTile(place, new Grass());
        program.show();
    }
}