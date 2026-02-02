package View;
import Controller.Session;
import MainFrame.MainFrame;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;




public class SeminarSchedulePage extends JPanel {
    // Seminar Schedule Page implementation here //    
    public SeminarSchedulePage(MainFrame mainFrame , String currentUserID){
        JLabel label = new JLabel("Seminar Schedule Page");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(label);


        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalGlue()); // push content to center


        
        

        add(Box.createVerticalGlue()); // push content to center

        String[] columnNames = {"Seminar Name", "Venue", "Type", "Start", "End", "Presenter"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);

        table.setRowHeight(30); 
        table.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 40)); 

        Session session = new Session();
        session.viewSession(model);

        add(new JScrollPane(table), BorderLayout.CENTER);


        JButton backButton = new JButton("Back to Dashboard");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(backButton);

        backButton.addActionListener(e -> {
            mainFrame.showPage("StudentDashboard");
        });


    }

}
