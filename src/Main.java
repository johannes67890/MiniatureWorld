import static org.junit.Assert.assertEquals;

import data.Distributer;
import itumulator.executable.Program;
import testReader.TestReader;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!");
        TestReader testReader = new TestReader(Distributer.t1_2cde.getUrl());
       
      

        testReader.getValues().forEach((k,v) -> {

            System.out.println(k + " " + v);
        });
    }
}


