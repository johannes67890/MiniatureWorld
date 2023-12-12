package main.testReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.IntStream;
import java.lang.Object;

import itumulator.world.Location;
import itumulator.world.World;
import main.Bear;

import java.lang.reflect.*;


/**
 * This class is used to read the file and return the content of the file.
 * 
 * @param filePath - The path to the file.
 * @param fileContent - The content of the file as a char array.
 * @param fileContentString - The content of the file as a string.
 * 
 * @throws IOException
 * @throws FileNotFoundException
 */
public class TestReader extends BufferedReader {
    private String filePath;
    private ArrayList<String[]> fileContentString;
    private int worldSize;
    private HashMap<Class<?>, Object> map = new HashMap<>();
    /**
     * Constructor for the TestReader class.
     * @param filePath - The path to the file.
     * @throws IOException
     * @throws FileNotFoundException
     */
    public TestReader(String filePath) throws IOException, FileNotFoundException {
        super(new FileReader(filePath));
        this.filePath = filePath;
        ArrayList<String> wordList = new ArrayList<>();
        String line = null;
        while ((line = this.readLine()) != null) {
            wordList.add(line);
        }
        this.fileContentString = wordList.stream().map(s -> s.split(" ")).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        this.worldSize = Integer.parseInt(this.fileContentString.get(0)[0]); // Assign the world size.
        fileContentString.remove(0); // Remove the first line of the file. (The world size)
        this.close();
    }

    /**
     * Method that returns the path of the file.
     * @return String
     */
    public String getFilePath() {
        return this.filePath;
    }

    /**
     * Method that returns the content of the file as a string.
     * @return String
     */
    public String getFileContentString(){
        return this.fileContentString.toString();
    }



    public ArrayList<Object> getInstances() throws Exception{
        ArrayList<Object> instances = new ArrayList<>();
        Class<?> c = null;
        for (String[] strings : fileContentString) {        
            if(getClass(strings[0]) instanceof Class<?>) {
                c = getClass(strings[0]);
                instances.add(c);            
            } else if(!(getClass(strings[0]) instanceof Class<?>)) throw new IllegalArgumentException("The type " + strings[0] + " does not exist.");
            
            for (String str : strings) {
                if(str.matches("\\((.*?)\\)")){ // If the string contains coordinates.
                    instances.add(setCoordinates(str));
                } 
                else if(str.contains("-") || isNumeric(str)){ // If the string contains an interval.
                    instances.add(getTypeRange(str));
                }
            } 
        }   
        return instances;
    }

    public <T> T getT(Class<T> clazz) {
        return clazz.cast(map.get(clazz));
    }

    public <T> void putT(Class<T> clazz, T favorite) {
        map.put(clazz, favorite);
    }

    public Class<?> getClass(ClassTypes ClassName){
        return  ClassName.getClassName();
    }

    public Class<?> getClass(String str){
        return getClass(ClassTypes.valueOf(str));
    }

    /**
     * Returns a stream of integers for a given type.
     * 
     * @throws IllegalArgumentException If the type does not exist.
     * @param type - The type of the location.
     * @return IntStream - The stream of integers.
     */
    public IntStream getTypeRange(String str){
        if(isNumeric(str)) {
            int numericVal = Integer.parseInt(str);
            return IntStream.rangeClosed(numericVal, numericVal);
        }
        else if(str instanceof String){
            String[] range = str.split("-");
            int start = Integer.parseInt(range[0]);
            int end = Integer.parseInt(range[1]);
            return IntStream.rangeClosed(start, end);
        } 
        else return null;
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
    public int getRandomNumberFromType(ReaderTypes.ClassTypes className){
        // get all object values of the key
        //IntStream stream = this.getMap().get(key).values().stream().filter(c -> c instanceof IntStream).mapToInt(c -> ((IntStream) c).findAny().getAsInt());    
        
       // return stream.findAny().getAsInt();
    }

    /**
     * Returns a random location for a given type. If the type does not have a location, null is returned.
     * 
     * @throws IllegalArgumentException If the type does not exist.
     * @param type - The type of the location.
     * @return Location - The random location.
     */
    public Location getLocation(ReaderTypes.ClassTypes className){
    }
    
    /**
     * Returns the size of the world.
     * @return int
     */
    public int getWorldSize(){
        return worldSize;
    }
    
    /**
     * Splits the string object and returns a location.
     * @param str - The string to split.
     * @return Location
     */
    private Location setCoordinates(String str){
        String[] coordinates = str.replaceAll("[()]", "").split(",");
        int x  = Integer.parseInt(coordinates[0]);
        int y = Integer.parseInt(coordinates[1]);
        return new Location(x, y);
    }

    /**
     * Method to check if a string is numeric.
     * 
     * @param strNum - String to check if it is numeric.
     * @return Boolean - True if the string is numeric, false otherwise.
     */
    private boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
