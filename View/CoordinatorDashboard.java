package View;

import Controller.Coordinator;
import MainFrame.MainFrame;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CoordinatorDashboard extends JPanel {
    public CoordinatorDashboard(MainFrame mainFrame){
        // Coordinator dashboard implementation here //
        add(Box.createVerticalStrut(15));
        JLabel label = new JLabel("Coordinator Dashboard Page");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(label);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalGlue());


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

        award.addActionListener( e ->{
            mainFrame.showPage("AwardPage");
        });

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
