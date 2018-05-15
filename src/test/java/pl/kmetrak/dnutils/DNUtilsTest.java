package pl.kmetrak.dnutils;

import static org.junit.Assert.*;

import org.junit.Test;

public class DNUtilsTest 
{
    @Test
    public void testFake() {
        assertNotNull(new DNUtils());
    } 
    @Test
    public void testGetField() {
        String dn="C=PL, O=First Test, OU=Many, many, many commas, cn=Something";
	    assertEquals("First Test", DNUtils.getField(dn,"O"));
	    assertEquals("Many, many, many commas", DNUtils.getField(dn,"OU"));
	    assertEquals("Something", DNUtils.getField(dn,"cn"));
	    assertEquals("Something", DNUtils.getField(dn,"CN"));
	    assertNull(DNUtils.getField(dn, "null"));
    }
    
    @Test
    public void testGetFieldNotNull() {
        String dn="C=PL, O=First Test, OU=Many, many, many commas, cn=Something";
        assertEquals("First Test", DNUtils.getFieldNotNull(dn,"O"));
        assertEquals("Many, many, many commas", DNUtils.getFieldNotNull(dn,"OU"));
        assertEquals("Something", DNUtils.getFieldNotNull(dn,"cn"));
        assertEquals("Something", DNUtils.getFieldNotNull(dn,"CN"));
        assertNotNull(DNUtils.getFieldNotNull(dn, "null"));
    }
    
    @Test
    public void testGetFields() {
        String dn="C=PL, O=First Test, OU=Many, many, many commas, OU=Another OU, cn=Something";
        assertArrayEquals(new String[]{"First Test"}, DNUtils.getFields(dn,"O"));
        assertArrayEquals(new String[]{"Many, many, many commas", "Another OU"}, DNUtils.getFields(dn,"OU"));
        assertArrayEquals(new String[]{"Something"}, DNUtils.getFields(dn,"cn"));
        assertArrayEquals(new String[]{"Something"}, DNUtils.getFields(dn,"CN"));
        assertArrayEquals(new String[0], DNUtils.getFields(dn, "null"));
    }
    
    public void testGetFieldsNotNull() {
        String dn="C=PL, O=First Test, OU=Many, many, many commas, OU=Another OU, cn=Something";
        assertArrayEquals(new String[]{"First Test"}, DNUtils.getFieldsNotNull(dn,"O"));
        assertArrayEquals(new String[]{"Many, many, many commas", "Another OU"}, DNUtils.getFieldsNotNull(dn,"OU"));
        assertArrayEquals(new String[]{"Something"}, DNUtils.getFieldsNotNull(dn,"cn"));
        assertArrayEquals(new String[]{"Something"}, DNUtils.getFieldsNotNull(dn,"CN"));
        assertArrayEquals(new String[]{""}, DNUtils.getFieldsNotNull(dn, "null"));
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
    
    @Test
    public void testFlatten() {
        String dn="C=PL,     o=First Test,\tOu=Many, many, many commas, cn=Something";
	    assertEquals("PL, First Test, Many, many, many commas, Something",DNUtils.flatten(dn));
    }
    
    @Test
    public void testEscapeDn(){
        String dn="C=PL,o=First Test,Ou=Many, many, many commas,cn=Something";
        assertEquals("C=PL,o=First Test,Ou=Many\\, many\\, many commas,cn=Something",DNUtils.escapeDN(dn));
    }
}
