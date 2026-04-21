package com.example.carapi.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Added integratedSecurity=true to use Windows Authentication
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=car_servlet_db;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";

    // Establish and return the database connection
    public static Connection getConnection() throws SQLException {
        try {
            // Load the Microsoft SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Connect using only the URL. The Windows system handles the authentication automatically.
            return DriverManager.getConnection(URL);
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQL Server driver not found. Please check your pom.xml", e);
        }
    }
}