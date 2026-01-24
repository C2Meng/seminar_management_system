package View;

import MainFrame.MainFrame;
import java.awt.*;
import javax.swing.*;

public class CoordinatorDashboard extends JPanel {
    public CoordinatorDashboard(MainFrame mainFrame , String currentUserID){
        // Coordinator dashboard implementation here //
        JLabel label = new JLabel("Coordinator Dashboard Page");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(label);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalGlue());

        JLabel welcomeLabel = new JLabel("Welcome to the Coordinator Dashboard! User with ID " + currentUserID);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(welcomeLabel);

        add(Box.createVerticalStrut(10)); // spacing

        //buttons

        //make a separate page
        JButton manageSeminars = new JButton("Manage Seminar");
        manageSeminars.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(manageSeminars);

         manageSeminars.addActionListener( e ->{
            mainFrame.showPage("ManageSeminarPage");
        });

        

        add(Box.createVerticalStrut(10));

        //separate page
        JButton report = new JButton("Reports");
        report.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(report);

        report.addActionListener( e ->{
            mainFrame.showPage("ReportPage");
        });

        add(Box.createVerticalStrut(10));

        //jdialog
        JButton award = new JButton("Awards");
        award.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(award);

        add(Box.createVerticalStrut(10));

        JButton logout = new JButton("Logout");
        logout.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(logout);

        logout.addActionListener( e ->{
            mainFrame.showPage("HomePage");
        });


        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalGlue()); // push content to center
    }
}
