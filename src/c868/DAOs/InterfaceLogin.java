package c868.DAOs;

import c868.Models.User;
import javafx.collections.ObservableList;


public interface InterfaceLogin {    
    public ObservableList<User> getAllUsers();
    public User login(String username, String password);
    public void updateUser(User user);
}