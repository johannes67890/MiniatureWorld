package test;
import java.io.IOException;

import org.junit.*;

import itumulator.world.Location;
import main.testReader.TestReader;

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

}

