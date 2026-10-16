package com.example.farmbook;

import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * Rules for changing an account's email and password.
 */
public class AccountService {

    /** Every possible outcome, so the screen can show a clear message for each. */
    public enum Result {
        OK, INVALID_EMAIL, EMAIL_TAKEN, USER_NOT_FOUND, WRONG_PASSWORD,
        PASSWORDS_DO_NOT_MATCH, PASSWORD_TOO_SHORT, SAME_PASSWORD, DATABASE_ERROR
    }

    public static final int MIN_PASSWORD_LENGTH = 6;

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final IAccountDAO dao;

    public AccountService(IAccountDAO dao) {
        this.dao = dao;
    }

    /** Changes the user's email if it is valid and not used by someone else. */
    public Result changeEmail(String username, String newEmail) {
        if (newEmail == null || !EMAIL.matcher(newEmail).matches()) {
            return Result.INVALID_EMAIL;
        }
        try {
            String currentEmail = dao.getEmail(username);
            if (currentEmail == null) {
                return Result.USER_NOT_FOUND;
            }
            if (!currentEmail.equals(newEmail) && dao.emailExists(newEmail)) {
                return Result.EMAIL_TAKEN;
            }
            dao.updateEmail(username, newEmail);
            return Result.OK;
        } catch (SQLException e) {
            return Result.DATABASE_ERROR;
        }
    }

    /** Changes the user's password after checking the current one and the new one. */
    public Result changePassword(String username, String currentPassword,
                                 String newPassword, String confirmPassword) {
        if (newPassword == null || confirmPassword == null || currentPassword == null) {
            return Result.WRONG_PASSWORD;
        }
        if (!newPassword.equals(confirmPassword)) {
            return Result.PASSWORDS_DO_NOT_MATCH;
        }
        if (newPassword.length() < MIN_PASSWORD_LENGTH) {
            return Result.PASSWORD_TOO_SHORT;
        }
        if (newPassword.equals(currentPassword)) {
            return Result.SAME_PASSWORD;
        }
        try {
            if (!dao.isValidLogin(username, currentPassword)) {
                return Result.WRONG_PASSWORD;
            }
            dao.updatePassword(username, newPassword);
            return Result.OK;
        } catch (SQLException e) {
            return Result.DATABASE_ERROR;
        }
    }
}
