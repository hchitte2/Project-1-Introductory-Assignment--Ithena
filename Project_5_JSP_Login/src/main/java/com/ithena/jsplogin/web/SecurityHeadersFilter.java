package com.ithena.jsplogin.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/** Adds standard browser security headers to every response. */
@WebFilter("/*")
public class SecurityHeadersFilter extends HttpFilter {

    private static final String CSP = String.join("; ",
            "default-src 'self'",
            "style-src 'self' https://fonts.googleapis.com",
            "font-src 'self' https://fonts.gstatic.com",
            "img-src 'self' blob:",
            "script-src 'self'",
            "form-action 'self'",
            "frame-ancestors 'none'");

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        resp.setHeader("Content-Security-Policy", CSP);
        resp.setHeader("X-Content-Type-Options", "nosniff");
        resp.setHeader("Referrer-Policy", "same-origin");
        chain.doFilter(req, resp);
    }
}
