package testReader;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import itumulator.world.Location;
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
    private char[] fileContent;
    private String[] fileContentString;

    private Location location;
    private HashMap<String, ArrayList<Object>> map;


    /**
     * Constructor for the TestReader class.
     * @param filePath - The path to the file.
     * @throws IOException
     * @throws FileNotFoundException
     */
    public TestReader(String filePath) throws IOException, FileNotFoundException {
        super(new FileReader(filePath));
        this.filePath = filePath;
        char[] array = new char[100];
        this.read(array);
        this.fileContent = array;  
        this.fileContentString = new String(array).split("\\s+");
    }

    /**
     * Method that returns the path of the file.
     * @return String
     */
    public String getFilePath() {
        return this.filePath;
    }

    /**
     * Method that returns the content of the file as a char array.
     * @return char[]
     */
    public char[] getFileContent() {
        return this.fileContent;
    }

    /**
     * Method that returns the content of the file as a string.
     * @return String
     */
    public String getFileContentString(){
        return new String(this.fileContent);
    }

    /**
     * This method returns a HashMap with the types as keys and the values as values.
     * 
     * The hashmap does not contain the size of the world. (See {@link getWorldSize} method)
     * @return HashMap<String, ArrayList<Integer>
     */
    public HashMap<String, ArrayList<Object>> getMap(){
        HashMap<String, ArrayList<Object>> types = new HashMap<>();

        String key = null;
        Object value;
        ArrayList<Object> values = new ArrayList<>(); // This is the list of values for each type.

        for (String str : this.fileContentString) {
            if(str.matches("\\((.*?)\\)")){
                value = this.setCoordinates(str);
                this.location = (Location) value;
                values.add(value);
                types.put(key, values);
            }
            if(str.contains("-")){
                values.add(str);
                types.put(key, values);
            }
            if(isNumeric(str) && key != null){
                // add the value to the key.
                value = Integer.parseInt(str);
                values.add(value);
                types.put(key, values);
            } else if(isNumeric(str) && key == null){ // If the key is null, then we have not found a type yet.
                continue;
            } else if(!isNumeric(str) && key != null){ // If the key is not null, then we have found a new type.
                key = str;
                values = new ArrayList<>(); 
            }
            else {
                key = str;
            }
        }
        return types;
    }

    /**
     * This method returns a random interval or a static number for a given type.
     * 
     * @throws IllegalArgumentException If the type does not exist.
     * @param type - The type of the interval or static number.
     * @return int - The random interval number.
     */
    public Object getRandomIntervalNumber(String type){
        if(!this.getMap().containsKey(type)) throw new IllegalArgumentException("The type " + type + " does not exist.");
        if(this.getMap().get(type).size() == 1){
            return this.getMap().get(type).get(0);
        } else if(this.getMap().get(type).contains("-")) {
            String[] interval = this.getMap().get(type).toString().split("-");
            int min = Integer.parseInt(interval[0]);
            int max = Integer.parseInt(interval[1]);
            return (Object) getRandomNumber(min, max  + 1);
        } else throw new IllegalArgumentException("The type " + type + " does not have a valid interval.");
    
    }

    public Location setCoordinates(String str){
        String[] coordinates = str.replaceAll("[()]", "").split(",");
        int x  = Integer.parseInt(coordinates[0]);
        int y = Integer.parseInt(coordinates[1]);
        return new Location(x, y);
    }

    public Location getLocation(){
        return this.location;
    }

    /**
     * This method returns the size of the world.
     * @return int
     */
    public int getWorldSize(){
        return Integer.parseInt(this.fileContentString[0]);
    }

    /**
     * Method to check if a string is numeric.
     * 
     * @param strNum - String to check if it is numeric.
     * @return Boolean - True if the string is numeric, false otherwise.
     */
    boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
