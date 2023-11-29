package test;
import java.io.IOException;
import org.junit.*;

import itumulator.world.Location;
import testReader.TestReader;

public class TestReaderTest {
    public TestReader testReader;
    public int n = 7;
    
    @Before
    public void setUp() throws IOException {
        testReader = new TestReader("src\\test\\test.txt");
    }

    @Test
    public void testGetFilePath() {
        Assert.assertEquals("src\\test\\test.txt", testReader.getFilePath());
    }
    
    @Test
    public void testGetFileContent() {
        Assert.assertEquals(7 , testReader.getWorldSize());

    }
    @Test
    public void testGetValues() {
      Assert.assertEquals(3, testReader.getTypeRange("TestType1").min().getAsInt());
      Assert.assertEquals(5, testReader.getTypeRange("TestType1").max().getAsInt());
    }
    
    @Test 
    public void testGetCoordinats(){
        Assert.assertEquals(new Location(3,5), testReader.getLocation("TestType1"));
    }
}

