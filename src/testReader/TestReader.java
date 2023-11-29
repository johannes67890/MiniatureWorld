package testReader;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;
import java.lang.Object;
import org.w3c.dom.ranges.Range;

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
    private ArrayList<String[]> fileContentString;

    private HashMap<String, ArrayList<Object>> map;
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
        
        this.map = this.getMap();
        this.worldSize = this.getWorldSize();

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

        for (String[] object : this.fileContentString) {
            for (String str : object) {
                if(str.matches("\\((.*?)\\)")){
                    if(values.stream().anyMatch(c -> c instanceof Location)) throw new IllegalArgumentException("Input contains more than one location. A location has already been set.");
                    System.out.println(str);
                    values.add(setCoordinates(str));
                    types.put(key, values);
                } else if(str.contains("-")){
                    values.add(str);
                    types.put(key, values);
                } else if(isNumeric(str) && key != null){
                    value = Integer.parseInt(str);
                    values.add(str);
                    types.put(key, values);
                } else if(isNumeric(str) && key == null){ // If the key is null, then we have not found a type yet.
                    continue;
                } else if(!isNumeric(str) && key != null){ // Reset the key and values. Ready for the next type.
                    key = str;
                    values = new ArrayList<>(); 
                }
                else {
                    key = str;
                }
            }
        }
        return types;
    }

    /**
     * This method returns a stream of integers for a given type.
     * 
     * @throws IllegalArgumentException If the type does not exist.
     * @param type - The type of the location.
     * @return IntStream - The stream of integers.
     */
    public IntStream getTypeRange(String type){
        if(!this.getMap().containsKey(type)) throw new IllegalArgumentException("The type " + type + " does not exist.");
        Object value = this.getMap().get(type).get(0);

        if(this.getMap().size() == 1 && value instanceof Integer){
            return IntStream.range((int) value, (int) value);
        } else if(value instanceof String) {
            String[] interval = ((String) value).split("-");
            int min = Integer.parseInt(interval[0]);
            int max = Integer.parseInt(interval[1]);
            return IntStream.range(min, max + 1);
        } else throw new IllegalArgumentException("The type " + type + " does not have a valid interval.");
    } 
    
    /**
     * Returns a random number for a given type. 
     * 
     * The random number is in the range of the type. See {@link getTypeRange} for the range.
     * 
     * @throws IllegalArgumentException If the type does not exist.
     * @param type - The type of the location.
     * @return int - The random number.
     */
    public int getRandomNumber(String type){
        IntStream stream = this.getTypeRange(type);
        return stream.findAny().getAsInt();
    }

    /**
     * This method returns a random location for a given type.
     * 
     * @throws IllegalArgumentException If the type does not exist.
     * @param type - The type of the location.
     * @return Location - The random location.
     */
    public Location getLocation(String type){
        return (Location) this.getMap().get(type).stream().filter(c -> c instanceof Location).findFirst().orElse(null);
    }
    
    /**
     * This method returns the size of the world.
     * @return int
     */
    public int getWorldSize(){
        return Integer.parseInt(this.fileContentString.get(0)[0]);
    }
    
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
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
