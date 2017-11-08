package pl.kmetrak.dnutils;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class DNUtilsTest 
{
    @Test
    public void testGetField() {
        String dn="C=PL, O=First Test, OU=Many, many, many commas, cn=Something";
	    assertEquals("First Test", DNUtils.getField(dn,"O"));
	    assertEquals("Many, many, many commas", DNUtils.getField(dn,"OU"));
	    assertEquals("Something", DNUtils.getField(dn,"cn"));
	    assertEquals("Something", DNUtils.getField(dn,"CN"));
    }
    
    @Test
    public void testSplit() {
        String dn="C=PL, O=First Test, OU=Many, many, many commas, cn=Something";
	    assertArrayEquals(new String[]{"C=PL", "O=First Test", "OU=Many, many, many commas", "cn=Something"},DNUtils.splitDN(dn));
    }
    
    @Test
    public void testReverse() {
        String dn="C=PL, O=First Test, OU=Many, many, many commas, cn=Something";
	    assertEquals("cn=Something,OU=Many, many, many commas,O=First Test,C=PL",DNUtils.reverseDN(dn));
    }
    
    @Test
    public void testNormalize() {
        String dn="C=PL,     o=First Test,\tOu=Many, many, many commas, cn=Something";
	    assertEquals("C=PL,O=First Test,OU=Many, many, many commas,CN=Something",DNUtils.normalizeDN(dn));
    }
    
    @Test
    public void testTree() {
        String dn="C=PL, O=First Test, OU=Many, many, many commas, cn=Something";
	    assertArrayEquals(new String[]{"C=PL", "C=PL,O=First Test", "C=PL,O=First Test,OU=Many, many, many commas", "C=PL,O=First Test,OU=Many, many, many commas,cn=Something"},DNUtils.treeDN(dn));
    }
}
