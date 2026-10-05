package com.ithena.jsplogin.web;

import com.ithena.jsplogin.model.Photo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/** Streams the logged-in user's profile photo from the database. */
@WebServlet("/photo")
public class PhotoServlet extends AppServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Photo> photo;
        try {
            photo = userDao().findPhoto(currentUserId(req));
        } catch (SQLException e) {
            throw new ServletException("Could not load photo", e);
        }
        if (photo.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        byte[] bytes = photo.get().bytes();
        resp.setContentType(photo.get().contentType());
        resp.setContentLength(bytes.length);
        resp.getOutputStream().write(bytes);
    }
}
