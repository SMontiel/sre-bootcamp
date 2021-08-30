package com.wizeline;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class MethodsTest extends TestCase {
    public static final String jwtToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiYWRtaW4ifQ.BmcZ8aB5j8wLSK8CqdDwkGxZfFwM1X1gfAIN7cXOx9w";

    public MethodsTest( String testName ){
        super( testName );
    }

    public static Test suite(){
        return new TestSuite( MethodsTest.class );
    }

    public void testGenerateToken() {
      assertEquals(jwtToken, Methods.generateToken("admin"));
    }

    public void testAccessData() {
      assertEquals("You are under protected data", Methods.accessData("Bearer " + jwtToken));
    }
}
