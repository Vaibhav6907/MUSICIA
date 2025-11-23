package com.musicia.dao;

import com.musicia.model.User;
import com.musicia.util.DatabaseUtil;

import java.sql.*;

public class UserDAO {
    
    /**
     * Authenticates user by checking username and password in database
     */
    public User authenticateUser(String username, String password) throws Exception {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setArtist(rs.getBoolean("is_artist"));
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * Registers a new user in the database
     */
    public void registerUser(User user) throws Exception {
        String query = "INSERT INTO users (username, password, email, is_artist) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setBoolean(4, user.isArtist());
            
            stmt.executeUpdate();
        }
    }

    /**
     * Gets user by ID
     */
    public User getUserById(int id) throws Exception {
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setArtist(rs.getBoolean("is_artist"));
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * Checks if username already exists
     */
    public boolean userExists(String username) throws Exception {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * Checks if email already exists
     */
    public boolean emailExists(String email) throws Exception {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}