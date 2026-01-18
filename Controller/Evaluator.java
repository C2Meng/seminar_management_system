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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class Evaluator extends User implements SignUp , SignIn{

    String submissionfilepath = "Data/examplesubmissiondata.csv";
    String evaluationfilepath = "Data/Evaluations.csv";


    private String email;
    private String password;
    private String name;
    private String userType;
    private WriteToCSV writeToCSV = new WriteToCSV();
    private boolean isRegistered = false;
    private String line;
    private Navigator navigator;
    
    public Evaluator (String email, String name , String password , Navigator navigator ){
        super(email , name , password , Role.EVALUATOR);
        this.navigator = navigator;
    }
    //pass in nulls for constructor in User class
    public Evaluator (){
        super(null,null,null, Role.EVALUATOR);
    }


    @Override
    public void registerUser(String email , String name , String password , String userType){
         
      
         writeToCSV.getFilePath();
         line = email + "," + name + "," + password + "," + userType;
         isRegistered = true;
         writeToCSV.writeData(line);
         navigator.goTo("LoginPage");

    }

    @Override
    public void authUser(String email , String password ){
       writeToCSV.getFilePath();
       String isAuthenticated = writeToCSV.verifyUser(email, password , navigator);
      
       if (isAuthenticated != null){
           System.out.println("User authenticated successfully");
       } else {
           System.out.println("Authentication failed. Invalid email or password.");
       }
    }

    //--------------Methods from Evaluator System------------------
    // public <return type> methodName (parameters) {
    // this method reads the CSV and return a list of students
    public List<Submission> loadSubmissions() {
        List<Submission> list = new ArrayList<>();
        File file = new File(submissionfilepath);

        if (!file.exists()){
            try ( BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
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


    public void saveGrade(Submission s) {
        s.status = "Graded";

        writeEvaluationToCSV(s);

        updateSubmissionStatusInCSV(s.submissionId, "Graded");
    }



    // how to read and write in java: https://www.w3schools.com/java/java_bufferedreader.asp
private void writeEvaluationToCSV(Submission s) {

        // Logic to append/update Evaluations.csv
        // (Copied from your original saveEvaluationToCSV method)
        List<String> lines = new ArrayList<>();
        File file = new File(evaluationfilepath);


        if (!file.exists()){
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))){;
                bw.write("SubmissionID,ScoreClarity,ScoreMethodology,ScoreResults,ScorePresentation,Comment");
            
            } catch (Exception e) {
               e.printStackTrace();
            }


        }


        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {

                while ((line = br.readLine()) != null) {
                    if (!line.startsWith(s.submissionId + ",")) {
                        lines.add(line);
                    }
                }
            } catch (IOException e) { e.printStackTrace(); }
        } 

        

        lines.add(s.submissionId + "," + s.scoreClarity + "," + s.scoreMethodology + "," + 
                  s.scoreResults + "," + s.scorePresentation + "," + s.comment);

        //lines[] array will contain = { 1, 10, 9, 8, 7, "Good job" } for example
        //then we write all lines back to the file
       
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

            
            for (String l : lines) {
                bw.write(String.valueOf(l));
                bw.newLine(); // Explicitly adds the new line character
            }
        } catch (IOException e) { 
            e.printStackTrace(); 
        }

        // try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
        //     for (String l : lines) pw.println(l);
        // } catch (IOException e) { e.printStackTrace(); }
    }

    private void updateSubmissionStatusInCSV(String subId, String newStatus) {
        // Logic to update Submissions.csv
        // (Copied from your original updateSubmissionStatusInCSV method)
        List<String> lines = new ArrayList<>();
        File file = new File(submissionfilepath);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(subId)) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 5; i++) {
                        if (i < parts.length) sb.append(parts[i]).append(",");
                        else sb.append("N/A,");
                    }
                    sb.append(newStatus);
                    lines.add(sb.toString());
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) { e.printStackTrace(); }

        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            for (String l : lines) pw.println(l);
        } catch (IOException e) { e.printStackTrace(); }
    }

//------------------End of Evaluator System Methods------------------







    @Override
    public void logOut(){
       // implementation for logging out a user //
    }

    @Override
    public void deleteUser(String username , String userType){
          // Implementation for deleting a user //
    }
}

