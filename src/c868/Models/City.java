package c868.Models;

import c868.Exceptions.CityException;
import c868.Exceptions.StateException;


public class City {
    public int cityId;
    public String cityName;
    public State state;

    // Constructor
    public City() {
        seedCity();
    }
    
    
    /* Getters */
    public int getCityId() {
        return this.cityId;
    }
    public String getCityName() {
        return this.cityName;
    }
    public State getState() {
        return this.state;
    }
    
    
    /* Setters */
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public void setState(State state) {
        this.state = state;
    }
        
    
    private void seedCity() {
        setCityName("");
    }
        
    
    // Validation  
    public boolean validate() throws CityException {
        if (this.cityName.equals("")) {
            throw new CityException("Please enter a city name.");
        }
        try {
            this.state.validate();
        }
        catch (StateException e) {
            throw new CityException(e.getMessage());
        }
        return true;
    } 
}