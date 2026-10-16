package com.example.farmbook;

import java.sql.SQLException;

/**
 * What the Settings screen needs from user storage, on top of login and register.
 */
public interface IAccountDAO extends IUserDAO {

    /** Returns the user's email, or null if there is no such user. */
    String getEmail(String username) throws SQLException;

    /** Saves a new email for the user. */
    void updateEmail(String username, String newEmail) throws SQLException;

    /** Saves a new password for the user. The password is hashed before it is stored. */
    void updatePassword(String username, String newPassword) throws SQLException;
}
