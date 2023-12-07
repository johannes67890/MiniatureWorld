package main.testReader;



public enum ReaderKey {

    // Objects
    Grass("grass"),
    Lair("burrow"),
    Bush("bush"),
    Carcass("carcass"),
    // Animals
    Rabbit("rabbit"),
    Wolf("wolf"),
    Bear("bear");

    private  String key;
 
    ReaderKey (String key) {
        this.key = key;
    }
    
    public String getKey() {
        return key;
    }


    public static ReaderKey getEnum(String key) {
        for(ReaderKey v : values()){
            if(v.getKey().equalsIgnoreCase(key)) return v;
        }
        throw new IllegalArgumentException("The type " + key + " does not exist on the list. See ReaderKey.java for the list of types.");
    }

}
