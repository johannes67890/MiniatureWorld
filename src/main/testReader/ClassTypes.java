package main.testReader;

import main.Bear;
import main.Bush;
import main.Grass;
import main.Lair;
import main.Rabbit;
import main.Wolf;

public enum ClassTypes{
        grass(Grass.class),
        burrow(Lair.class),
        bush(Bush.class),
        rabbit(Rabbit.class),
        wolf(Wolf.class),
        bear(Bear.class);

        private Class<?> ReaderClass;
        ClassTypes(Class<?> key){
            this.ReaderClass = key;
        }
        public Class<?> getType(){
            return ReaderClass;
        }
}
    

