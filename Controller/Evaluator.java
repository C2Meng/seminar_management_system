package Controller;

import InterfaceLib.Navigator;
import InterfaceLib.Role;
import InterfaceLib.SignIn;
import InterfaceLib.SignUp;
import Models.WriteToCSV;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Evaluator extends User implements SignUp, SignIn {

    String evaluationfilepath = "Data/Evaluations.csv";
    String submissionfilepath = "Data/Submission.csv";
    String sessionfilepath = "Data/Sessions.csv";

    private String email;
    private String password;
    private String name;
    private String userType;
    private WriteToCSV writeToCSV = new WriteToCSV();
    private boolean isRegistered = false;
    private String line;
    private Seminar seminarID;
    private Navigator navigator;
    private String seminarIDstr;
    private int sessionID;
    private int scoreClarity;
    private int scoreMethodology;
    private int scorePresentation;
    private String comment;

    public Evaluator(String email, String name, String password, Navigator navigator) {
        super(email, name, password, Role.EVALUATOR);
        this.navigator = navigator;
    }

    public Evaluator(String seminarIDstr, String sessionID, int scoreClarity, int scoreMethodology,
            int scorePresentation, String comment) {
        super("N/A", "N/A", "N/A", Role.EVALUATOR);
        this.seminarIDstr = seminarIDstr;
        this.sessionID = Integer.parseInt(sessionID);
        this.scoreClarity = scoreClarity;
        this.scoreMethodology = scoreMethodology;
        this.scorePresentation = scorePresentation;
        this.comment = comment;
    }

    public String getSeminarIDstr() {
        return this.seminarIDstr;
    }

    public int getSessionID() {
        return this.sessionID;
    }

    public int getScoreClarity() {
        return this.scoreClarity;
    }

    public int getScoreMethodology() {
        return this.scoreMethodology;
    }

    public int getScorePresentation() {
        return this.scorePresentation;
    }

    public String getComment() {
        return this.comment;
    }

    // pass in nulls for constructor in User class
    public Evaluator() {
        super(null, null, null, Role.EVALUATOR);
    }

    @Override
    public void registerUser(String email, String name, String password, String userType) {

        writeToCSV.registerUser(this.id, email, name, password, userType);
        isRegistered = true;
        navigator.goTo("LoginPage");

    }

    @Override
    public void authUser(String email, String password) {
        String[] isAuthenticated = writeToCSV.verifyUser(email, password, navigator);

        if (isAuthenticated != null) {
            System.out.println("User authenticated successfully");
        } else {
            System.out.println("Authentication failed. Invalid email or password.");
        }
    }

    // --------------Methods from Evaluator System------------------
    // public <return type> methodName (parameters) {
    // this method reads the CSV and return a list of students
    public List<Submission> loadSubmissions() {
        List<Submission> list = new ArrayList<>();
        File file = new File(submissionfilepath);

        if (!file.exists()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write("SubmissionID,StudentName,Title,Abstract,FilePath,Status");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                list.add(new Submission(values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void saveGrade(String seminar, Session s, int clarity, int method, int result, int pres, String comment) {
        // Logic to append/update Evaluations.csv
        List<String> lines = new ArrayList<>();
        File file = new File(evaluationfilepath);
        String sID = String.valueOf(s.getSessionID());

        // Read existing file if it exists, otherwise start with header
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.startsWith(seminar + "," + sID + ",")) {
                        lines.add(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            lines.add("SeminarID,SessionID,ScoreClarity,ScoreMethodology,ScoreResults,ScorePresentation,Comment");
        }

        // Add the new grade
        lines.add(seminar + "," + sID + "," + clarity + "," + method + "," + result + "," + pres + "," + comment);

        // Write back
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateSubmissionStatus(s.getSubmission().getSubmissionId(), "Graded");
        System.out.println("Grade saved for Session ID: " + sID);
    }

    // how to read and write in java:
    // https://www.w3schools.com/java/java_bufferedreader.asp

    private void updateSubmissionStatus(String targetSubmissionId, String newStatus) {
        List<String> lines = new ArrayList<>();
        File file = new File(submissionfilepath);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            // Read header
            String header = br.readLine();
            if (header != null)
                lines.add(header);

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                // Check if this is the row to update (Match SubmissionID at Index 0)
                if (parts.length > 0 && parts[0].trim().equals(targetSubmissionId)) {
                    // Preserve first 8 columns, update status at index 8
                    parts = java.util.Arrays.copyOf(parts, 9);
                    parts[8] = newStatus;
                    lines.add(String.join(",", parts));
                } else {
                    // Keep original line
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write back to file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Session> loadSessions(String myEvaluatorID) {
        List<Session> sessionList = new ArrayList<>();

        // 1. Load Submissions into a Map for quick lookup (Key: UserID)
        Map<String, Submission> submissionMap = loadSubmissionsMap();

        File file = new File(sessionfilepath);
        if (!file.exists())
            return sessionList;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip Header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 9)
                    continue;

                String csvEvaluatorID = data[8].trim();

                // Only load sessions for THIS evaluator
                if (csvEvaluatorID.equals(myEvaluatorID)) {
                    int semID = Integer.parseInt(data[0].trim());
                    int sID = Integer.parseInt(data[1].trim());
                    Seminar seminarStub = new Seminar(semID, "Seminar " + semID);
                    Session session = new Session(seminarStub, sID, data[2].trim(), data[3].trim(), data[4].trim());
                    session.setPresenter(data[5].trim());
                    String pID = data[6].trim();
                    session.setPresenterID(pID);
                    session.setEvaluatorID(csvEvaluatorID);

                    // LINKAGE: Find the submission matching this Student/Presenter ID
                    if (submissionMap.containsKey(pID)) {
                        session.setSubmission(submissionMap.get(pID));
                    }

                    sessionList.add(session);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sessionList;
    }

    // Helper to get a list of IDs that are already in Evaluations.csv
    public Set<String> loadGradedSessionIDs() {
        Set<String> gradedIds = new HashSet<>();
        File file = new File(evaluationfilepath);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > 0 && !parts[0].equals("SessionID")) {
                        gradedIds.add(parts[0].trim());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return gradedIds;
    }

    private Map<String, Submission> loadSubmissionsMap() {
        Map<String, Submission> map = new HashMap<>();
        File file = new File(submissionfilepath);
        if (!file.exists())
            return map;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip Header
            // Columns: SubmissionID, SeminarID, UserID, Title, Abstract, Attachment,
            // Supervisor, Type
            while ((line = br.readLine()) != null) {
                // Use a regex split to handle potential commas in Titles/Abstracts if quoted,
                // but for simple CSV:
                String[] values = line.split(",");
                if (values.length >= 3) {
                    Submission s = new Submission(values);
                    // Map Key = User ID (Index 2)
                    map.put(s.getUserId(), s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
    // ------------------End of Evaluator System Methods------------------

    @Override
    public void logOut() {
        // implementation for logging out a user //
    }

    @Override
    public void deleteUser(String username, String userType) {
        // Implementation for deleting a user //
    }
}