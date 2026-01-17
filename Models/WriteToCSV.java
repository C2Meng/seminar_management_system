package Models;

import Controller.Seminar;
import Controller.Session;
import InterfaceLib.Navigator;
import MainFrame.MainFrame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class WriteToCSV {

    // =========================== FILE PATHS =========================== //
    private String filePath = "Data/database.csv"; // Stores Users
    private String seminarFilePath = "Data/seminar.csv"; // Stores Seminars
    private String sessionFilePath = "Data/sessions.csv"; // Stores Sessions


    // =========================== USER MANAGEMENT (EXISTING CODE)
    // =========================== //

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    // Creates the USER database file
    public void createFile() {
        try {
            File file = new File(filePath);

            // create folder if missing
            file.getParentFile().mkdirs();

            // write header only if file does NOT exist
            if (!file.exists()) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.append("id,email,name,password,role\n"); // header
                    System.out.println("User CSV file created with headers.");
                }
            } else {
                System.out.println("User CSV file already exists, skipping header creation.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Writes a new USER to the database
    public void writeData(String line) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.append(line + "\n");
            System.out.println("User data written successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Verifies user credentials for Login
    public String verifyUser(String email, String password, Navigator navigator) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            br.readLine(); // Skip header
            while ((currentLine = br.readLine()) != null) {
                String[] data = currentLine.split(",");
                // Ensure the line has enough data to prevent crashes on empty lines
                if (data.length < 4)
                    continue;

                String storedEmail = data[0];
                // Index 1 is usually name, Index 2 is password based on your registerUser logic
                String storedPassword = data[2];
                String storedRole = data[3];

                if (storedEmail.equals(email) && storedPassword.equals(password)) {
                    return storedRole;
                }
            }
            // If loop finishes without return, credentials are invalid
            System.out.println("Invalid email or password.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // =========================== SEMINAR MANAGEMENT =========================== //

    // 1. Write a single Seminar to CSV (Append mode)
    public void writeSeminar(Seminar seminar) {
        File file = new File(seminarFilePath);

        // Ensure folder exists
        file.getParentFile().mkdirs();

        boolean fileExists = file.exists();

        try (FileWriter writer = new FileWriter(seminarFilePath, true)) {
            // Write header if file is new
            if (!fileExists) {
                writer.append("ID,Title,Description,Venue,Date,StartTime,EndTime\n");
            }

            // Format: ID,Title,Description,Presenter,SessionType
            String line = seminar.getSeminarID() + "," +
                    seminar.getTitle() + "," +
                    seminar.getDescription() + "," +
                    seminar.getVenue() + "," +
                    seminar.getDate() + "," +
                    seminar.getStartTime() + "," +
                    seminar.getEndTime();

            writer.append(line + "\n");
            System.out.println("Seminar written to CSV: " + seminar.getTitle());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 2. Read all Seminars from CSV into an ArrayList
    public ArrayList<Seminar> readSeminars() {
        ArrayList<Seminar> list = new ArrayList<>();
        File file = new File(seminarFilePath);

        if (!file.exists())
            return list; // Return empty list if no file exists

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                // Basic validation: must have at least ID and Title
                if (data.length >= 2) {
                    try {
                        int id = Integer.parseInt(data[0]);
                        String title = data[1];

                        Seminar s = new Seminar(id, title);

                        // Fill optional fields if they exist in CSV
                        // Check bounds to avoid ArrayIndexOutOfBoundsException
                        if (data.length > 2)
                            s.setDescription(data[2]);
                        if (data.length > 3)
                            s.setVenue(data[3]);
                        if (data.length > 4)
                            s.setDate(data[4]);
                        if (data.length > 5)
                            s.setStartTime(data[5]);
                        if (data.length > 6)
                            s.setEndTime(data[6]);

                        list.add(s);
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid seminar ID: " + data[0]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Update/Overwrite Seminar CSV (Used for Delete or Edit operations)
    public void updateSeminarCSV(ArrayList<Seminar> seminars) {
        // false = overwrite mode (clears file content)
        try (FileWriter writer = new FileWriter(seminarFilePath, false)) {
            writer.append("ID,Title,Description,Venue,Date,StartTime,EndTime\n"); // Re-write Header

            for (Seminar s : seminars) {
                String line = s.getSeminarID() + "," +
                        s.getTitle() + "," +
                        s.getDescription() + "," +
                        s.getVenue() + "," +
                        s.getDate() + "," +
                        s.getStartTime() + "," +
                        s.getEndTime();
                writer.append(line + "\n");
            }
            System.out.println("Seminar CSV updated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Creates the session database file// read if already exists
    public void createSession(Session session) {
        try {
            File file = new File(sessionFilePath);

            // create folder if missing
            file.getParentFile().mkdirs();

            // write header only if file does NOT exist
            if (!file.exists()) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.append("seminarID,sessionID,sessionType,startTime,endTime,presenter,evaluator\n"); // header
                    System.out.println("Sessions CSV file created with headers.");
                }
            } else {
                System.out.println("Sessions CSV file already exists, skipping header creation.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // read sessions

    public ArrayList<Session> readSessions(Session session) {
        ArrayList<Session> sessions = new ArrayList<>();
        // Assuming file is at "Data/session.csv"
        File file = new File(sessionFilePath);

        if (!file.exists())
            return sessions;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                // Format: SeminarID, SessionID, Type, Start, End, Presenter, Evaluator

                if (data.length >= 2) {
                    int linkedID = Integer.parseInt(data[0]); // compare given seminar id with the session's seminar id

                    if (linkedID == session.getSeminarID()) {
                        // Create a dummy seminar object just to satisfy the constructor
                        Seminar sessionSem = new Seminar(session.getSeminarID(), "");

                        // typecast data[1] from Str to an int
                        String tempStr = data[1];
                        int tempInt = Integer.parseInt(tempStr);

                        Session s = new Session(sessionSem, tempInt, data[2], data[3], data[4]);
                        // if(data.length > 5) session.addPresenter(data[5]);
                        // if(data.length > 6) session.addEvaluator(data[6]);

                        // If you saved presenters/evaluators in columns 5 and 6:
                        // You can load them here if needed, or leave them for now.
                        sessions.add(s);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sessions;
    }

    // --- SESSION: Write (Append) ---
    public void writeSession(Session session, int seminarID) {
        try (FileWriter writer = new FileWriter(sessionFilePath, true)) {
            File f = new File(sessionFilePath);
            if (f.length() == 0)
                writer.append("seminarID,sessionID,sessionType,startTime,endTime,presenter,evaluator\n");

            String line = seminarID + "," +
                    session.getSessionID() + "," +
                    session.getSessionType() + "," +
                    session.getStartTime() + "," +
                    session.getEndTime();
            // presenter + "," +
            // evaluator;

            writer.append(line + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- SESSION: Delete (Rewrite Method) ---
    public void deleteSession(int sessionIDToDelete) {
        ArrayList<String> allLines = new ArrayList<>();
        File file = new File(sessionFilePath);

        // 1. Read ALL lines into memory
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                allLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. Rewrite file, skipping the deleted line
        try (FileWriter writer = new FileWriter(file, false)) { // false = Overwrite mode
            for (String line : allLines) {
                String[] data = line.split(",");

          

                // If it's the header, write it back and skip parsing
                if (data[1].equalsIgnoreCase("SessionID")) {
                    writer.append(line + "\n");
                    continue; // skip to next line
                }

                //  Parse the ID safely
                int tempInt = Integer.parseInt(data[1].trim());
               
                 

                // 3. Write lines whose ID does NOT match the one to delete
                if (tempInt != sessionIDToDelete) {
                    writer.append(line + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void generateSeminarReport(){

        
        ArrayList<String> seminarList = new ArrayList<>(); // to store seminar data
        File file = new File("Data/seminar.csv"); // 
        file.getParentFile().mkdirs(); // ensure folder exists


        try (BufferedReader br = new BufferedReader(new FileReader(file))){ // read seminar data

            String line; // to hold each line
            while((line = br.readLine())!= null){
                seminarList.add(line); // add line to seminar list
            }
        } catch (IOException e){
            e.printStackTrace();
        } 


        try (FileWriter writer = new FileWriter("Data/seminar_report.txt"  , false)){ // write report to text file

            writer.append("==================  Seminar Report ==================\n\n");

            int totalSeminars = seminarList.size() - 1; // exclude header
            writer.write("Total Seminars: " + totalSeminars + "\n\n");
            writer.write("-----------------------------------------------------\n");



            // write each seminar's details
            for (String seminar : seminarList.subList(1, seminarList.size())){ // skip header from csv file
                String data[] = seminar.split(",");

            writer.write("Seminar ID   : " + data[0] + "\n");
            writer.write("Title        : " + data[1] + "\n");
            writer.write("Description  : " + data[2] + "\n");
            writer.write("Venue        : " + data[3] + "\n");
            writer.write("Date         : " + data[4] + "\n");
            writer.write("Start Time   : " + data[5] + "\n");
            writer.write("End Time     : " + data[6] + "\n");
            writer.write("-----------------------------------------------------\n");
            }

        System.out.println("Report generated successfully."); // confirmation message

            
        } catch (IOException e){
            e.printStackTrace();
        }

    }


    public void saveSeminarReportToFile(File destination , MainFrame mainFrame) {
            try {
            Files.copy(
                    Paths.get("Data/seminar_report.txt"), // getting the generated report
                    destination.toPath(), // destination chosen by user
                    StandardCopyOption.REPLACE_EXISTING // overwrite if file exists
            );

            JOptionPane.showMessageDialog(
                    mainFrame,
                    "File saved successfully!",
                    "Saved",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "Failed to save file.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

}