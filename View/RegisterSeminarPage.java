package View;
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
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RegisterSeminarPage extends JPanel {

     private String[] presentationType = {"Poster" , "Oral"};

     public JComboBox <String> presentationTypeComboBox = new JComboBox<>();

     private void getPresentationTypes(){
         for ( String presentation : presentationType){
             presentationTypeComboBox.addItem(presentation);
         }
     }



     public RegisterSeminarPage(MainFrame mainFrame){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Register Seminar Page");
       

        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(label);

    
        add(Box.createVerticalStrut(10)); // spacing

        JLabel titleLabel = new JLabel("Paper Title: ");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);

        add(Box.createVerticalStrut(10)); // spacing

        JTextField titleField = new JTextField();
        titleField.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleField.setMaximumSize(new Dimension(200, 80));
        add(titleField);

        add(Box.createVerticalStrut(10)); // spacing

        JLabel abstractLabel = new JLabel("Abstract: ");
        abstractLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(abstractLabel);

        add(Box.createVerticalStrut(10)); // spacing

        JTextField abstractField = new JTextField();
        abstractField.setAlignmentX(Component.CENTER_ALIGNMENT);
        abstractField.setMaximumSize(new Dimension(200, 80));
        add(abstractField);

        add(Box.createVerticalStrut(10)); // spacing


        JLabel attachmentLabel = new JLabel("Attachment: ");
        attachmentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(attachmentLabel);

        add(Box.createVerticalStrut(10)); // spacing

        JTextField attachmentField = new JTextField();
        attachmentField.setAlignmentX(Component.CENTER_ALIGNMENT);
        attachmentField.setMaximumSize(new Dimension(200, 80));
        add(attachmentField);

        JButton browseButton = new JButton("Browse");
        browseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(browseButton);

        add(Box.createVerticalStrut(10)); // spacing


        JLabel presentationLabel = new JLabel("Presentation Type: ");
        presentationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        presentationLabel.setMaximumSize(new Dimension(200 , 80));
        add(presentationLabel);

        RegisterSeminarPage.this.getPresentationTypes();
        add(presentationTypeComboBox);
        presentationTypeComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        presentationTypeComboBox.setMaximumSize(new Dimension(200 , 80));
        add(Box.createVerticalStrut(10)); // spacing


        JButton submitButton = new JButton("Submit");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(submitButton);

        add(Box.createVerticalStrut(10)); // spacing

        JButton backButton = new JButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(backButton);

        backButton.addActionListener(e -> {
            mainFrame.showPage("StudentDashboard");
        });


        browseButton.addActionListener( e->{
            JFileChooser fileChoose = new JFileChooser();
            fileChoose.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChoose.setDialogTitle("Select File");
            int result = fileChoose.showOpenDialog(RegisterSeminarPage.this);
            if (result == JFileChooser.APPROVE_OPTION){
                File selectedFile = fileChoose.getSelectedFile();
                attachmentField.setText(selectedFile.getAbsolutePath());
            }
        });







   add(Box.createVerticalGlue());

        



     }
}
