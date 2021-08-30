package com.wizeline.data;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.smontiel.simple_jdbc.SimpleJDBC;

public class RemoteDatabase implements Database {
  private static RemoteDatabase instance;
  public SimpleJDBC db;

  private RemoteDatabase(SimpleJDBC db) {
    this.db = db;
  }

  public static RemoteDatabase getInstance() {
    if (instance == null) {
      MysqlDataSource dataSource = new MysqlDataSource();
      dataSource.setServerName("bootcamp-tht.sre.wize.mx");
      dataSource.setPort(3306);
      dataSource.setDatabaseName("bootcamp_tht");
      dataSource.setUser("secret");
      dataSource.setPassword("noPow3r");

      SimpleJDBC db = SimpleJDBC.from(dataSource);
      instance = new RemoteDatabase(db);
    }

    return instance;
  }

  @Override
  public User findUserByUsername(String username) {
    return db.oneOrNone("SELECT * FROM users WHERE username = '" + username + "'", rs -> {
      return new User(rs.getString("username"), rs.getString("password"), rs.getString("salt"), rs.getString("role"));
    });
  }
}
