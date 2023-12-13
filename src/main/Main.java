package main;

import itumulator.executable.Program;

import java.util.*;
import java.util.stream.IntStream;
import java.io.IOException;
import java.lang.reflect.Constructor;

import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import main.testReader.ClassTypes;
import main.testReader.TestReader;

/**
 * Main class
 * 
 * The main class initializes the world and spawns the objects in the world.
 * 
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Distributer distributior = Distributer.test;
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
            HashMap<Class<?>, Object> parameters = new HashMap<Class<?>, Object>();
            while (iterator.hasNext()) {
                Object obj = iterator.next();
                    if(obj instanceof Class<?>) {
                        key = (Class<?>) obj;

                        Constructor<?>[] constructors = key.getDeclaredConstructors();
                        for (Constructor<?> constr : constructors) {
                            constr.setAccessible(true);
                            Class<?>[] pTypes = constr.getParameterTypes();
                            constructor = key.getDeclaredConstructor(pTypes);
                        }
                        System.out.println(constructor);
                        continue;
                    } 

                    parameters.put(obj.getClass(), obj);
         

                    // if(obj instanceof IntStream) parameters.put(IntStream.class, (IntStream) obj);
                    // if(obj instanceof Location) parameters.put(Location.class, (Location) obj);
                  
                    // spawnRandomObj(world, constructor.newInstance());

                            // constructor.setAccessible(true);
                            // Class<?>[] parameterTypes = constructor.getParameterTypes();
                            // if (parameterTypes.length > 0) {
                            //     if(key == ClassTypes.bear.getType()){
                            //         Object instance = constructor.newInstance(Location.class.cast(new Location(2, 3)), world);
                            //     spawnRandomObj(world, instance);

                            //     }
                            // } else spawnRandomObj(world, constructor.newInstance());
                }
                Class<?> intStreamClass = IntStream.class;
                int t = getRandomNumberFromStream((IntStream) parameters.get(intStreamClass));
                System.out.println(t);
                // for (int i = 0; i < getRandomNumberFromStream((IntStream) parameters.get(IntStream.class)); i++) {
                //     System.out.println(i);
                // }
                // spawnRandomObj(world, constructor.newInstance(parameters.get(key)));
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
