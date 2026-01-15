package View;

import Controller.Student;
import MainFrame.MainFrame;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ViewRegisteredSeminarsPage extends JPanel {

    public ViewRegisteredSeminarsPage(MainFrame mainFrame, Student student) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Title
        JLabel label = new JLabel("View Registered Seminars");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(label);

        add(Box.createVerticalStrut(10)); // spacing

        // Create table to display registered seminars
        String[] columnNames = {"Seminar ID", "Title", "Abstract", "Attachment", "Supervisor" , "Presentation Type"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable seminarsTable = new JTable(tableModel);

        // Load data from CSV file filtered by student email
        loadRegisteredSeminars(tableModel, student.getEmail());

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(seminarsTable);
        add(scrollPane);

        add(Box.createVerticalStrut(10)); // spacing

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(backButton);

        backButton.addActionListener(e -> {
            mainFrame.showPage("StudentDashboard");
        });
    }

    // Method to load registered seminars from CSV filtered by student email
    private void loadRegisteredSeminars(DefaultTableModel tableModel, String studentEmail) {
        String filePath = "Data/SeminarRegistrations.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String currentLine = br.readLine();

            // Read all lines and filter by student email
            while (currentLine != null) {
                String[] data = currentLine.split(",");
                if (data.length >= 7 && data[0].trim().equals(studentEmail)) {
                    tableModel.addRow(new Object[]{
                            data[1].trim(), // Seminar ID
                            data[2].trim(), // Title
                            data[3].trim(), // Abstract
                            data[4].trim(), // Attachment
                            data[5].trim(), // Supervisor
                            data[6].trim()  // Presentation Type
                    });
                }
                currentLine = br.readLine();
            }
        } catch (IOException e) {
            System.out.println("Error reading seminars file: " + e.getMessage());
        }
    }
}
