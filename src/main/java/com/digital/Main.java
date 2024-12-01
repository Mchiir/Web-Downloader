package com.digital;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        var db = new DBConnector();

        try (Connection conn = db.getConnection()) {
            System.out.println("Connected to the database!");
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("General Error: " + e.getMessage());
        }
    }
}