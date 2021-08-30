package com.wizeline;

import com.m3.curly.HTTP;
import com.m3.curly.Request;
import com.wizeline.data.Database;
import com.wizeline.data.User;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import spark.Spark;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AppTest extends TestCase {
  private static final String baseURL = "http://localhost:8000/";

  public static junit.framework.Test suite() {
    Database testDb = username -> new User("admin",
        "15e24a16abfc4eef5faeb806e903f78b188c30e4984a03be4c243312f198d1229ae8759e98993464cf713e3683e891fb3f04fbda9cc40f20a07a58ff4bb00788",
        "F^S%QljSfV", "admin");
    new App(testDb);
    Spark.awaitInitialization();

    return new TestSuite( AppTest.class );
  }

  public void testIndexRespondsSuccessfully() throws IOException {
    com.m3.curly.Response res = HTTP.get(baseURL);

    assertEquals(200, res.getStatus());
    assertEquals("OK", new String(res.getBody()));
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

  public void testProtectedValidAuhorizationHeader() throws IOException {
    Request req = new Request(baseURL + "protected");
    req.setHeader("Authorization", "Bearer " + MethodsTest.jwtToken);
    com.m3.curly.Response res = HTTP.get(req);
    assertEquals(200, res.getStatus());
    String expected = "{" +
        "\"data\":\"You are under protected data\"" +
        "}";
    assertEquals(expected, new String(res.getBody()));
  }

  public void testProtectedEmptyAuhorizationHeader() throws IOException {
    com.m3.curly.Response res = HTTP.get(baseURL + "protected");

    assertEquals(403, res.getStatus());
    assertEquals("{\"status\":403,\"message\":\"Invalid Authorization header\"}", new String(res.getBody()));
  }
}
