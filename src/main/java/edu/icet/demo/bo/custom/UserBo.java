package edu.icet.demo.bo.custom;

import edu.icet.demo.bo.SuperBo;
import edu.icet.demo.model.User;

public interface UserBo extends SuperBo {

    /**
     * Verifies the credentials and returns the matching user, or {@code null}
     * if the username/email or password is wrong.
     */
    User authenticate(String usernameOrEmail, String password);

    /** Adds a staff account; the plain-text password is hashed before saving. */
    boolean addUser(User user);

    String generateUserId();
}
