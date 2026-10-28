package com.sagiphayop.dao;

import com.sagiphayop.model.User;
import com.sagiphayop.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDao {

    /** Registers a new account. Always starts as PENDING. Returns false if the username is taken. */
    public boolean registerUser(String username, String plainPassword, String role) {
        String sql = "INSERT INTO users (username, password_hash, roles, status) VALUES (?, ?, ?, 'PENDING')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, PasswordUtil.hash(plainPassword));
            ps.setString(3, role.toUpperCase());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            return false;   // UNIQUE violation or other failure
        }
    }

    public boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            return false;
        }
    }

    /** Returns the user if credentials match (any status); the caller checks status. */
    public Optional<User> authenticate(String username, String plainPassword) {
        String sql = "SELECT id, username, roles, status FROM users WHERE username = ? AND password_hash = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, PasswordUtil.hash(plainPassword));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            return Optional.empty();
        }
    }

    private User map(ResultSet rs) throws SQLException {
        return new User(rs.getInt("id"), rs.getString("username"),
                        rs.getString("roles"), rs.getString("status"));
    }
}
