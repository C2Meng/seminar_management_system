package Controller;

public class Session {
    
    private Seminar seminar;
    
    private int sessionID;
    private String startTime;
    private String endTime;
    private String sessionType;
    private String evalName;
    private String stuName;
    private Award assignedAward;
    private String presenterID;
    private String evaluatorID;

    private Submission submission;


    // sessopm constructor
    public Session(Seminar sem ){
        this.seminar = sem;
       
    }
    
    public Session(Seminar sem, int sID,String sType, String sTime, String eTime) {
        this.seminar = sem;
        this.sessionID = sID;
        this.sessionType = sType;
        this.startTime = sTime;
        this.endTime = eTime;

    };

    public int getSessionID() {
        return this.sessionID;
    }

    public Seminar getSeminar() {
        return seminar;
    }

    //gets the id of the super class (in this case seminarID)
    public int getSeminarID(){
        return this.seminar.getSeminarID();
    }
    
    

    public void setSessionType(String sType) {
        this.sessionType = sType;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSeminar(Seminar sem) {
        this.seminar = sem;
    }

     public void setStartTime(String start) {
        this.startTime = start;
    }

     public void setEndTime(String end) {
        this.endTime = end;
    };

    public void setEvaluator(String eval){
        this.evalName = eval;
    };

    public void setPresenter(String stu){
        this.stuName = stu;
    };

     public String getStartTime() {
        return this.startTime;
    };

     public String getEndTime() {
        return this.endTime;
    };

    
    // Getters for the lists
    public String getPresenter() {
        return stuName;
    }

    public String getEvaluator() {
        return evalName;
    }

   public void setAward(Award award) {
        this.assignedAward = award;
    }

    public Award getAward(){
        return assignedAward;
    }
   


    public void setPresenterID(String id) {
        this.presenterID = id;
    }

    public String getPresenterID() {
        return presenterID;
    }

    public void setEvaluatorID(String id) {
        this.evaluatorID = id;
    }

    public String getEvaluatorID() {
        return evaluatorID;
    }
    public void setSubmission(Submission s) { this.submission = s; }
    public Submission getSubmission() { return submission; }

    public String getProjectTitle() {
        return (submission != null) ? submission.getTitle() : "No Submission Found";
    }
}
