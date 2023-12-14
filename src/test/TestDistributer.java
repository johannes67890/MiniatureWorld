package test;

import static org.junit.Assert.assertEquals;

import java.util.EnumSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import main.Distributer;

public class TestDistributer {
    Set<Distributer> allEnums;
    @Before
    public void setUp() throws Exception{
        allEnums = EnumSet.allOf(Distributer.class);
    }
    
    /**
     * Test if the enum set Distributer contains and match all the test files. 
     */
    @Test
    public void testGetUrl() {
        for (Distributer d : allEnums) {
            assertEquals(d.toString().concat(".txt"), d.getUrl().split("\\\\")[2]);
        }
    }
}
