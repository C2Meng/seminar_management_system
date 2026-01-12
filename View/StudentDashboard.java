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
    public StudentDashboard(MainFrame mainFrame){
        JLabel label = new JLabel("Student Dashboard Page");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(label);


        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalGlue()); // push content to center

        
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
