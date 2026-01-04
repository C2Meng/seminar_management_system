package Controller;

public class Submission {

    // --- Attributes ---
    public String submissionId;
    public String studentName;
    public String title;
    public String type;
    public String evaluatorId;
    public String status;

    // Evaluation Data
    public int scoreClarity;
    public int scoreMethodology;
    public int scoreResults;
    public int scorePresentation;
    public String comment;

    // --- Constructor ---
    public Submission(String[] data) {
        this.submissionId = data[0];
        this.studentName = data[1];
        this.title = data[2];
        this.type = data[3];
        this.evaluatorId = data[4];

        //  if status exists in CSV, else default to Pending
        if (data.length > 5) {
            this.status = data[5];
        } else {
            this.status = "Pending";
        }

        this.comment = "N/A"; 
    }

    // --- Helper Methods ---
    public int getTotalScore() {
        return scoreClarity + scoreMethodology + scoreResults + scorePresentation;
    }
}