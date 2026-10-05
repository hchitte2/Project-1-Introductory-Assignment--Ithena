package com.ithena.jsplogin.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/** Confirmation page shown once after a successful registration (Post/Redirect/Get). */
@WebServlet("/registered")
public class RegisteredServlet extends AppServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Object user = session == null ? null : session.getAttribute(RegisterServlet.FLASH_REGISTERED);
        if (user == null) {
            redirect(req, resp, "/login");
            return;
        }
        session.removeAttribute(RegisterServlet.FLASH_REGISTERED);
        req.setAttribute("user", user);
        render(req, resp, "registered");
    }
}
