package com.ithena.jsplogin.web;

import com.ithena.jsplogin.db.DuplicateAccountException;
import com.ithena.jsplogin.model.Photo;
import com.ithena.jsplogin.model.Registration;
import com.ithena.jsplogin.model.User;
import com.ithena.jsplogin.security.PasswordHasher;
import com.ithena.jsplogin.validation.ImageTypes;
import com.ithena.jsplogin.validation.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

/** Registration form: validates input, stores the new user with a hashed password and photo. */
@WebServlet("/register")
@MultipartConfig(maxFileSize = 10 * 1024 * 1024, maxRequestSize = 11 * 1024 * 1024)
public class RegisterServlet extends AppServlet {

    /** Session attribute read once by {@link RegisteredServlet} to show the confirmation page. */
    static final String FLASH_REGISTERED = "registeredUser";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (currentUserId(req) != null) {
            redirect(req, resp, "/home");
            return;
        }
        if (req.getParameter("photoTooLarge") != null) {
            req.setAttribute("errors", Map.of("photo", "That photo is larger than 2 MB. Choose a smaller one and fill in the form again."));
        }
        render(req, resp, "register");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        byte[] photo;
        try {
            photo = readPhoto(req.getPart("photo"));
        } catch (IllegalStateException e) {
            // The upload exceeded the container limit in @MultipartConfig. Tomcat now rethrows on every
            // getParameter() for this request, so redirect instead of re-rendering the form here.
            redirect(req, resp, "/register?photoTooLarge");
            return;
        }

        Registration form = new Registration(
                req.getParameter("firstName"), req.getParameter("lastName"), req.getParameter("email"),
                req.getParameter("dateOfBirth"), req.getParameter("userId"), req.getParameter("password"),
                req.getParameter("confirmPassword"), photo);

        Map<String, String> errors = new LinkedHashMap<>(Validator.validateRegistration(form));
        String userId = Validator.normalizeUserId(form.userId());
        String email = Validator.normalizeEmail(form.email());

        try {
            if (!errors.containsKey("userId") && userDao().userIdExists(userId)) {
                errors.put("userId", "That User ID is taken. Try another.");
            }
            if (!errors.containsKey("email") && userDao().emailExists(email)) {
                errors.put("email", "An account already uses this email. Log in or reset your password.");
            }
            if (!errors.isEmpty()) {
                showErrors(req, resp, errors);
                return;
            }

            String firstName = Validator.normalizeName(form.firstName());
            String lastName = Validator.normalizeName(form.lastName());
            LocalDate dateOfBirth = Validator.parseDate(form.dateOfBirth()).orElseThrow();
            String photoType = ImageTypes.detect(photo).orElseThrow();
            userDao().create(userId, firstName, lastName, email, dateOfBirth,
                    new Photo(photo, photoType), PasswordHasher.hash(form.password()));

            req.getSession(true).setAttribute(FLASH_REGISTERED,
                    new User(userId, firstName, lastName, email, dateOfBirth, null));
            redirect(req, resp, "/registered");
        } catch (DuplicateAccountException e) {
            showErrors(req, resp, Map.of("userId", e.getMessage()));
        } catch (SQLException e) {
            throw new ServletException("Could not register user", e);
        }
    }

    private void showErrors(HttpServletRequest req, HttpServletResponse resp, Map<String, String> errors)
            throws ServletException, IOException {
        req.setAttribute("errors", errors);
        render(req, resp, "register");
    }

    /** Reads at most MAX_PHOTO_BYTES + 1 bytes, which is enough for the validator to detect an oversized file. */
    private static byte[] readPhoto(Part part) throws IOException {
        if (part == null || part.getSize() == 0) {
            return null;
        }
        try (InputStream in = part.getInputStream()) {
            return in.readNBytes(Validator.MAX_PHOTO_BYTES + 1);
        }
    }
}
