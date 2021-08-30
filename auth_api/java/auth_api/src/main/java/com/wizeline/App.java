package com.wizeline;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import static com.wizeline.JsonUtil.json;
import static spark.Spark.*;

public class App {
  private static final RemoteDatabase db = RemoteDatabase.getInstance();

    public static void main(String[] args) {
      System.out.println( "Listening on: http://localhost:8000/" );

      port(8000);
      get("/", App::routeRoot);
      get("/_health", App::routeRoot);
      post("/login", App::urlLogin, json());
      get("/protected", App::protect, json());

      exception(IllegalArgumentException.class, (e, req, res) -> {
        res.status(403);
        res.type("application/json");
        res.body(JsonUtil.toJson(new ErrorResponse(403, e.getMessage())));
      });

      exception(JWTVerificationException.class, (e, req, res) -> {
        res.status(403);
        res.type("application/json");
        res.body(JsonUtil.toJson(new ErrorResponse(403, e.getMessage())));
      });

      exception(AlgorithmMismatchException.class, (e, req, res) -> {
        res.status(403);
        res.type("application/json");
        res.body(JsonUtil.toJson(new ErrorResponse(403, e.getMessage())));
      });

      exception(InvalidClaimException.class, (e, req, res) -> {
        res.status(403);
        res.type("application/json");
        res.body(JsonUtil.toJson(new ErrorResponse(403, e.getMessage())));
      });

      exception(TokenExpiredException.class, (e, req, res) -> {
        res.status(403);
        res.type("application/json");
        res.body(JsonUtil.toJson(new ErrorResponse(403, e.getMessage())));
      });
    }

    public static Object routeRoot(spark.Request req, spark.Response res) throws Exception {
      return "OK";
    }

    public static Object urlLogin(spark.Request req, spark.Response res) throws Exception {
      String username = req.queryParams("username");
      String password = req.queryParams("password");

      if (username == null || username.length() == 0 || password == null || password.length() == 0) {
        res.status(403);
        return new ErrorResponse(403, "Invalid credentials");
      }

      User user = db.findUserByUsername(username);
      if (user == null) {
        res.status(403);
        return new ErrorResponse(403, "User does not exist");
      }

      String hashedPassword = Methods.hashTextWithSHA512(password + user.salt);
      if (!hashedPassword.equals(user.password)) {
        res.status(403);
        return new ErrorResponse(403, "Forbidden");
      }

      Response r = new Response(Methods.generateToken(user.role));
      res.type("application/json");
      return r;
    }

    public static Object protect(spark.Request req, spark.Response res) throws Exception {
      String authorization = req.headers("Authorization");
      if (authorization == null) {
        throw new IllegalArgumentException("Invalid Authorization header");
      }

      Response r = new Response(Methods.accessData(authorization));
      res.type("application/json");
      return r;
    }
}
