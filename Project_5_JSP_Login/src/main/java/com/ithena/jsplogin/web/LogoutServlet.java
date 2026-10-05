package com.ithena.jsplogin.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/** Ends the session. Logout is a POST so links or prefetching cannot log a user out by accident. */
@WebServlet("/logout")
public class LogoutServlet extends AppServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        redirect(req, resp, "/login?loggedOut");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        redirect(req, resp, "/home");
    }
}
