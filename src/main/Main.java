package main;

import itumulator.executable.Program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
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
            
            for (Object obj : reader.getInstances()) {
                                
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
       

        // for (Class<?> key : reader.test().getClasses()) {
        //     // if (key == ReaderTypes.Wolf) {
        //     //     tempPack = new WolfPack();
        //     //     world.add(tempPack);
        //     // }
        //     for (int i = 0; i < reader.getRandomNumberFromType(key); i++) {
        //         spawnRandomObj(world, key);
        //     }

        program.show();
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
