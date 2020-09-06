package c868.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import c868.Main.MainApp;
import c868.Models.State;


public class SQLState implements InterfaceState {
    private final static Connection conn = MainApp.conn;
    
    
    // Constructor
    public SQLState() {
    }

    SQLState(Connection conn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
 
    
    // Retrieve highest primary key value from table.
    private int getMaxId() {
        int maxID = 0;
        String maxIdQuery = "SELECT MAX(stateId) FROM state";
        
        try {
            Statement statement = conn.createStatement(); 
            ResultSet result = statement.executeQuery(maxIdQuery);
            
            if(result.next()) {
                maxID = result.getInt(1);
            }
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return maxID + 1;
    }

    
    // Add new state.
    @Override
    public int addState(State state) {
        int stateId = getMaxId();
        String addStateQuery = String.join(" ",
            "INSERT INTO state (stateId, state, createDate, createdBy, lastUpdate, lastUpdateBy)",
            "VALUES (?, ?, NOW(), ?, NOW(), ?)"
        );
            
        try {
            PreparedStatement statement = conn.prepareStatement(addStateQuery);
            
            statement.setInt(1, stateId);
            statement.setString(2, state.getStateName());
            statement.setString(3, MainApp.user.username);
            statement.setString(4, MainApp.user.username);
            statement.executeUpdate();
            
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return stateId;
    }
  
    
    // Lookup state by ID.
    @Override
    public State getState(int stateId) {
        State fetchedState = new State();
        String getStateQuery = "SELECT * FROM state WHERE stateId = ?"; 
        
        try{
            PreparedStatement statement = conn.prepareStatement(getStateQuery);
            statement.setInt(1, stateId);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()){
                fetchedState.setStateName(resultSet.getString("state"));
                fetchedState.setStateId(resultSet.getInt("stateId"));
            } else {
                return null;
            }
        } catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        
        return fetchedState;
    }
    
    
    // Update a state.
    @Override
    public void updateState(State state) {
        String updateStateQuery = String.join(" ",
            "UPDATE state SET state=?, lastUpdate=NOW(), lastUpdateBy=?",
            "WHERE stateId = ?"
        );
        
        try {
            PreparedStatement statement = conn.prepareStatement(updateStateQuery);
            
            statement.setString(1, state.getStateName());
            statement.setString(2, MainApp.user.getUsername());
            statement.setInt(3, state.getStateId());
            statement.executeUpdate();
            
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
