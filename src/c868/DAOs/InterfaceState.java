package c868.DAOs;

import c868.Models.State;


public interface InterfaceState {
    public int addState(State state);
    public State getState(int stateId);
    public void updateState(State state);
}
