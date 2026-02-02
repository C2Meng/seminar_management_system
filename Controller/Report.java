package Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Report {
   
    private String reportTitle;
    private String reportContent;
    ArrayList<String[]> seminarList = loadCSV("Data/seminar.csv");
    ArrayList<String[]> submissionList = loadCSV("Data/Submission.csv");
    ArrayList<String[]> evaluationList = loadCSV("Data/Evaluations.csv");
    ArrayList<String[]> sessionList = loadCSV("Data/sessions.csv");
    private final String awardFilePath = "Data/awards.csv"; // Define your source path here

    public Report(String reportTitle , String reportContent){

        this.reportTitle = reportTitle;
        this.reportContent = reportContent;
      
    }


    private ArrayList<String[]> loadCSV(String path) {
        ArrayList<String[]> list = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line.split(","));
            }
        } catch (IOException e) {
            System.out.println("Could not read " + path);
        }
        return list;
    }

    private String getSeminarNameByID(int id) {
        return "Seminar #" + id; 
    }


    public void generateAwardReport() {
        ArrayList<String[]> awardList = loadCSV(awardFilePath);
        
        if (awardList.isEmpty()) {
            System.out.println("No data found to generate report.");
            return;
        }

        File outputFile = new File("Data/award_report.txt");
        outputFile.getParentFile().mkdirs(); 

        try (FileWriter writer = new FileWriter(outputFile, false)) {
            // Header
            writer.write("=====================================================\n");
            writer.write("                " + reportTitle.toUpperCase() + "                \n");
            writer.write("=====================================================\n\n");
            writer.write("Total Awards Given: " + awardList.size() + "\n\n");

            for (String[] data : awardList) {
                if (data.length >= 4) {
                    writer.write("-------------------------------------------------------\n");
                    int currentSeminarID = Integer.parseInt(data[0].trim());
                    // Note: Ensure getSeminarNameByID is defined in this class or accessible
                    String seminarName = getSeminarNameByID(currentSeminarID); 
                    
                    writer.write("Seminar Name    : " + seminarName + "\n");
                    writer.write("Session ID      : " + data[1].trim() + "\n");
                    writer.write("Receiver        : " + data[2].trim() + "\n");
                    writer.write("Award Name      : " + data[3].trim() + "\n");
                }
            }

            writer.write("\n=======================================================\n");
            writer.write("End of Award Report\n");
            System.out.println("Report successfully generated at: " + outputFile.getPath());

        } catch (IOException e) {
            System.err.println("Error writing report: " + e.getMessage());
        }
    }



    public void generateOverallReport() {
  

    try (FileWriter writer = new FileWriter("Data/overall_report.txt", false)) {
        writer.write("=============================================================\n");
        writer.write("                OFFICIAL SEMINAR SUMMARY                     \n");
        writer.write("=============================================================\n\n");

        writer.write("Total Seminars Conducted: " + seminarList.size()  + "\n"); // exclude header
        writer.write("Total Submissions Received: " + submissionList.size() + "\n"); // exclude header
        writer.write("Total Evaluations Recorded: " + evaluationList.size() + "\n"); // exclude header
        writer.write("Total Sessions Held: " + (sessionList.size() - 1) + "\n\n"); // exclude header
        writer.write("=============================================================\n\n");
        for (String[] seminar : seminarList) {
            // Skip header or empty rows
            if (seminar.length < 1 || seminar[0].equalsIgnoreCase("ID")) continue;
            String semID = seminar[0].trim();
            
            // 1. Calculate Total Submissions for this seminar
          

            writer.write("----------------------------------------------------------\n");
            writer.write("SEMINAR: " + seminar[1].trim().toUpperCase() + "\n");
            writer.write("Location: " + seminar[3].trim() + " | Date: " + seminar[4].trim() + "\n");
            writer.write("Time    : " + seminar[5].trim() + " - " + seminar[6].trim() + "\n");
            writer.write("----------------------------------------------------------\n");

            writer.write("\n[ SCHEDULED SESSIONS ]\n");
            
            for (String[] session : sessionList) {
                if (session.length > 0 && session[0].trim().equals(semID)) {
                    String sessionID = session[1].trim();
                    String presenterID = session[6].trim();

                    String paperTitle = "Pending Submission";
                    for (String[] sub : submissionList) {
                        if (sub.length > 2 && sub[1].trim().equals(semID) && sub[2].trim().equals(presenterID)) {
                            paperTitle = sub[3].trim();

                            
                            break;
                        }
                    }

                    writer.write(String.format("- Paper Title: %s\n", paperTitle));
                    writer.write("  Type : " + session[2].trim() + " | Presenter: " + session[5].trim() + "\n");

                    // Manual Scoring Logic (Replacing tryParse)
                    boolean hasEval = false;
                    for (String[] eval : evaluationList) {
                        if (eval.length > 5 && eval[0].trim().equals(semID) && eval[1].trim().equals(sessionID)) {
                            hasEval = true;
                            // Direct parsing - assuming data is numeric
                            int s1 = Integer.parseInt(eval[2].trim());
                            int s2 = Integer.parseInt(eval[3].trim());
                            int s3 = Integer.parseInt(eval[4].trim());
                            int s4 = Integer.parseInt(eval[5].trim());
                            
                            writer.write(String.format("  Score: %d/100 | Feedback: \"%s\"\n", (s1+s2+s3+s4), eval[6].trim()));
                            break;
                        }
                    }
                    if (!hasEval) writer.write("  Feedback: (No evaluation recorded)\n");
                }
            }

            writer.write("\n[ UNASSIGNED PAPERS ]\n");
            boolean foundUnassigned = false;
            for (String[] sub : submissionList) {
                if (sub.length > 1 && !sub[0].equalsIgnoreCase("SubmissionID") && sub[1].trim().equals(semID)) {
                    String subUserID = sub[2].trim();
                    boolean assigned = false;
                    for (String[] sess : sessionList) {
                        if (sess.length > 6 && sess[0].trim().equals(semID) && sess[6].trim().equals(subUserID)) {
                            assigned = true;
                            break;
                        }
                    }
                    if (!assigned) {
                        writer.write(String.format("! Title: %s (ID: %s)\n", sub[3].trim(), subUserID));
                        foundUnassigned = true;
                    }
                }
            }
            if (!foundUnassigned) writer.write("None.\n");
            writer.write("\n\n");
        }
    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
}



   


    
}
