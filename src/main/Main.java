package main;

import itumulator.executable.Program;

import java.util.*;
import java.util.stream.IntStream;
import java.io.IOException;
import java.lang.reflect.Constructor;

import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import java.util.HashMap;
import java.util.Random;
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
            
            // Create a hashmap with the constructor parameters.
            HashMap<Class<?>, Object> parameters = new HashMap<Class<?>, Object>();
            WolfPack wolfPack = new WolfPack();

            while (iterator.hasNext()) {
                Object obj = iterator.next();
                    // Set the current key and constructor.
                    if(obj instanceof Class<?>) { // 
                        key = (Class<?>) obj;

                        Constructor<?>[] constructors = key.getDeclaredConstructors();
                        for (Constructor<?> constr : constructors) {
                            constr.setAccessible(true);
                            Class<?>[] pTypes = constr.getParameterTypes();
                            constructor = key.getDeclaredConstructor(pTypes);
                        }
                        continue;
                    } 
                    // set the parameters for the class constructor.
                    if(key == Wolf.class){
                        world.add(wolfPack);
                        parameters.put(WolfPack.class, wolfPack);
                        key = WolfPack.class;
                    }
                    
                    if(obj instanceof IntStream) {
                        parameters.put(IntStream.class, obj);
                    }else parameters.put(obj.getClass(), obj);
                    
                }
                //
                // Spawn the object(s) in the world.
                //
                IntStream ClassStream = (IntStream) parameters.get(IntStream.class);
            
                int spawnAmount = getRandomNumberFromStream(ClassStream); // Get the amount of objects to spawn from parameters.
                 
                for (int i = 0; i < spawnAmount; i++) {
                    parameters.remove(IntStream.class); // Remove the IntStream from the parameters. We don't need it anymore.
                    
                    if(parameters.size() == 0 && key == Bear.class){ // If the bear has no set Location, spawn it at a random location. 
                      Location loc = null;
                        spawnRandomObj(world, constructor.newInstance(loc));
                        continue;
                    }else if(parameters.size() == 0) { // If the parameters are empty, spawn the object without constructor parameters.
                        spawnRandomObj(world, constructor.newInstance());
                        continue;
                    }
                    spawnRandomObj(world, constructor.newInstance(parameters.get(key)));
                }
            } 
        } catch (Exception e) {
          System.out.println("Exception encountered invoking: " + e);
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
    if (isWorldFull(world)) throw new IllegalArgumentException(
      "The world is full."
    );
    Location location = new Location(
      new Random().nextInt(world.getSize()),
      new Random().nextInt(world.getSize())
    );

    if (object instanceof NonBlocking && !world.containsNonBlocking(location)) {
      world.setTile(location, object);
    } else if (
      !(object instanceof NonBlocking) && world.isTileEmpty(location)
    ) {
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
    if (world.getSize() == 0) return true;
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
