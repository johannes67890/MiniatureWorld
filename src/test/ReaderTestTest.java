package test;
import java.io.IOException;
import java.util.stream.IntStream;

import org.junit.*;

import itumulator.world.Location;
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
        Assert.assertEquals(5, testReader.getInstances().size());
        testReader.getInstances().forEach(instance -> {
            Assert.assertEquals(Class.class,instance.get(0).getClass());
            // instance.forEach(stackObj -> {
            //     Assert.assertEquals(Location.class, stackObj.getClass());               
            // });      
        });
    }

    @Test
    public void testGetTypeRange() {
        Assert.assertEquals(IntStream.rangeClosed(3, 5), testReader.getTypeRange("3-5"));
        Assert.assertEquals(IntStream.rangeClosed(3, 3), testReader.getTypeRange("3-3"));
        Assert.assertEquals(IntStream.rangeClosed(2, 2), testReader.getTypeRange("2"));
    }

    @Test
    public void testSetCoordinates() {
        Assert.assertEquals(new Location(2, 3), testReader.setCoordinates("(2,3)"));
    }

}

