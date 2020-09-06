package c868.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import c868.Models.User;


public class SQLLogin implements InterfaceLogin {
    private final static Connection conn = c868.Main.MainApp.conn;
    
    
    // Constructor
    public SQLLogin() {
    }
    
    
    // Retrieve list of active users.
    @Override
    public ObservableList<User> getAllUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        String getPatientQuery = "SELECT * FROM user WHERE active = 1"; 

        try {
            PreparedStatement statement = conn.prepareStatement(getPatientQuery);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                User fetchedUser = new User();
                
                fetchedUser.setUsername(resultSet.getString("userName"));
                fetchedUser.setPassword(resultSet.getString("password"));
                fetchedUser.setUserId(resultSet.getInt("userId"));
                fetchedUser.setIsActive(resultSet.getInt("active"));

                users.add(fetchedUser);
            }
        } catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return users;
    }
 
    
    // Lookup user by username and password.
    @Override
    public User login(String username, String password) {
        User fetchedUser = new User();
        String getUserQuery = "SELECT * FROM user WHERE userName = ? AND password = ?"; 
        
        try{
            PreparedStatement statement = conn.prepareStatement(getUserQuery);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()){
                
                fetchedUser.setUsername(resultSet.getString("userName"));
                fetchedUser.setPassword(resultSet.getString("password"));
                fetchedUser.setUserId(resultSet.getInt("userId"));
                fetchedUser.setIsActive(resultSet.getInt("active"));
                
            } else {
                return null;
            }
        } catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return fetchedUser;
    }
    
    
    // Update a user password.
    @Override
    public void updateUser(User user) {
        String updateUserQuery = String.join(" ",
            "UPDATE user",
            "SET password = ?",
            "WHERE userId = ?"
        );
    
        try {
            PreparedStatement statement = conn.prepareStatement(updateUserQuery);
            
            statement.setString(1, user.getPassword());
            statement.setInt(2, user.getUserId());
            
            statement.executeUpdate();
            
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
