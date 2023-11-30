
import itumulator.executable.Program;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.io.IOException;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import testReader.TestReader;


public class Main {
    public static void main(String[] args) throws IOException {
        Distributer distributior = Distributer.t2_7a;
        TestReader reader = new TestReader(distributior.getUrl());
        int size = reader.getWorldSize();
        int delay = 100;
        int display_size = 800;
        Program program = new Program(size, display_size, delay);
        World world = program.getWorld();

        HashMap<String, ArrayList<Object>> map = reader.getMap();
        
        for(String key : map.keySet()) {
            for (int i = 0; i < reader.getRandomNumberFromType(key); i++) {
                Object object = null;
                switch (key) {
                    case "grass":
                        object = new Grass();
                        break;
                    case "rabbit":
                        object = new Rabbit();
                        break;
                    case "burrow":
                        object = new Burrow();
                        break;
                    case "wolf":
                        object = new Wolf();
                        break;
                    case "bear":
                        object = new Bear();
                        break;
                    case "berry":
                        object = new Bush();
                        break;
                    default:
                        throw new RuntimeException("Not on list");
                }
                spawnRandomObj(world, object);
            }
        }

        program.show();
    }
    /**
     * Spawns a random object in the world.
     * 
     * @param world - The world to spawn the object in.
     * @param object - The object to spawn.
     */
    public static void spawnRandomObj(World world, Object object){
        if(isWorldFull(world)) throw new IllegalArgumentException("The world is full.");
            Location location = new Location(new Random().nextInt(world.getSize()), new Random().nextInt(world.getSize()));
            
            if(object instanceof NonBlocking && !world.containsNonBlocking(location)){
                world.setTile(location, object); 
            } else if(!(object instanceof NonBlocking) && world.isTileEmpty(location)){
                world.setTile(location, object); 
            } else {
                spawnRandomObj(world, object);
            }
    }
    /**
     * This method checks if the world is full.
     * @param world - The world to check.
     * @return boolean - True if the world is full, false otherwise.
     */
    public static boolean isWorldFull(World world) {
        if(world.getSize() == 0) return true;
        for (int i = 0; i < world.getSize(); i++) {
            for (int j = 0; j < world.getSize(); j++) {
                if(world.isTileEmpty(new Location(i, j))) {
                    return false;
                }
            }
        }
        return true;
    }

}


