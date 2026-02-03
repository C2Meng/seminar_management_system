package Controller;

public class Submission {

private String submissionId;
    private int seminarId;
    private String userId; // Links to Session's PresenterID
    private String title;
    private String abstractText;
    private String attachment;
    private String supervisor;
    private String presentationType;
    
    // Grading fields (Not in CSV, but used for logic)
    private String status = "Pending"; 
    
    public Submission(String[] data) {
        // Safe parsing in case of missing columns
        this.submissionId = (data.length > 0) ? data[0].trim() : "";
        this.seminarId = (data.length > 1) ? Integer.parseInt(data[1].trim()) : 0;
        this.userId = (data.length > 2) ? data[2].trim() : "";
        this.title = (data.length > 3) ? data[3].trim() : "";
        this.abstractText = (data.length > 4) ? data[4].trim() : "";
        this.attachment = (data.length > 5) ? data[5].trim() : "";
        this.supervisor = (data.length > 6) ? data[6].trim() : "";
        this.presentationType = (data.length > 7) ? data[7].trim() : "";

        if (data.length > 8 && !data[8].trim().isEmpty()) {
            this.status = data[8].trim();
        } else {
            this.status = "Pending";
        }
    }

    public Submission(String submissionId, int seminarId, String userId, String title, 
                      String abstractText, String attachment, String supervisor, 
                      String presentationType, String status) {
        this.submissionId = submissionId;
        this.seminarId = seminarId;
        this.userId = userId;
        this.title = title;
        this.abstractText = abstractText;
        this.attachment = attachment;
        this.supervisor = supervisor;
        this.presentationType = presentationType;
        this.status = (status == null || status.isEmpty()) ? "Pending" : status;
    }

    
    public String getStatus() { return status; }
    public String getSubmissionId() { return submissionId; }
    public String getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getAbstractText() { return abstractText; }  
    public String getAttachment() { return attachment; }
    public String getSupervisor() { return supervisor; }
    public String getPresentationType() { return presentationType; }  
}