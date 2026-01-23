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
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

public class WriteToCSV {

    // =========================== FILE PATHS =========================== //
    private String userFilePath = "Data/User.csv"; // Stores Users with user_id
    private String seminarFilePath = "Data/seminar.csv"; // Stores Seminars
    private String sessionFilePath = "Data/sessions.csv"; // Stores Sessions


    // =========================== USER MANAGEMENT (EXISTING CODE)
    // =========================== //



    public String getUserFilePath() {
        return userFilePath;
    }

    public void setUserFilePath(String userFilePath) {
        this.userFilePath = userFilePath;
    }

    // Creates the USER.csv database file
    public void createUserFile() {
        try {
            File file = new File(userFilePath);

            // create folder if missing
            file.getParentFile().mkdirs();

            // write header only if file does NOT exist
            if (!file.exists()) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.append("user_id,email,name,password,role\n"); // header
                    System.out.println("User.csv file created with headers.");
                }
            } else {
                System.out.println("User.csv file already exists, skipping header creation.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Register user with user_id
    public void registerUser(String userId, String email, String name, String password, String role) {
        try {
            // Ensure User.csv exists
            createUserFile();

            File file = new File(userFilePath);
            String line = userId + "," + email + "," + name + "," + password + "," + role;

            try (FileWriter writer = new FileWriter(file, true)) {
                writer.append(line + "\n");
                System.out.println("User registered successfully with ID: " + userId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


public ArrayList<String> getAssignedStudentIDs(String currentEvaluatorID) {
        ArrayList<String> assignedStudentIDs = new ArrayList<>();
        File file = new File(sessionFilePath);

        if (!file.exists()) return assignedStudentIDs;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                // Split by comma, keep empty strings
                String[] data = line.split(",", -1);

                // CSV Structure based on your provided file:
                // 0:seminarID, 1:sessionID, 2:Type, 3:Start, 4:End, 
                // 5:PresenterName, 6:PresenterID, 7:EvaluatorName, 8:EvaluatorID
                
                if (data.length >= 9) {
                    String rowEvaluatorID = data[8].trim();
                    String rowPresenterID = data[6].trim();

                    // If the logged-in evaluator matches the one in this session row
                    if (rowEvaluatorID.equals(currentEvaluatorID)) {
                        assignedStudentIDs.add(rowPresenterID);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return assignedStudentIDs;
    }
  

    // filter database for evaluators and students, adding the names to respective
    // arraylist
  public Map<String, ArrayList<String>> readData() {
    Map<String, ArrayList<String>> map = new HashMap<>();
    
    // Initialize lists
    ArrayList<String> evaluatorNames = new ArrayList<>();
    ArrayList<String> evaluatorIDs   = new ArrayList<>();
    ArrayList<String> studentNames   = new ArrayList<>();
    ArrayList<String> studentIDs     = new ArrayList<>();

    File file = new File(userFilePath); // e.g., "Data/user.csv"

    if (file.exists()) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                // Using -1 to keep empty trailing fields if any
                String[] data = line.split(",", -1); 
                
                // Safety check: ensure we have enough columns
                if (data.length >= 5) {
                    String id   = data[0].trim(); // user_id is at index 0
                    String name = data[2].trim(); // name is at index 2
                    String role = data[4].trim(); // role is at index 4

                    if (role.equalsIgnoreCase("Evaluator")) {
                        evaluatorNames.add(name);
                        evaluatorIDs.add(id);
                    } else if (role.equalsIgnoreCase("Student")) {
                        studentNames.add(name);
                        studentIDs.add(id);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Store in map with the EXACT keys your button listener expects
    map.put("evaluatorNameList", evaluatorNames);
    map.put("evaluatorIDList", evaluatorIDs); 
    map.put("studentNameList", studentNames);
    map.put("studentIDList", studentIDs);
    
    return map;
}

    //get user information via userID
    public ArrayList<String> readUser(int userID) {
        return readUserInternal(String.valueOf(userID), true);
    }

    public ArrayList<String> readUser(String name) {
        return readUserInternal(name, false);
    }
    private ArrayList<String> readUserInternal(String value, boolean searchByID) {

    ArrayList<String> userInfo = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(userFilePath))) {

        String line;

        // Skip header
        reader.readLine();

        while ((line = reader.readLine()) != null) {

            String[] data = line.split(",");
            if (data.length < 5) {
                continue;
            }
            boolean match;
            if (searchByID) {
                match = data[0].equals(value); // id
            } else {
                match = data[2].equalsIgnoreCase(value); // name
            }
            if (match) {
                // [0]=id, [1]=email, [2]=name, [3]=role
                userInfo.add(data[0]);
                userInfo.add(data[1]);
                userInfo.add(data[2]);
                userInfo.add(data[4]);
                break;
            }
        }
        if (userInfo.isEmpty()) {
            System.out.println("Error: User not found (" +
                    (searchByID ? "id=" : "name=") + value + ")");
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return userInfo;
}
    //arrayList<String> userinfo = new csvModel.readUser; userid = userinfo.get(0), etc

    // Verifies user credentials for Login
    public String verifyUser(String email, String password, Navigator navigator) {
        try (BufferedReader br = new BufferedReader(new FileReader(userFilePath))) {
            String currentLine;
            br.readLine(); // Skip header
            while ((currentLine = br.readLine()) != null) {
                String[] data = currentLine.split(",");
                // Ensure the line has enough data to prevent crashes on empty lines
                if (data.length < 5)
                    continue;

                // Format: user_id,email,name,password,role
                String storedEmail = data[1].trim();
                String storedPassword = data[3].trim();
                String storedRole = data[4].trim();

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
    File file = new File(sessionFilePath);

    if (!file.exists()) return sessions;

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        br.readLine(); // Skip header
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",", -1); 

            if (data.length >= 2) {
                try {
                    int linkedID = Integer.parseInt(data[0]); 

                    if (linkedID == session.getSeminarID()) {
                        Seminar sessionSem = new Seminar(session.getSeminarID(), "");
                        int sessionID = Integer.parseInt(data[1]);

                        Session s = new Session(sessionSem, sessionID, data[2], data[3], data[4]);

                        // --- UPDATED LOGIC: No Integer.parseInt for IDs ---
                        
                        // Presenter
                        if (data.length > 5) {
                            s.setPresenter(data[5]);
                            if (data.length > 6) s.setPresenterID(data[6]); // Direct String assignment
                        }

                        // Evaluator
                        if (data.length > 7) {
                            s.setEvaluator(data[7]);
                            if (data.length > 8) s.setEvaluatorID(data[8]); // Direct String assignment
                        }
                        // --------------------------------------------------

                        sessions.add(s);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Skipping malformed line (Seminar/Session ID error): " + line);
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
        
        // 1. Update the Header to include ID columns
        if (f.length() == 0)
            writer.append("seminarID,sessionID,sessionType,startTime,endTime,presenter,presenterID,evaluator,evaluatorID\n");

        // 2. Add the IDs to the data string
        // Order matches readSessions: ... Name, ID, Name, ID
        String line = seminarID + "," +
                session.getSessionID() + "," +
                session.getSessionType() + "," +
                session.getStartTime() + "," +
                session.getEndTime() + "," +
                session.getPresenter() + "," +
                session.getPresenterID() + "," + // Insert Presenter ID
                session.getEvaluator() + "," +
                session.getEvaluatorID();        // Insert Evaluator ID
        

        writer.append(line + "\n");
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public void updateSession(Session session, int seminarID) {
    ArrayList<String> allLines = new ArrayList<>();
    
    // Read all lines first
    try (BufferedReader reader = new BufferedReader(new FileReader(sessionFilePath))) {
        String line;
        while ((line = reader.readLine()) != null) allLines.add(line);
    } catch (IOException e) { e.printStackTrace(); }

    // Write back with updates
    try (FileWriter writer = new FileWriter(sessionFilePath, false)) {
        for (String line : allLines) {
            // Use -1 limit to preserve empty trailing columns
            String[] data = line.split(",", -1);

            // 1. Skip logic for header or invalid lines
            if (data.length < 2 || data[0].equalsIgnoreCase("seminarID")) {
                writer.write(line + "\n");
                continue;
            }

            try {
                // 2. Parse SeminarID and SessionID (These remain ints)
                int rowSemID = Integer.parseInt(data[0].trim());
                int rowSessID = Integer.parseInt(data[1].trim());

                // 3. MATCH BOTH IDs: Ensure we only update the specific session
                if (rowSemID == seminarID && rowSessID == session.getSessionID()) {
                    
                    // Construct the new line with 9 columns
                    // Order: SemID, SessID, Type, Start, End, PresName, PresID, EvalName, EvalID
                    String newLine = seminarID + "," + 
                                 session.getSessionID() + "," + 
                                 session.getSessionType() + "," + 
                                 session.getStartTime() + "," + 
                                 session.getEndTime() + "," + 
                                 session.getPresenter() + "," + 
                                 session.getPresenterID() + "," + // Add Presenter UUID
                                 session.getEvaluator() + "," + 
                                 session.getEvaluatorID();        // Add Evaluator UUID
                    
                    writer.write(newLine + "\n");
                } else {
                    // Write existing line unchanged
                    writer.write(line + "\n");
                }
            } catch (NumberFormatException e) {
                // In case of any other corrupted numeric data, just preserve the line
                writer.write(line + "\n");
            }
        }
    } catch (IOException e) { e.printStackTrace(); }
}
    // --- SESSION: Delete (Rewrite Method) ---
    public void deleteSession(int seminarID, int sessionIDToDelete) {
    ArrayList<String> allLines = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(sessionFilePath))) {
        String line;
        while ((line = br.readLine()) != null) allLines.add(line);
    } catch (IOException e) { e.printStackTrace(); }

    try (FileWriter writer = new FileWriter(sessionFilePath, false)) {
        for (String line : allLines) {
            String[] data = line.split(",");

            // Skip header check if data is too small
            if (data.length < 2) {
                writer.write(line + "\n");
                continue;
            }

            try {
                int currentSemID = Integer.parseInt(data[0].trim());
                int currentSessID = Integer.parseInt(data[1].trim());

                // ONLY delete if BOTH IDs match
                if (currentSemID == seminarID && currentSessID == sessionIDToDelete) {
                    continue; // Skip this line (Delete)
                }
            } catch (NumberFormatException e) {
                // This handles the Header row
            }
            writer.write(line + "\n");
        }
    } catch (IOException e) { e.printStackTrace(); }
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