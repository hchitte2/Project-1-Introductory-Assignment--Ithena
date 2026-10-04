package com.ithena.jsplogin.web;

import com.ithena.jsplogin.security.PasswordHasher;
import com.ithena.jsplogin.validation.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/** Shows the login form and checks the User ID and password against the database. */
@WebServlet("/login")
public class LoginServlet extends AppServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (currentUserId(req) != null) {
            redirect(req, resp, "/home");
            return;
        }
        render(req, resp, "login");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = Validator.normalizeUserId(req.getParameter("userId"));
        String password = req.getParameter("password");
        req.setAttribute("userId", userId);

        if (userId.isEmpty() || password == null || password.isEmpty()) {
            req.setAttribute("error", "Enter your User ID and password.");
            render(req, resp, "login");
            return;
        }

        String storedHash;
        try {
            storedHash = userDao().findPasswordHash(userId).orElse(null);
        } catch (SQLException e) {
            throw new ServletException("Could not check login", e);
        }

        // Same message for unknown user and wrong password, so the form does not reveal which User IDs exist.
        if (!PasswordHasher.verify(password, storedHash)) {
            req.setAttribute("error", "That User ID and password don't match. Try again or reset your password.");
            render(req, resp, "login");
            return;
        }

        // Start a fresh session on login to prevent session fixation.
        HttpSession old = req.getSession(false);
        if (old != null) {
            old.invalidate();
        }
        req.getSession(true).setAttribute(SESSION_USER, userId);
        redirect(req, resp, "/home");
    }
}
