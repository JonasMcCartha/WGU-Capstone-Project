package c868.Models;

import c868.Exceptions.StateException;

public class State {
    public int stateId;
    public String stateName;

    // Constructor
    public State() {
        seedState();
    }
    
    
    /* Getters */
    public int getStateId() {
        return this.stateId;
    }
    public String getStateName() {
        return this.stateName;
    }
    
    
    /* Setters */
    public void setStateId(int cityId) {
        this.stateId = cityId;
    }
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
       
    
    @Override
    public String toString() {
        return getStateName();
    }
     
    
    private void seedState() {
        setStateName("");
    }

    
    // Validation
    public boolean validate() throws StateException {
        if (this.stateName.equals("")) {
            throw new StateException("Please enter a state.");
        }
        return true;
    }
}