package com.example.carapi.model;

public class Car {
    private int id;
    private String make;
    private String model;
    private int manufacturingYear;

    // Hold the full User object instead of just the ID
    private User user;

    public Car() {}

    public Car(int id, String make, String model, int manufacturingYear, User user) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.manufacturingYear = manufacturingYear;
        this.user = user;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getManufacturingYear() { return manufacturingYear; }
    public void setManufacturingYear(int manufacturingYear) { this.manufacturingYear = manufacturingYear; }

    // Updated Getter and Setter to return the User object
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}