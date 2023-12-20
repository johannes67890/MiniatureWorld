package test;
import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.IntStream;

import org.junit.*;

import itumulator.world.Location;
import main.Bear;
import main.Carcass;
import main.Grass;
import main.Snake;
import main.Wolf;
import main.testReader.TestReader;

/**
 * Test the TestReader class.
 * Excpeted outputs is projected from the test.txt file.
 */
public class ReaderTestTest {
    public TestReader testReader;
    
    @Before
    public void setUp() throws IOException {
        testReader = new TestReader("src\\test\\test.txt");
    }

    @Test
    public void testGetFilePath() {
        Assert.assertEquals("src\\test\\test.txt", testReader.getFilePath());
    }
    
    @Test
    public void testFileContent() {
        Assert.assertEquals(10 , testReader.getWorldSize());
    }

    @Test
    public void testInstances() throws Exception {
        ArrayList<Object> types = new ArrayList<Object>(asList( 
            Class.class,Grass.class, Wolf.class, Carcass.class, 
            Snake.class,Location.class, Bear.class));

        Assert.assertEquals(5, testReader.getInstances().size());
        testReader.getInstances().forEach(instance -> {
            Assert.assertEquals(Class.class,instance.get(0).getClass());
            instance.forEach(stackObj -> {
                if(stackObj instanceof IntStream) {
                    Assert.assertEquals(1, ((IntStream)stackObj).max().getAsInt());
                }
                if(stackObj instanceof Location) {
                    Assert.assertEquals(new Location(2, 3), stackObj);
                }
                if(stackObj instanceof Class) {
                    Assert.assertTrue(types.contains(stackObj));
                }
            }); 
        });
    }

    @Test
    public void testGetTypeRange() {
        // Closed range of two different min, max numbers
        Assert.assertEquals(5, testReader.getTypeRange("3-5").max().getAsInt());
        Assert.assertEquals(3, testReader.getTypeRange("3-5").min().getAsInt());
        // Closed range of two same min, max numbers
        Assert.assertEquals(3, testReader.getTypeRange("3-3").max().getAsInt());
        Assert.assertEquals(3, testReader.getTypeRange("3-3").min().getAsInt());
        // Single number
        Assert.assertEquals(2, testReader.getTypeRange("2").max().getAsInt());
        Assert.assertEquals(2, testReader.getTypeRange("2").min().getAsInt());    
    }

    @Test
    public void testSetCoordinates() {
        Assert.assertEquals(new Location(2, 3), testReader.setCoordinates("(2,3)"));
    }

}

