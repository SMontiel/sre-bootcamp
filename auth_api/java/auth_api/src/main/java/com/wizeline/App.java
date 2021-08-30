package com.wizeline;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.wizeline.data.Database;
import com.wizeline.data.RemoteDatabase;
import com.wizeline.data.User;
import spark.Route;

import static com.wizeline.JsonUtil.json;
import static spark.Spark.*;

public class App {

  public static void main(String[] args) {
    new App(RemoteDatabase.getInstance());
  }

  private final Database db;

  public App(Database db) {
    this.db = db;
    System.out.println( "Listening on: http://localhost:8000/" );

    port(8000);
    get("/", routeRoot());
    get("/_health", routeRoot());
    post("/login", urlLogin(), json());
    get("/protected", protect(), json());

    initExceptionHandlers();
  }

  public Route routeRoot() {
    return (request, response) -> "OK";
  }

  public Route urlLogin() {
    return (req, res) -> {
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
    };
  }

  public Route protect() {
    return (req, res) -> {
      String authorization = req.headers("Authorization");
      if (authorization == null) {
        throw new IllegalArgumentException("Invalid Authorization header");
      }

      Response r = new Response(Methods.accessData(authorization));
      res.type("application/json");
      return r;
    };
  }

  private void initExceptionHandlers() {
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
}
