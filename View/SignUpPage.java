package View;

import Controller.Student;
import MainFrame.MainFrame;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.TextField;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SignUpPage extends JPanel{

    private String[] roles = { "Student" , "Evaluator" , "Coordinator"};

    private JComboBox <String> roleComboBox = new JComboBox<>();

    public void getRoles(){
        for (String role : roles){
            roleComboBox.addItem(role);
            
        }
    }


    public SignUpPage(MainFrame mainFrame){
       JLabel label = new JLabel("Sign Up Page");
       label.setAlignmentX(Component.CENTER_ALIGNMENT);
       add(label);


        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalGlue()); // push content to center


// ======================================== USERNAME LABEL & TEXTFIELD =========================================== //


        JLabel emailLabel = new JLabel("Email: " );
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(emailLabel);

        TextField emailArea = new TextField();
        emailArea.setMaximumSize(new Dimension(200 , 200)); 
        add(emailArea);
        
        
        add(Box.createVerticalStrut(10)); // spacing


        JLabel usernameLabel = new JLabel("Name: " );
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(usernameLabel);

        TextField nameArea = new TextField();
        nameArea.setMaximumSize(new Dimension(200 , 200)); 
        add(nameArea);       
        
        add(Box.createVerticalStrut(10)); // spacing

        JLabel passwordLabel = new JLabel("Password: ");
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(passwordLabel);

        TextField passwordArea = new TextField();
        passwordArea.setMaximumSize(new Dimension(200 , 200));
        add(passwordArea);

        add(Box.createVerticalStrut(10)); // spacing

        JLabel roleLabel = new JLabel("Select Role: ");
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(roleLabel);

        add(Box.createVerticalStrut(10)); // spacing

        SignUpPage.this.getRoles();
        roleComboBox.setMaximumSize(new Dimension(200 , 200));
        add(roleComboBox);

        add(Box.createVerticalStrut(20)); // spacing

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(signUpButton);

        signUpButton.addActionListener(e -> {
                String email = emailArea.getText();
                String name = nameArea.getText();
                String password = passwordArea.getText();
                String userType = roleComboBox.getSelectedItem().toString();

                if (userType.equals("Student")){
                   Student student = new Student(email , name , password , userType , mainFrame);
                   student.registerUser(name, password, email, userType);
                   JOptionPane.showMessageDialog(SignUpPage.this, "Registration Successful! Please login to enter", 
                   "success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    
                }
               
            }
        );


        add(Box.createVerticalStrut(10));

// =================================================DISPLAY SIGN UP====================================================== //

        JButton loginButton = new JButton("Already have an account? Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(loginButton);


        loginButton.addActionListener(e-> 
            mainFrame.showPage("LoginPage")
        );
       
        add(Box.createVerticalGlue()); // push content to center
       
     
    }
}
