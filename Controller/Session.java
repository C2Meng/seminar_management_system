package Controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.DefaultTableModel;


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
    private String submissionFilePath;

    private Submission submission;

    // sessopm constructor
    public Session(Seminar sem) {
        this.seminar = sem;

    }

    public Session() {}

    public Session(Seminar sem, int sID, String sType, String sTime, String eTime) {
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

    // gets the id of the super class (in this case seminarID)
    public int getSeminarID() {
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

    public void setEvaluator(String eval) {
        this.evalName = eval;
    };

    public void setPresenter(String stu) {
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

    public Award getAward() {
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


    public void setSubmissionFilePath(Submission s) {
        this.submission = s;
    }

    public String getSubmissionFilePath() {
        return submissionFilePath;
    }

    public String getEvaluatorID() {
        return evaluatorID;
    }

    public void setSubmission(Submission s) {
        this.submission = s;
    }

    public Submission getSubmission() {
        return submission;
    }

    public String getProjectTitle() {
        return (submission != null) ? submission.getTitle() : "Awaiting Submission";
    }



   public void viewSession(DefaultTableModel model) {
    // Map to store: seminarID -> "SeminarTitle|Venue"
    Map<String, String> seminarDataMap = new HashMap<>();

    try {
        // 1. Load Seminar Names and Venues from seminar.csv
        BufferedReader brSem = new BufferedReader(new FileReader("Data/seminar.csv"));
        String line;
        brSem.readLine(); // Skip header
        while ((line = brSem.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 4) {
                String id = parts[0];
                String title = parts[1];
                String venue = parts[3]; // Venue is the 4th column (index 3)
                seminarDataMap.put(id, title + "|" + venue);
            }
        }
        brSem.close();

        // 2. Load Sessions and combine with the Seminar Data
        BufferedReader brSes = new BufferedReader(new FileReader("Data/sessions.csv"));
        brSes.readLine(); // Skip header
        while ((line = brSes.readLine()) != null) {
            String[] parts = line.split(",");
            // sessions.csv indices: 0:semID, 1:sesID, 2:type, 3:start, 4:end, 5:presName
            if (parts.length >= 6) {
                String semID = parts[0];
                String seminarInfo = seminarDataMap.getOrDefault(semID, "Unknown Seminar|Unknown Venue");
                
                // Split the mapped string back into Title and Venue
                String[] info = seminarInfo.split("\\|");
                String semName = info[0];
                String venue = info[1];
                
                String type = parts[2];
                String start = parts[3];
                String end = parts[4];
                String presenter = parts[5];

                // Add to table model in the order matching your ColumnNames
                model.addRow(new Object[]{semName, venue, type, start, end, presenter});
            }
        }
        brSes.close();
    } catch (Exception e) {
        System.err.println("Error loading schedule: " + e.getMessage());
        e.printStackTrace();
    }
}
}
