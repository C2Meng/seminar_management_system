package Models;
import java.io.File;
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
}
