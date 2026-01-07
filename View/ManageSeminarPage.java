package View;
import Controller.Student;
import MainFrame.MainFrame;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ManageSeminarPage extends JPanel {


     public ManageSeminarPage(MainFrame mainFrame){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Manage Seminar Page");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(label);
}

}