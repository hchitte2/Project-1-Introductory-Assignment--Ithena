package com.ithena.jsplogin.web;

import com.ithena.jsplogin.security.PasswordHasher;
import com.ithena.jsplogin.validation.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

/**
 * Password recovery. Passwords are stored as one-way hashes, so they cannot be shown back;
 * instead the user proves who they are (User ID + email + date of birth) and sets a new one.
 */
@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends AppServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        render(req, resp, "forgot-password");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = Validator.normalizeUserId(req.getParameter("userId"));
        String email = Validator.normalizeEmail(req.getParameter("email"));
        String dateOfBirth = req.getParameter("dateOfBirth");
        String password = req.getParameter("password");

        Map<String, String> errors = Validator.validatePasswordReset(
                userId, email, dateOfBirth, password, req.getParameter("confirmPassword"));
        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            render(req, resp, "forgot-password");
            return;
        }

        try {
            LocalDate dob = Validator.parseDate(dateOfBirth).orElseThrow();
            if (!userDao().identityMatches(userId, email, dob)) {
                req.setAttribute("error",
                        "Those details don't match an account. Check your User ID, email, and date of birth.");
                render(req, resp, "forgot-password");
                return;
            }
            userDao().updatePassword(userId, PasswordHasher.hash(password));
        } catch (SQLException e) {
            throw new ServletException("Could not reset password", e);
        }
        redirect(req, resp, "/login?reset");
    }
}
