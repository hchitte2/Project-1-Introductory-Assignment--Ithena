package com.ithena.jsplogin.web;

import com.ithena.jsplogin.db.Database;
import com.ithena.jsplogin.db.UserDao;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;

/**
 * Connects to the database when the app starts. Settings come from environment variables
 * (DB_DRIVER, DB_URL, DB_USER, DB_PASSWORD) if set, otherwise from context params in web.xml.
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    static final String USER_DAO = "userDao";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext ctx = event.getServletContext();
        Database db = new Database(
                setting(ctx, "DB_DRIVER", "db.driver"),
                setting(ctx, "DB_URL", "db.url"),
                setting(ctx, "DB_USER", "db.user"),
                setting(ctx, "DB_PASSWORD", "db.password"));
        try {
            db.initSchema();
        } catch (SQLException e) {
            throw new IllegalStateException("Could not set up the database: " + e.getMessage(), e);
        }
        ctx.setAttribute(USER_DAO, new UserDao(db));
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // Deregister JDBC drivers loaded by this app so Tomcat can unload it cleanly on redeploy.
        ClassLoader appLoader = Thread.currentThread().getContextClassLoader();
        for (Driver driver : Collections.list(DriverManager.getDrivers())) {
            if (driver.getClass().getClassLoader() == appLoader) {
                try {
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException e) {
                    event.getServletContext().log("Could not deregister JDBC driver " + driver, e);
                }
            }
        }
    }

    private static String setting(ServletContext ctx, String envVar, String contextParam) {
        String value = System.getenv(envVar);
        if (value != null && !value.isBlank()) {
            return value;
        }
        value = ctx.getInitParameter(contextParam);
        return value == null ? "" : value;
    }
}
