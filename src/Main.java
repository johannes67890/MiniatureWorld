
import itumulator.executable.Program;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.io.IOException;
import itumulator.world.Location;
import itumulator.world.World;
import testReader.TestReader;

public class Main {

    public static void main(String[] args) throws IOException {
        Distributer distributior = Distributer.t1_1d;
        TestReader reader = new TestReader(distributior.getUrl());
        int size = reader.getWorldSize();
        int delay = 100;
        int display_size = 800;
        Program program = new Program(size, display_size, delay);
        World world = program.getWorld();

        HashMap<String, ArrayList<Integer>> map = reader.getMap();
        for(String key : map.keySet()) {
            if(key.equals("grass")){
                int amount = reader.getRandomIntervalNumber(key);
                for(int i = 0; i < amount; i++){
                    Location place = new Location(new Random().nextInt(size), new Random().nextInt(size));
                    while(world.getTile(place) instanceof Grass){
                        place = new Location(new Random().nextInt(size), new Random().nextInt(size));
                    }
                    world.setTile(place, new Grass());
                }
            }
        }

        Rabbit rab = new Rabbit(new Location(2, 0), world);
        rab.digHole();

        program.show();


    }
}


