package com.example.carapi.dao;

import com.example.carapi.config.DBConnection;
import com.example.carapi.model.Car;
import com.example.carapi.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {

    // Retrieve all cars with their owners using an INNER JOIN
    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        // Select fields from both tables and alias the IDs to avoid confusion
        String sql = "SELECT c.id AS car_id, c.make, c.model, c.manufacturing_year, " +
                "u.id AS user_id, u.name, u.email " +
                "FROM cars c " +
                "INNER JOIN users u ON c.user_id = u.id";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // First, construct the User object from the ResultSet
                User owner = new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email")
                );

                // Second, construct the Car object and pass the User object into it
                cars.add(new Car(
                        rs.getInt("car_id"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getInt("manufacturing_year"),
                        owner
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    // Retrieve a single car with its owner
    public Car getCarById(int id) {
        String sql = "SELECT c.id AS car_id, c.make, c.model, c.manufacturing_year, " +
                "u.id AS user_id, u.name, u.email " +
                "FROM cars c " +
                "INNER JOIN users u ON c.user_id = u.id " +
                "WHERE c.id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User owner = new User(
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("email")
                    );

                    return new Car(
                            rs.getInt("car_id"),
                            rs.getString("make"),
                            rs.getString("model"),
                            rs.getInt("manufacturing_year"),
                            owner
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Create a new car record
    public boolean addCar(Car car) {
        String sql = "INSERT INTO cars (make, model, manufacturing_year, user_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, car.getMake());
            pstmt.setString(2, car.getModel());
            pstmt.setInt(3, car.getManufacturingYear());

            // Extract the user ID from the nested User object
            pstmt.setInt(4, car.getUser().getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update an existing car record
    public boolean updateCar(Car car) {
        String sql = "UPDATE cars SET make = ?, model = ?, manufacturing_year = ?, user_id = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, car.getMake());
            pstmt.setString(2, car.getModel());
            pstmt.setInt(3, car.getManufacturingYear());

            // Extract the user ID from the nested User object
            pstmt.setInt(4, car.getUser().getId());

            pstmt.setInt(5, car.getId());
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete a car record by ID (No changes needed)
    public boolean deleteCar(int id) {
        String sql = "DELETE FROM cars WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}