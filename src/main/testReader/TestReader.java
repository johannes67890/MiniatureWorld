package main.testReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Stack;
import java.util.stream.IntStream;
import java.lang.Object;

import itumulator.world.Location;
import main.Carcass;

/**
 * TestReader is used to read a test file and return the content.
 * 
 * @param filePath - The path to the file.
 * @param fileContent - The content of the file as a char array.
 * @param fileContentString - The content of the file as a string.W
 * 
 * @throws IOException
 * @throws FileNotFoundException
 */
public class TestReader extends BufferedReader {
    private String filePath;
    private ArrayList<String[]> fileContentString;
    private int worldSize;

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

    /**
     * getInstances reads each line of the files content and returns a Arraylist of each line with the stack of each objects within the line.
     * 
     * The stacks first object is the class representing the world object. (Grass, Bush, Rabbit etc.) see {@link ClassTypes}. 
     * @return ArrayList<Stack<Object>> - The list of lines, with the stacks of objects within the line.
     * @throws Exception - If the type does not exist.
     */
    public ArrayList<Stack<Object>> getInstances() throws Exception{
        ArrayList<Stack<Object>> instances = new ArrayList<Stack<Object>>(); // The intances of the world objects.
        Stack<Object> objects = new Stack<Object>(); // The object to the current instance.
        Class<?> c = null; // The class of the current instance, on runtime.


        for (String[] strings : fileContentString) {     
            objects = new Stack<Object>();
   
            // If the first line of the file-line is a class.
            if(getClass(strings[0]) instanceof Class<?>) {
                c = getClass(strings[0]);
                objects.push(c);        
            } else if(!(getClass(strings[0]) instanceof Class<?>)) throw new IllegalArgumentException("The type " + strings[0] + " does not exist.");
            
            for (String str : strings) {
                if(str.matches("\\((.*?)\\)")){ // If the string contains coordinates.
                    objects.push(setCoordinates(str));
                } 
                else if(str.contains("-") || isNumeric(str)){ // If the string contains an interval.
                    objects.push(getTypeRange(str));
                } 
                if(str.equals("fungi") && c == Carcass.class){
                    objects.push(true);
                } 
            } 
            instances.add(objects);
        }   
        return instances;
    }

    /**
     * Returns the class of a given {@link ClassTypes}.
     * @param ClassName - The class to return the type.
     * @return Class<?>
     */
    public Class<?> getClass(ClassTypes ClassName){
        return  ClassName.getType();
    }

    /**
     * Returns the class of a given string.
     * @param str - The string to return the type.
     * @return Class<?>
     */
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
