package edu.icet.demo.util;

import edu.icet.demo.model.User;

/**
 * Holds the currently logged-in user for the lifetime of the application.
 */
public final class UserSession {

    private static User currentUser;

    private UserSession() {
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static void clear() {
        currentUser = null;
    }

    public static boolean isAdmin() {
        return currentUser != null && "ADMIN".equalsIgnoreCase(currentUser.getRole());
    }
}
