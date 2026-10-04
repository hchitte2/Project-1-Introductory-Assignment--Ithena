package com.ithena.jsplogin.db;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** JDBC connection settings plus schema setup. Opens a new connection per call. */
public final class Database {

    private final String url;
    private final String user;
    private final String password;

    public Database(String driverClass, String url, String user, String password) {
        try {
            // Explicit load: drivers inside a webapp are not always auto-registered with DriverManager.
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("JDBC driver not found on the classpath: " + driverClass, e);
        }
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /** Runs schema.sql from the classpath. Statements use IF NOT EXISTS, so this is safe on every startup. */
    public void initSchema() throws SQLException {
        String script;
        try (InputStream in = Database.class.getResourceAsStream("/schema.sql")) {
            if (in == null) {
                throw new IllegalStateException("schema.sql not found on the classpath");
            }
            script = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            for (String sql : script.replaceAll("(?m)^--.*$", "").split(";")) {
                if (!sql.isBlank()) {
                    stmt.execute(sql);
                }
            }
        }
    }
}
