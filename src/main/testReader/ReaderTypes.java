package main.testReader;

import itumulator.world.Location;

public class ReaderTypes{
    public enum Keys{
        Grass("grass"),
        Lair("burrow"),
        Bush("bush"),
        Carcass("carcass"),
        Rabbit("rabbit"),
        Wolf("wolf"),
        Bear("bear");

        private String key;
        Keys(String key){
            this.key = key;
        }
        public String getKey(){
            return key;
        }

        public static ReaderTypes.Keys getEnumFromKeys(String key) {
        for(Keys k : Keys.values()){
                if(k.getKey().equalsIgnoreCase(key)) return k;
        }
        throw new IllegalArgumentException("The type " + key + " does not exist on the list. See ReaderKey.java for the list of types.");
    }
    }

    
   

    // public static ReaderTypes.Values getEnumFromValues(String key) {
    //         for (Values v : Values.values()) {
    //             if(v.getvalue().equalsIgnoreCase(key)) return v;
    //         }
    //     throw new IllegalArgumentException("The type " + key + " does not exist on the list. See ReaderKey.java for the list of types.");
    // }
    
}

