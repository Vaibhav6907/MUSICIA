package com.musicia.servlet;

import com.musicia.dao.UserDAO;
import com.musicia.model.User;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        try {
            if ("/verify".equals(pathInfo)) {
                handleVerify(request, response);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            JSONObject err = new JSONObject();
            err.put("success", false);
            err.put("message", "Error: " + e.getMessage());
            response.getWriter().write(err.toString());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        try {
            if ("/login".equals(pathInfo)) {
                handleLogin(request, response);
            } else if ("/register".equals(pathInfo)) {
                handleRegister(request, response);
            } else if ("/logout".equals(pathInfo)) {
                handleLogout(request, response);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            JSONObject err = new JSONObject();
            err.put("success", false);
            err.put("message", "Error: " + e.getMessage());
            response.getWriter().write(err.toString());
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println("Login attempt - Username: " + username);

        // Authenticate user from database
        User user = userDAO.authenticateUser(username, password);
        
        JSONObject jsonResponse = new JSONObject();
        if (user != null) {
            // Create server session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(30 * 60); // 30 minutes
            
            System.out.println("Login successful - User ID: " + user.getId());
            
            jsonResponse.put("success", true);
            jsonResponse.put("user", new JSONObject()
                .put("id", user.getId())
                .put("username", user.getUsername())
                .put("email", user.getEmail())
                .put("isArtist", user.isArtist()));
        } else {
            System.out.println("Login failed - Invalid credentials");
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Invalid username or password");
        }

        response.setContentType("application/json");
        response.getWriter().write(jsonResponse.toString());
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        boolean isArtist = Boolean.parseBoolean(request.getParameter("isArtist"));

        System.out.println("Register attempt - Username: " + username + ", Email: " + email);

        // Validate inputs
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty() || 
            email == null || email.trim().isEmpty()) {
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("success", false);
            jsonResponse.put("message", "All fields are required");
            response.setContentType("application/json");
            response.getWriter().write(jsonResponse.toString());
            return;
        }

        try {
            // Check if user already exists
            if (userDAO.userExists(username)) {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Username already exists");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.getWriter().write(jsonResponse.toString());
                return;
            }

            if (userDAO.emailExists(email)) {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Email already registered");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.getWriter().write(jsonResponse.toString());
                return;
            }

            // Create new user and save to database
            User newUser = new User(username, password, email, isArtist);
            userDAO.registerUser(newUser);

            System.out.println("Registration successful - Username: " + username);

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("success", true);
            jsonResponse.put("message", "Registration successful. Please login.");

            response.setContentType("application/json");
            response.getWriter().write(jsonResponse.toString());
        } catch (Exception e) {
            System.out.println("Registration error: " + e.getMessage());
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Registration failed: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write(jsonResponse.toString());
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", true);
        jsonResponse.put("message", "Logout successful");

        response.setContentType("application/json");
        response.getWriter().write(jsonResponse.toString());
    }

    private void handleVerify(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        HttpSession session = request.getSession(false);
        JSONObject jsonResponse = new JSONObject();

        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            jsonResponse.put("success", true);
            jsonResponse.put("user", new JSONObject()
                .put("id", user.getId())
                .put("username", user.getUsername())
                .put("email", user.getEmail())
                .put("isArtist", user.isArtist()));
        } else {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "No active session");
        }

        response.setContentType("application/json");
        response.getWriter().write(jsonResponse.toString());
    }
}