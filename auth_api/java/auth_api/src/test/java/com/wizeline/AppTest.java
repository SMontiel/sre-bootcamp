package com.wizeline;

import com.m3.curly.HTTP;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import spark.Spark;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AppTest extends TestCase {
  private static final String baseURL = "http://localhost:8000/";

  public static junit.framework.Test suite() {
    App.main(null);
    Spark.awaitInitialization();

    return new TestSuite( AppTest.class );
  }

  public void testLoginRespondsSuccessfully() throws IOException {
    Map<String, String> body = new HashMap<>();
    body.put("username", "admin");
    body.put("password", "secret");
    com.m3.curly.Response res = HTTP.post(baseURL + "login", body);
    assertEquals(200, res.getStatus());
    String expected = "{" +
        "\"data\":\"" + MethodsTest.jwtToken + "\"" +
        "}";
    assertEquals(expected, new String(res.getBody()));
  }

  public void testEmptyCredentials() throws IOException {
    com.m3.curly.Response res = HTTP.post(baseURL + "login", new HashMap<>());
    assertEquals(403, res.getStatus());
  }

  public void testIndexRespondsSuccessfully() throws IOException {
    com.m3.curly.Response res = HTTP.get(baseURL);

    assertEquals(200, res.getStatus());
    assertEquals("OK", new String(res.getBody()));
  }
}
