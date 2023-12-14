package main;

import itumulator.executable.Program;

import java.util.*;
import java.util.stream.IntStream;
import java.io.IOException;
import java.lang.reflect.Constructor;

import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import main.testReader.TestReader;

/**
 * Main class
 * 
 * The main class initializes the world and spawns the objects in the world.
 * 
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Distributer distributior = Distributer.t2_2a;
        TestReader reader = new TestReader(distributior.getUrl());
        int size = reader.getWorldSize();
        int delay = 100;
        int display_size = 800;
        Program program = new Program(size, display_size, delay);
        World world = program.getWorld();
        
        try {    
            for (Stack<Object> Stacks : reader.getInstances()) {
                Iterator<Object> iterator = Stacks.iterator();

                Class<?> key = null;
                Constructor<?> constructor = null;

                // HashMap that contains the parameters for the constructor of the runtime class.
                HashMap<Class<?>, Object> parameters = new HashMap<Class<?>, Object>();

                while (iterator.hasNext()) { // Iterate through the stack.
                    Object obj = iterator.next();
                        if(obj instanceof Class<?>) { // If the object is a class, set the key and constructor.
                            key = (Class<?>) obj;

                            Constructor<?>[] constructors = key.getDeclaredConstructors();
                            for (Constructor<?> constr : constructors) { // Find the constructor of the class.
                                constr.setAccessible(true);
                                Class<?>[] pTypes = constr.getParameterTypes();
                                constructor = key.getDeclaredConstructor(pTypes);
                            }
                            continue;
                        } 
                        if(obj instanceof IntStream) {
                            parameters.put(IntStream.class, obj);
                        } else parameters.put(obj.getClass(), obj);
                }
                IntStream ClassStream = (IntStream) parameters.get(IntStream.class);
                int spawnAmount = getRandomNumberFromStream(ClassStream);
                for (int i = 0; i < spawnAmount; i++) {
                    parameters.remove(IntStream.class);
                    if(parameters.size() == 0) {
                        spawnRandomObj(world, constructor.newInstance());
                        continue;
                    }
                spawnRandomObj(world, constructor.newInstance(parameters.get(key)));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        program.show();
    }

     /**
     * Returns a random number for a given type. 
     * 
     * The random number is in the range of the type. See {@link getTypeRange} for the range.
     * 
     * @throws IllegalArgumentException If the type does not exist.
     * @param type - 
     * @return int - The random number.
     */
    public static int getRandomNumberFromStream(IntStream stream){
        // Returns a random number from the stream.
        return stream.findAny().getAsInt();
    }


    /**
     * Spawns a random object in the world.
     * 
     * @param world  - The world to spawn the object in.
     * @param object - The object to spawn.
     */
    public static void spawnRandomObj(World world, Object object) {
        if (isWorldFull(world))
            throw new IllegalArgumentException("The world is full.");
        Location location = new Location(new Random().nextInt(world.getSize()), new Random().nextInt(world.getSize()));

        if (object instanceof NonBlocking && !world.containsNonBlocking(location)) {
            world.setTile(location, object);
        } else if (!(object instanceof NonBlocking) && world.isTileEmpty(location)) {
            world.setTile(location, object);
        } else {
            spawnRandomObj(world, object);
        }
    }

    /**
     * This method checks if the world is full.
     * 
     * @param world - The world to check.
     * @return boolean - True if the world is full, false otherwise.
     */
    public static boolean isWorldFull(World world) {
        if (world.getSize() == 0)
            return true;
        for (int i = 0; i < world.getSize(); i++) {
            for (int j = 0; j < world.getSize(); j++) {
                if (world.isTileEmpty(new Location(i, j))) {
                    return false;
                }
            }
        }
        return true;
    }

}
