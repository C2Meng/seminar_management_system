package Controller;

public enum Award {
    BEST_ORAL("Best Oral Presentation"),
    BEST_POSTER("Best Poster"),
    PEOPLES_CHOICE("People's Choice");

    // Encapsulation: Private fields
    private final String awardName;
    private String presenterName;
    private String seminarID;
    private String sessionID;



    public String getPresenterName() {
        return presenterName;
    }

    public void setPresenterName(String presenterName) {
        this.presenterName = presenterName;
    }

    public String getSeminarID() {
        return seminarID;
    }

    public void setSeminarID(String seminarID) {
        this.seminarID = seminarID;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    private String getAwardName(){
        return awardName;
    }

    private String setAwardName(String awardName){
        return awardName;
    }


    Award(String awardName, String presenterName, String seminarID, String sessionID) {
        this.awardName = awardName;
        this.presenterName = presenterName;
        this.seminarID = seminarID;
        this.sessionID = sessionID;
    }

    // Constructor (runs once for each predefined constant)
    Award(String awardName) {
        this.awardName = awardName;
       
    }

    public String getDisplayName() { return awardName; }


}
