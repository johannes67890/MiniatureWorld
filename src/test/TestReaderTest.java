package test;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

import org.junit.*;

import itumulator.world.Location;
import testReader.TestReader;

public class TestReaderTest {
    public TestReader testReader;
    public int n = 7;
    public ArrayList<String> types = new ArrayList<String>(List.of("TestType0", "TestType1"));
    public ArrayList<Integer> type0Val = new ArrayList<Integer>(List.of(2));
    public ArrayList<Integer> type1Val = new ArrayList<Integer>(List.of(3));
    public String str = new String("(2,3)");
    
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
       Assert.assertEquals(type0Val, testReader.getMap().get("TestType0"));
       Assert.assertEquals(type1Val, testReader.getMap().get("TestType1"));
    }
    
    // @Test 
    // public void testGetCoordinats(){
    //     Assert.assertEquals(new Location(2,3), testReader.getLocation(str));
    // }
}

