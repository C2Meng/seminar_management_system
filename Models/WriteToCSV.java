package Models;

import Controller.Seminar;
import InterfaceLib.Navigator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WriteToCSV {

    // =========================== FILE PATHS =========================== //
    private String filePath = "Data/database.csv";       // Stores Users
    private String seminarFilePath = "Data/seminar.csv"; // Stores Seminars
    private String sessionFilePath = "Data/session.csv"; // Stores Sessions


    // =========================== USER MANAGEMENT (EXISTING CODE) =========================== //

    public String getFilePath(){
        return filePath;
    }

    public void setFilePath(String filePath){
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
    public void writeData (String line){
        try (FileWriter writer = new FileWriter(filePath, true)){
            writer.append(line + "\n");
            System.out.println("User data written successfully");
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    // Verifies user credentials for Login
    public String verifyUser(String email , String password , Navigator navigator){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String currentLine;
            br.readLine(); // Skip header
            while((currentLine = br.readLine())!= null){
                String[] data = currentLine.split(",");
                // Ensure the line has enough data to prevent crashes on empty lines
                if (data.length < 4) continue; 

                String storedEmail = data[0]; 
                // Index 1 is usually name, Index 2 is password based on your registerUser logic
                String storedPassword = data[2]; 
                String storedRole = data[3];

                if (storedEmail.equals(email) && storedPassword.equals(password)){
                    return storedRole;
                } 
            }
            // If loop finishes without return, credentials are invalid
            System.out.println("Invalid email or password.");
        } catch (IOException e){
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

        if (!file.exists()) return list; // Return empty list if no file exists

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
                        if(data.length > 2) s.setDescription(data[2]);
                        if(data.length > 3) s.setVenue(data[3]);
                        if(data.length > 4) s.setDate(data[4]);
                        if(data.length > 5) s.setStartTime(data[5]);
                        if(data.length > 6) s.setEndTime(data[6]);
                        
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
    
    // 3. Update/Overwrite Seminar CSV (Used for Delete or Edit operations)
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
}