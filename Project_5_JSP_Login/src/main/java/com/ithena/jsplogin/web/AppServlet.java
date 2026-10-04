package com.ithena.jsplogin.web;

import com.ithena.jsplogin.db.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/** Shared helpers for the app's servlets (controllers). Views are JSPs under /WEB-INF/views. */
abstract class AppServlet extends HttpServlet {

    /** Session attribute holding the logged-in user's ID. */
    static final String SESSION_USER = "userId";

    protected UserDao userDao() {
        return (UserDao) getServletContext().getAttribute(AppContextListener.USER_DAO);
    }

    protected void render(HttpServletRequest req, HttpServletResponse resp, String view)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/" + view + ".jsp").forward(req, resp);
    }

    protected void redirect(HttpServletRequest req, HttpServletResponse resp, String path) throws IOException {
        resp.sendRedirect(req.getContextPath() + path);
    }

    static String currentUserId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session == null ? null : (String) session.getAttribute(SESSION_USER);
    }
}
