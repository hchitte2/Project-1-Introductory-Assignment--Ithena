package com.ithena.jsplogin.db;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ithena.jsplogin.TestImages;
import com.ithena.jsplogin.model.Photo;
import com.ithena.jsplogin.model.User;
import com.ithena.jsplogin.security.PasswordHasher;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Runs the real schema.sql and SQL against an in-memory H2 database in MySQL mode. */
class UserDaoTest {

    private static final LocalDate DOB = LocalDate.of(1990, 12, 10);
    private UserDao dao;
    private byte[] photo;

    @BeforeEach
    void setUp() throws Exception {
        String url = "jdbc:h2:mem:" + UUID.randomUUID() + ";MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1";
        Database db = new Database("org.h2.Driver", url, "sa", "");
        db.initSchema();
        db.initSchema(); // second run must be harmless (IF NOT EXISTS)
        dao = new UserDao(db);
        photo = TestImages.png();
        dao.create("ada.l", "Ada", "Lovelace", "ada@example.com", DOB,
                new Photo(photo, "image/png"), PasswordHasher.hash("engine1843"));
    }

    @Test
    void createdUserCanBeFoundWithProfileAndPhoto() throws Exception {
        User user = dao.findByUserId("ada.l").orElseThrow();
        assertEquals("Ada Lovelace", user.getFullName());
        assertEquals("ada@example.com", user.getEmail());
        assertEquals(DOB, user.getDateOfBirth());

        Photo stored = dao.findPhoto("ada.l").orElseThrow();
        assertArrayEquals(photo, stored.bytes());
        assertEquals("image/png", stored.contentType());
    }

    @Test
    void storedPasswordHashVerifies() throws Exception {
        String hash = dao.findPasswordHash("ada.l").orElseThrow();
        assertTrue(PasswordHasher.verify("engine1843", hash));
        assertTrue(dao.findPasswordHash("nobody").isEmpty());
    }

    @Test
    void duplicateUserIdOrEmailIsRejected() throws Exception {
        assertTrue(dao.userIdExists("ada.l"));
        assertTrue(dao.emailExists("ada@example.com"));
        assertFalse(dao.userIdExists("grace.h"));

        assertThrows(DuplicateAccountException.class, () -> dao.create("ada.l", "A", "B", "other@example.com", DOB,
                new Photo(photo, "image/png"), "hash"));
        assertThrows(DuplicateAccountException.class, () -> dao.create("other", "A", "B", "ada@example.com", DOB,
                new Photo(photo, "image/png"), "hash"));
    }

    @Test
    void identityCheckRequiresAllThreeDetails() throws Exception {
        assertTrue(dao.identityMatches("ada.l", "ada@example.com", DOB));
        assertFalse(dao.identityMatches("ada.l", "ada@example.com", DOB.plusDays(1)));
        assertFalse(dao.identityMatches("ada.l", "wrong@example.com", DOB));
        assertFalse(dao.identityMatches("nobody", "ada@example.com", DOB));
    }

    @Test
    void passwordCanBeReset() throws Exception {
        assertTrue(dao.updatePassword("ada.l", PasswordHasher.hash("newPass99")));
        String hash = dao.findPasswordHash("ada.l").orElseThrow();
        assertTrue(PasswordHasher.verify("newPass99", hash));
        assertFalse(PasswordHasher.verify("engine1843", hash));
        assertFalse(dao.updatePassword("nobody", "hash"));
    }
}
