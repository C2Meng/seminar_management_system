package Controller;

public enum Award {
    BEST_ORAL("Best Oral Presentation"),
    BEST_POSTER("Best Poster"),
    PEOPLES_CHOICE("People's Choice");

    // Encapsulation: Private fields
    private final String awardName;
   

    // Constructor (runs once for each predefined constant)
    Award(String awardName) {
        this.awardName = awardName;
       
    }
    public String getDisplayName() { return awardName; }
    
}
