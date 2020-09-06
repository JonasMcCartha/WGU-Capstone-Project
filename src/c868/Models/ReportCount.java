package c868.Models;


public class ReportCount {
    private int count;
    private final String title;
    
    
    // Constructor
    public ReportCount(String title){
        this.title = title;
        this.count = 1;
    }

   
    public void autoIncrement() {
        this.count++;
    }
    
    public int getCount() {
        return this.count;
    }
    
    public String getTitle() {
        return this.title;
    }
}
