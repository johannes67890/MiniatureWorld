package main;

import itumulator.executable.Program;

import java.util.*;
import java.util.stream.IntStream;
import java.io.IOException;
import java.lang.reflect.Constructor;

import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import java.util.Random;

import main.testReader.Distributer;
import main.testReader.TestReader;

/**
 * Main execute point from the program.
 * Uses {@link TestReader} to read the test file and {@link Distributer} to get the url of the test file.
 * Then spawns objects in the world, using the {@link spawnRandomObj} method after gathering the objects from the test file.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Distributer distributior = Distributer.test;
        TestReader reader = new TestReader(distributior.getUrl());
        int size = reader.getWorldSize();
        int delay = 250;
        int display_size = 1000;
        Program program = new Program(size, display_size, delay);
        World world = program.getWorld();
        
      try {    
        for (Stack<Object> Stacks : reader.getInstances()) {
            Iterator<Object> iterator = Stacks.iterator();
            Class<?> key = null;
            Constructor<?> constructor = null;
            
            // Create a hashmap with the constructor parameters.
            Stack<Object> parameters = new Stack<Object>();
            IntStream ClassStream = null;
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
                    // Add the constructor parameters to the stack.
                    if(obj instanceof IntStream) {
                      ClassStream = (IntStream) obj;
                    }else parameters.add(obj);                    
                }

                // If there is no location in file, add a random location.
                if(key == Bear.class){
                    if(!parameters.stream().anyMatch(o -> o instanceof Location)) {
                    parameters.add(new Location(
                      new Random().nextInt(world.getSize()),
                      new Random().nextInt(world.getSize())
                    ));
                  }
                }
                // Add wolf to the wolfpack.
                if(key == Wolf.class){
                    world.add(wolfPack);
                    parameters.add(wolfPack);
                    key = WolfPack.class;
                }
                
                if(key == Carcass.class){
                    // Set default carcass to wolf, if no carcass is given.
                      if(!parameters.stream().anyMatch(o -> o instanceof Wolf)) {
                        parameters.insertElementAt(new Wolf(new WolfPack()), 0); 
                      }
                      // Set default carcass' fungus to false, if no fungi is given.
                      if(!parameters.stream().anyMatch(o -> o instanceof Boolean)) {
                        parameters.add(false); 
                      }
                }

                //
                // Spawn the object(s) in the world.
                //            
                int spawnAmount = getRandomNumberFromStream(ClassStream); // Get the amount of objects to spawn from parameters.
                 
                for (int i = 0; i < spawnAmount; i++) {                    
                    if(parameters.size() == 0) { // If the parameters are empty, spawn the object without constructor parameters.
                        spawnRandomObj(world, constructor.newInstance());
                        continue;
                    }
                    spawnRandomObj(world, constructor.newInstance(parameters.toArray()));
                }
                
            } 
        } catch (Exception e) {
          System.out.println("Exception encountered invoking: " + e);
        }
        program.show();
      }

  /**
  * Returns a random number from a stream.
  * 
  * The random number is in the range of the type. See {@link getTypeRange} for the range.
  * @param stream - The stream to get the random number from.
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
