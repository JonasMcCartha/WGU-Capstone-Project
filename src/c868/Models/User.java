package c868.Models;

public class User {
    public int isActive;
    public String password;
    public int userId;
    public String username;

    
    // Constructor
    public User() {
        seedUser();
    }
    
    
    /* Getters */
    public int getIsActive() {
        return this.isActive;
    }
    public String getPassword() {
        return this.password;
    }
    public String getUsername() {
        return this.username;
    }
    public int getUserId() {
        return this.userId;
    }
    
        
    /* Setters */
    public void setIsActive (int isActive) {
        this.isActive = isActive;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
        
    @Override
    public String toString() {
        return getUsername();
    }
       
        
    private void seedUser() {
        setUsername("");
        setPassword("");
    }
    
    
    // Validation
    public boolean validate() throws AssertionError {
        assert !this.username.equals("");
        assert !this.password.equals("");
        return true;
    }
}