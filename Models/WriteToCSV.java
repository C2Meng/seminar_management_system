package Models;
import InterfaceLib.Navigator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class WriteToCSV {


    private String filePath = "Data/database.csv";

    public String getFilePath(){
        return filePath;
    }

     public void createFile() {
        try {
            File file = new File(filePath);

            // create folder if missing
            file.getParentFile().mkdirs();

            // write header only if file does NOT exist
            if (!file.exists()) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.append("email,name,password,role\n"); // header
                    System.out.println("CSV file created with headers.");
                }
            } else {
                System.out.println("CSV file already exists, skipping header creation.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void writeData (String line){
        try (FileWriter writer = new FileWriter(filePath, true)){

            writer.append(line + "\n");
            System.out.println("Data written successfully");
        } catch (IOException e){
            e.printStackTrace();
        }
    }


// =========================== method to verify user credentials =========================== //

    public boolean verifyUser(String email , String password , Navigator navigator){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String currentLine;
            br.readLine();
            while((currentLine = br.readLine())!= null){
                String[] data = currentLine.split(",");
                String storedEmail = data[0];
                String storedPassword = data[2];
                String storedRole = data[3];

                if (storedEmail.equals(email) && storedPassword.equals(password)){
                    if (storedRole.equals("Student")){
                        navigator.goTo("StudentDashboard");
                    } else if (storedRole.equals("Evaluator")){
                        navigator.goTo("EvaluatorDashboard");
                    } 


                    return true;

                    
                } else {
                    System.out.println("Invalid email or password.");
                }
                
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        return false;
    }
}