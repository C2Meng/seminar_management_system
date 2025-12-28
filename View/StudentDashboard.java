package View;

import MainFrame.MainFrame;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.BoxLayout;
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

    }
}
