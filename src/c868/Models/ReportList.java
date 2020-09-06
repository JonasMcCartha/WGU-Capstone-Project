package c868.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReportList {
    private final ObservableList<ReportCount> items = FXCollections.observableArrayList();
    
    
    // Constructor
    public ReportList() {
    }
    
    
    public void add(ReportCount item) {
        this.items.add(item);
    }
        
    public ObservableList<ReportCount> list() {
        return this.items;
    }
    
    public int indexOf(String item) {
        int index = 0;
        for (ReportCount i : this.items) {
            if (i.getTitle().equals(item)) {
                return index;
            }
            index++;
        }
        return -1;
    }
    
    public ReportCount get(int index) {
        return this.items.get(index);
    }
        
    public int size() {
        return this.items.size();
    }
}
