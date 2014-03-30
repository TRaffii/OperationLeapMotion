/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.model;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import mygame.model.Users;

/**
 *
 * @author Rafal
 */
public class DatabaseManager {

    public static final String DRIVER = "org.sqlite.JDBC";
    public static final String DB_URL = "jdbc:sqlite:userManagment.db";
    private Connection conn;
    private Statement stat;

    public DatabaseManager() {
        try {
            Class.forName(DRIVER);

        } catch (ClassNotFoundException e) {
            System.err.println("Cannot obtain JDBC driver");
            e.printStackTrace();
        }

        try {
            conn = DriverManager.getConnection(DB_URL);
            stat = conn.createStatement();
        } catch (SQLException e) {
            System.err.println("Unable to open connection ");
            e.printStackTrace();
        }
        createTables();
    }

    public boolean createTables() {
        String createUsers = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, userName varchar(255),  result int, dateTime Date)";
        try {
            stat.execute(createUsers);
        } catch (SQLException e) {
            System.err.println("Error while creating database");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean insertUser(String userName, int result, Date dateTime) {
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "insert into users values (NULL, ?, ?, ?);");
            prepStmt.setString(1, userName);
            prepStmt.setInt(2, result);
            prepStmt.setDate(3, dateTime);
            prepStmt.execute();
        } catch (SQLException e) {
            System.err.println("Unable to insert user");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public List<Users> selectUsers() {
        List<Users> users = new LinkedList<Users>();
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM users");
            int id;
            String userName;
            int resultVar;
            Date dateTime;
            while(result.next()) {
                //id = result.getInt("id");
                userName = result.getString("userName");
                resultVar = result.getInt("result");
                dateTime = result.getDate("dateTime");
                users.add(new Users(userName, resultVar,dateTime));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return users;
    }
    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.err.println("Unable to quit connection");
            e.printStackTrace();
        }
    }
}
