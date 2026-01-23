
import MainFrame.MainFrame;
import Models.WriteToCSV;


public class Main {
    public static void main(String[] args){

        WriteToCSV writeToCSV = new WriteToCSV();
        writeToCSV.createFile();
        writeToCSV.createUserFile();
        
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    }
}