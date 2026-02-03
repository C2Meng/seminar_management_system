package View;

import MainFrame.MainFrame;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

public class ViewRegisteredSeminarsPage extends JPanel {

    private MainFrame mainFrame;
    private String currentUserID;

    public ViewRegisteredSeminarsPage(MainFrame mainFrame, String currentUserID) {
        this.mainFrame = mainFrame;
        this.currentUserID = currentUserID;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


        // Title
        JLabel label = new JLabel("View Registered Seminars");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(label);

        add(Box.createVerticalStrut(10)); // spacing

        // Create table to display registered seminars
        String[] columnNames = {"Submission ID", "Seminar ID" , "User ID" , "Title", "Abstract", "Attachment", "Supervisor" , "Presentation Type" , "Status (Graded)"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable seminarsTable = new JTable(tableModel);

        seminarsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e){
                if (e.getClickCount() == 1){
                    int row = seminarsTable.getSelectedRow();

                    String filePath = seminarsTable.getValueAt(row, 5).toString();
                    String id = seminarsTable.getValueAt(row, 0).toString();
                    String title = seminarsTable.getValueAt(row, 3).toString();
                    viewPreview( filePath , id , title);
                }
            }
        });

        // Load data from CSV file filtered by student email
        loadRegisteredSeminars(tableModel, currentUserID);

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
    private void loadRegisteredSeminars(DefaultTableModel tableModel, String currentUserID) {
        String filePath = "Data/Submission.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String currentLine = br.readLine();

            // Read all lines and filter by student email
            while (currentLine != null) {
                String[] data = currentLine.split(",");
                if (data.length >= 8 && data[2].trim().equals(currentUserID)) {
                    tableModel.addRow(new Object[]{
                        data[0].trim(),              // Submission ID
                        data[1].trim(),              // Seminar ID
                        data[2].trim(),              // User ID
                        data[3].trim().replace(";", ","), // Title (swap back ;)
                        data[4].trim().replace(";", ","), // Abstract (swap back ;)
                        data[5].trim(),              // Attachment
                        data[6].trim(),              // Supervisor
                        data[7].trim(),              // Presentation Type
                        data[8].trim()               // Status (Graded)
                    });
                }
                currentLine = br.readLine();
            }
        } catch (IOException e) {
            System.out.println("Error reading seminars file: " + e.getMessage());
        }
    }


    private void viewPreview(String filePath , String id , String title) {
        try {
            SwingController controller = new SwingController();
            SwingViewBuilder factory = new SwingViewBuilder(controller);
            JPanel viewerComponentPanel = factory.buildViewerPanel();

            JFrame viewerFrame = new JFrame("Preview - " + title + " (ID: " + id + ")");
            viewerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            viewerFrame.getContentPane().add(viewerComponentPanel);
            viewerFrame.pack();
            viewerFrame.setVisible(true);
            viewerFrame.setSize(800, 600);


            controller.openDocument(filePath);
            viewerComponentPanel.setVisible(true);  

            
            
        } catch (Exception e) {
        }


    
    }


    private void openPDF(String filePath) {
        try {
            if (filePath != null && !filePath.isEmpty()) {
                java.awt.Desktop.getDesktop().open(new java.io.File(filePath));
            } else {
                System.out.println("Invalid file path.");
            }
        } catch (IOException e) {
            System.out.println("Error opening PDF: " + e.getMessage());
        }
    }
}