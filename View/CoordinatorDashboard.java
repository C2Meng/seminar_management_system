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
        JLabel label = new JLabel("Coordinator Dashboard Page");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(label);
        
    }
}
