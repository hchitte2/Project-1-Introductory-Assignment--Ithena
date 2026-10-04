package com.ithena.jsplogin.db;

import com.ithena.jsplogin.model.Photo;
import com.ithena.jsplogin.model.User;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Optional;

/**
 * All SQL for the users table. Every query uses PreparedStatement parameters (no string
 * concatenation), which prevents SQL injection. User IDs and emails are stored lowercase.
 */
public class UserDao {

    private final Database db;

    public UserDao(Database db) {
        this.db = db;
    }

    public boolean userIdExists(String userId) throws SQLException {
        return exists("SELECT 1 FROM users WHERE user_id = ?", userId);
    }

    public boolean emailExists(String email) throws SQLException {
        return exists("SELECT 1 FROM users WHERE email = ?", email);
    }

    public void create(String userId, String firstName, String lastName, String email, LocalDate dateOfBirth,
                       Photo photo, String passwordHash) throws SQLException, DuplicateAccountException {
        String sql = "INSERT INTO users (user_id, first_name, last_name, email, date_of_birth, photo, photo_type, password_hash) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, email);
            ps.setDate(5, Date.valueOf(dateOfBirth));
            ps.setBytes(6, photo.bytes());
            ps.setString(7, photo.contentType());
            ps.setString(8, passwordHash);
            ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            // Another request registered the same User ID or email between our check and this insert.
            throw new DuplicateAccountException("That User ID or email address is already registered.");
        }
    }

    public Optional<User> findByUserId(String userId) throws SQLException {
        String sql = "SELECT user_id, first_name, last_name, email, date_of_birth, created_at FROM users WHERE user_id = ?";
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                Timestamp created = rs.getTimestamp("created_at");
                return Optional.of(new User(
                        rs.getString("user_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getDate("date_of_birth").toLocalDate(),
                        created == null ? null : created.toLocalDateTime()));
            }
        }
    }

    public Optional<String> findPasswordHash(String userId) throws SQLException {
        String sql = "SELECT password_hash FROM users WHERE user_id = ?";
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(rs.getString(1)) : Optional.empty();
            }
        }
    }

    public Optional<Photo> findPhoto(String userId) throws SQLException {
        String sql = "SELECT photo, photo_type FROM users WHERE user_id = ?";
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(new Photo(rs.getBytes(1), rs.getString(2))) : Optional.empty();
            }
        }
    }

    /** True if an account has this User ID, email, and date of birth (used to verify a password reset). */
    public boolean identityMatches(String userId, String email, LocalDate dateOfBirth) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE user_id = ? AND email = ? AND date_of_birth = ?";
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, email);
            ps.setDate(3, Date.valueOf(dateOfBirth));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean updatePassword(String userId, String passwordHash) throws SQLException {
        String sql = "UPDATE users SET password_hash = ? WHERE user_id = ?";
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, passwordHash);
            ps.setString(2, userId);
            return ps.executeUpdate() == 1;
        }
    }

    private boolean exists(String sql, String value) throws SQLException {
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
