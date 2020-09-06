package c868.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import c868.Main.MainApp;
import c868.Models.City;


public class SQLCity implements InterfaceCity {
    private final static Connection conn = MainApp.conn;
       
    
    // Constructor
    public SQLCity() {
    }
    
    
    // Retrieve highest primary key value from table.
    private int getMaxId() {
        int maxID = 0;
        String maxIdQuery = "SELECT MAX(cityId) FROM city";
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

    
    // Add a new city.
    @Override
    public int addCity(City city) {
        String addCityQuery = String.join(" ",
            "INSERT INTO city (cityId, city, stateId, createDate, createdBy, lastUpdate, lastUpdateBy)",
            "VALUES (?, ?, ?, NOW(), ?, NOW(), ?)"
        );
    
        int cityId = getMaxId();
        try {
            PreparedStatement statement = conn.prepareStatement(addCityQuery);
            
            statement.setInt(1, cityId);
            statement.setString(2, city.getCityName());
            statement.setInt(3, city.getState().getStateId());
            statement.setString(4, MainApp.user.username);
            statement.setString(5, MainApp.user.username);
            statement.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return cityId;
    }
 
    
    // Lookup city by ID.
    @Override
    public City getCity(int cityId) {
        String getCityQuery = "SELECT * FROM city WHERE cityId = ?"; 
        City fetchedCity = new City();
        
        try{
            PreparedStatement statement = conn.prepareStatement(getCityQuery);
            statement.setInt(1, cityId);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()){
                InterfaceState state = new SQLState();
                
                fetchedCity.setCityName(resultSet.getString("city"));
                fetchedCity.setCityId(resultSet.getInt("cityId"));
                fetchedCity.setState(state.getState(resultSet.getInt("stateId")));
                
            } else {
                return null;
            }
        } catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return fetchedCity;
    }
    
    
    // Update a city.
    @Override
    public void updateCity(City city) {
        String updateCityQuery = String.join(" ",
            "UPDATE city SET city=?, stateId=?, lastUpdate=NOW(), lastUpdateBy=?",
            "WHERE cityId = ?"
        );
    
        try {
            PreparedStatement statement = conn.prepareStatement(updateCityQuery);
            statement.setString(1, city.getCityName());
            statement.setInt(2, city.getState().getStateId());
            statement.setString(3, MainApp.user.username);
            statement.setInt(4, city.getCityId());
            statement.executeUpdate();
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
