package main.testReader;

import java.io.Reader;

import main.Bear;
import main.Bush;
import main.Grass;
import main.Lair;
import main.Rabbit;
import main.Wolf;

public enum ClassTypes{

        grass(Grass.class),
        lair(Lair.class),
        bush(Bush.class),
        rabbit(Rabbit.class),
        wolf(Wolf.class),
        bear(Bear.class);


        private Class<?> ReaderClass;
        ClassTypes(Class<?> key){
            this.ReaderClass = key;
        }
        public Class<?> getClassName(){
            return ReaderClass;
        }
        }
    

    
   

    // public static ReaderTypes.Values getEnumFromValues(String key) {
    //         for (Values v : Values.values()) {
    //             if(v.getvalue().equalsIgnoreCase(key)) return v;
    //         }
    //     throw new IllegalArgumentException("The type " + key + " does not exist on the list. See ReaderKey.java for the list of types.");
    // }
    

