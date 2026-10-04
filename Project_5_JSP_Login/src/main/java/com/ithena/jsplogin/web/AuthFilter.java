package com.ithena.jsplogin.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/** Sends visitors who are not logged in to the login page, and stops browsers caching private pages. */
@WebFilter(urlPatterns = {"/home", "/photo"})
public class AuthFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        if (AppServlet.currentUserId(req) == null) {
            resp.sendRedirect(req.getContextPath() + "/login?required");
            return;
        }
        // After logout, the Back button must not show the cached landing page.
        resp.setHeader("Cache-Control", "no-store");
        chain.doFilter(req, resp);
    }
}
