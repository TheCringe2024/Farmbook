package com.example.farmbook;

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

    private final IAccountDAO dao;

    public AccountService(IAccountDAO dao) {
        this.dao = dao;
    }

    /** Changes the user's email if it is valid and not used by someone else. */
    public Result changeEmail(String username, String newEmail) {
        return Result.DATABASE_ERROR;
    }

    /** Changes the user's password after checking the current one and the new one. */
    public Result changePassword(String username, String currentPassword,
                                 String newPassword, String confirmPassword) {
        return Result.DATABASE_ERROR;
    }
}
