import static org.junit.Assert.assertEquals;

import itumulator.executable.Program;
import testReader.TestReader;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!");
        TestReader testReader = new TestReader("data\\T0.1\\t1-1a.txt");
        System.out.println(testReader.readAllLines());
    }
}


