package com.ithena.jsplogin.web;

import com.ithena.jsplogin.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

/** Landing page after login: today's date, a welcome message, and the user's photo. */
@WebServlet("/home")
public class HomeServlet extends AppServlet {

    private static final DateTimeFormatter LONG_DATE = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.US);
    private static final DateTimeFormatter DAY_AND_DATE = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.US);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<User> user;
        try {
            user = userDao().findByUserId(currentUserId(req));
        } catch (SQLException e) {
            throw new ServletException("Could not load user", e);
        }
        if (user.isEmpty()) {
            // Account no longer exists: end the session.
            req.getSession().invalidate();
            redirect(req, resp, "/login");
            return;
        }

        User u = user.get();
        LocalDate today = LocalDate.now();
        req.setAttribute("user", u);
        req.setAttribute("today", today.format(DAY_AND_DATE));
        req.setAttribute("todayIso", today.toString());
        req.setAttribute("dateOfBirth", u.getDateOfBirth().format(LONG_DATE));
        req.setAttribute("memberSince", u.getCreatedAt() == null ? "" : u.getCreatedAt().format(LONG_DATE));
        render(req, resp, "home");
    }
}
