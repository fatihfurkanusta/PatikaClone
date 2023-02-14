package com.patika.Helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    private Connection connect = null;

    public Connection connectDB(){
        try {
            this.connect = DriverManager.getConnection(Config.DATA_URL,Config.USERNAME,Config.PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.connect;
    }

    public static Connection getInstance(){
        DBConnector db = new DBConnector();
        return db.connectDB();
    }
}

