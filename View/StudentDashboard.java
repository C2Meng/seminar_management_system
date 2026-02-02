package View;

import MainFrame.MainFrame;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class StudentDashboard extends JPanel{
    // Student Dashboard implementation here //
    public StudentDashboard(MainFrame mainFrame , String currentUserID){
        JLabel label = new JLabel("Student Dashboard Page");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(label);


        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalGlue()); // push content to center

        JLabel welcomeLabel = new JLabel("Welcome to the Student Dashboard! User with ID" );  
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(welcomeLabel);

        JLabel userIDLabel = new JLabel("Your User ID: " + currentUserID);
        userIDLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(userIDLabel);
        
        add(Box.createVerticalStrut(10)); // spacing

        JButton enterSeminarButton = new JButton("Register for Seminar"); // button to register for seminar
        enterSeminarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(enterSeminarButton);

        enterSeminarButton.addActionListener(e ->{
            mainFrame.goToRegisterSeminarPage();
        });

        add(Box.createVerticalStrut(10)); // spacing

        JButton viewSeminarsButton = new JButton("View Registered Seminars"); // button to view registered seminars
        viewSeminarsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(viewSeminarsButton);

        add(Box.createVerticalStrut(10)); // spacing

        JButton viewSeminarScheduleButton = new JButton("View Seminar Schedule"); // button to view seminar schedule
        viewSeminarScheduleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(viewSeminarScheduleButton);

        viewSeminarScheduleButton.addActionListener(e->{
            mainFrame.showPage("SeminarSchedulePage");
        });

        viewSeminarsButton.addActionListener(e -> {
            mainFrame.goToViewRegisteredSeminarsPage();
        });

        add(Box.createVerticalStrut(10)); // spacing


        JButton logoutButton = new JButton("Logout");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(logoutButton);

        logoutButton.addActionListener( e ->{
            mainFrame.showPage("HomePage");
        });






        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalGlue()); // push content to center

    }
}
