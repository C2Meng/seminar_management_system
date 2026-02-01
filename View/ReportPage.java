package View;
import MainFrame.MainFrame;
import Models.WriteToCSV;
import java.awt.Component;
import java.io.File;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ReportPage extends JPanel {


     public ReportPage(MainFrame mainFrame){


        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Report Page");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(label);

        add(Box.createVerticalStrut(10)); // spacing



        JButton generateSeminarButton = new JButton("Generate Seminar Report");
        generateSeminarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(generateSeminarButton);


        generateSeminarButton.addActionListener(e ->{
                WriteToCSV writeToCSV = new WriteToCSV();
                writeToCSV.generateSeminarReport();
                int choice = JOptionPane.showConfirmDialog(mainFrame, "Seminar report generated! Do you want to save it to your device?" , "Success" , JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {

    // Open file chooser for user to pick save location
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setSelectedFile(new File("seminar_report.txt")); // default name

    int option = fileChooser.showSaveDialog(mainFrame); // parent component

    if (option == JFileChooser.APPROVE_OPTION) {
        File destination = fileChooser.getSelectedFile(); // get the selected file
 
        WriteToCSV writer = new WriteToCSV(); // create instance to use the method
        writer.saveReportToDevice("Data/seminar_report.txt", destination, mainFrame); // save the report to the chosen file
    
    } else { 
        // user cancelled the save dialog
        JOptionPane.showMessageDialog(mainFrame, "Save operation cancelled." , "Cancelled" , JOptionPane.INFORMATION_MESSAGE);
    }
}

        });



        add(Box.createVerticalStrut(10)); // spacing


        JButton generatefinalEvalButton = new JButton("Generate Final Evaluation Report");
        generatefinalEvalButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(generatefinalEvalButton);

        add(Box.createVerticalStrut(10)); // spacing

        generatefinalEvalButton.addActionListener( e->{
            WriteToCSV writeToCSV = new WriteToCSV();
            writeToCSV.generateAwardReport();

            int choice = JOptionPane.showConfirmDialog(mainFrame, "Final Evaluation report generated! Do you want to save it to your device?" , "Success" , JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setSelectedFile(new File("award_report.txt")); // default name

                int option = fileChooser.showSaveDialog(mainFrame); // parent component

                if (option == JFileChooser.APPROVE_OPTION) {
                    File destination = fileChooser.getSelectedFile(); // get the selected file

                    WriteToCSV writer = new WriteToCSV(); // create instance to use the method
                    writer.saveReportToDevice("Data/award_report.txt", destination, mainFrame); // save the report to the chosen file
                } else { 
                    // user cancelled the save dialog
                    JOptionPane.showMessageDialog(mainFrame, "Save operation cancelled." , "Cancelled" , JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });


        JButton generateOverallReportButton = new JButton("Generate Overall Report");
        generateOverallReportButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(generateOverallReportButton);


        generateOverallReportButton.addActionListener( e->{
            WriteToCSV writeToCSV = new WriteToCSV();
            writeToCSV.generateOverallReport();
            int choice = JOptionPane.showConfirmDialog(mainFrame, "Overall report generated! Do you want to save it to your device?" , "Success" , JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setSelectedFile(new File("overall_report.txt")); // default name

                int option = fileChooser.showSaveDialog(mainFrame); // parent component

                if (option == JFileChooser.APPROVE_OPTION) {
                    File destination = fileChooser.getSelectedFile(); // get the selected file

                    WriteToCSV writer = new WriteToCSV(); // create instance to use the method
                    writer.saveReportToDevice("Data/overall_report.txt", destination, mainFrame); // save the report to the chosen file
                } else { 
                    // user cancelled the save dialog
                    JOptionPane.showMessageDialog(mainFrame, "Save operation cancelled." , "Cancelled" , JOptionPane.INFORMATION_MESSAGE);
                }
            }

        });

        add(Box.createVerticalStrut(10)); // spacing


        JButton backButton = new JButton("Back to Dashboard");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(backButton);

        backButton.addActionListener(e-> {
                mainFrame.showPage("CoordinatorDashboard");
        });













}

}