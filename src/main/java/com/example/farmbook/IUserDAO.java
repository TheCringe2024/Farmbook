package com.example.farmbook;
import java.sql.SQLException;

public interface IUserDAO {
    void addUser(String username, String email, String password) throws SQLException;
    boolean isValidLogin(String username, String password) throws SQLException;
    boolean usernameExists(String username) throws SQLException;
    boolean emailExists(String email) throws SQLException;
}
