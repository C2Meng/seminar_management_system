package View;

import MainFrame.MainFrame;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class RegisteredSeminarsPage extends JPanel {
    private JTable seminarsTable;
    private DefaultTableModel tableModel;
    private MainFrame mainFrame;
    private String studentEmail; // To be set when viewing seminars

    public RegisteredSeminarsPage(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Title
        JLabel titleLabel = new JLabel("My Registered Seminars");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);

        add(Box.createVerticalStrut(20));

        // Table setup
        String[] columnNames = {"Seminar ID", "Title", "Presenter", "Session Type", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0);
        seminarsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(seminarsTable);
        add(scrollPane);

        add(Box.createVerticalStrut(10));

        // Back button
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(backButton);

        backButton.addActionListener(e -> {
            mainFrame.showPage("StudentDashboard");
        });
    }

    // Method to set student email and load their seminars
    public void loadStudentSeminars(String email) {
        this.studentEmail = email;
        // TODO: Implement loading logic from CSV
        // This will call Student controller to get registered seminars
    }

    // Method to display seminars in the table
    public void displaySeminars(ArrayList<Object[]> seminars) {
        tableModel.setRowCount(0); // Clear existing rows
        for (Object[] seminar : seminars) {
            tableModel.addRow(seminar);
        }
    }
}
