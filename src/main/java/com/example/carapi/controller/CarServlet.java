package com.example.carapi.controller;

import com.example.carapi.dao.CarDAO;
import com.example.carapi.model.Car;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

// Maps all requests starting with /api/cars/ to this servlet
@WebServlet("/api/cars/*")
public class CarServlet extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();

    // The core Jackson class for reading and writing JSON
    private final ObjectMapper mapper = new ObjectMapper();

    // Helper method to set common response headers for JSON
    private void setJsonResponse(HttpServletResponse resp, int statusCode) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(statusCode);
    }

    // Handle READ operations (GET)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // No ID provided, return all cars
            List<Car> cars = carDAO.getAllCars();
            setJsonResponse(resp, HttpServletResponse.SC_OK);

            // Marshalling: Convert List of Objects to JSON and write to response
            mapper.writeValue(resp.getOutputStream(), cars);
        } else {
            // Extract ID from the path (e.g., /api/cars/5)
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                Car car = carDAO.getCarById(id);
                if (car != null) {
                    setJsonResponse(resp, HttpServletResponse.SC_OK);
                    // Marshalling: Convert Object to JSON and write to response
                    mapper.writeValue(resp.getOutputStream(), car);
                } else {
                    setJsonResponse(resp, HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().print("{\"message\": \"Car not found\"}");
                }
            } catch (NumberFormatException e) {
                setJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().print("{\"message\": \"Invalid ID format\"}");
            }
        }
    }

    // Handle CREATE operations (POST)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Unmarshalling: Read JSON payload from the request body and convert to Car object
        Car newCar = mapper.readValue(req.getReader(), Car.class);

        if (carDAO.addCar(newCar)) {
            setJsonResponse(resp, HttpServletResponse.SC_CREATED);
            resp.getWriter().print("{\"message\": \"Car created successfully\"}");
        } else {
            setJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print("{\"message\": \"Failed to create car\"}");
        }
    }

    // Handle UPDATE operations (PUT)
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.length() > 1) {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));

                // Unmarshalling: Read JSON payload from the request body and convert to Car object
                Car updatedCar = mapper.readValue(req.getReader(), Car.class);
                updatedCar.setId(id); // Ensure the ID matches the URL

                if (carDAO.updateCar(updatedCar)) {
                    setJsonResponse(resp, HttpServletResponse.SC_OK);
                    resp.getWriter().print("{\"message\": \"Car updated successfully\"}");
                } else {
                    setJsonResponse(resp, HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().print("{\"message\": \"Car not found or update failed\"}");
                }
            } catch (NumberFormatException e) {
                setJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().print("{\"message\": \"Invalid ID format\"}");
            }
        } else {
            setJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"message\": \"Missing car ID\"}");
        }
    }

    // Handle DELETE operations (DELETE)
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.length() > 1) {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));

                if (carDAO.deleteCar(id)) {
                    setJsonResponse(resp, HttpServletResponse.SC_OK);
                    resp.getWriter().print("{\"message\": \"Car deleted successfully\"}");
                } else {
                    setJsonResponse(resp, HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().print("{\"message\": \"Car not found\"}");
                }
            } catch (NumberFormatException e) {
                setJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().print("{\"message\": \"Invalid ID format\"}");
            }
        } else {
            setJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"message\": \"Missing car ID\"}");
        }
    }
}