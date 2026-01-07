package View;

import Controller.Coordinator;
import Controller.Evaluator;
import Controller.Student;
import Controller.User;
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


                if (email.isEmpty() || name.isEmpty() || password.isEmpty()){
                    JOptionPane.showMessageDialog(SignUpPage.this, "Please fill all fields" , 
                        "Error" , JOptionPane.ERROR_MESSAGE
                    ); }
                    else if( !email.contains("@") || !email.contains(".")){
                      JOptionPane.showMessageDialog(SignUpPage.this, "Please enter a valid email address" , 
                        "Error" , JOptionPane.ERROR_MESSAGE );
                        emailArea.setText("");
                        passwordArea.setText("");
                        nameArea.setText("");
                     return;
                    

                    } else if ( password.length() < 6){
                        JOptionPane.showMessageDialog(SignUpPage.this, "Password must be at least 6 characters long" , 
                            "Error" , JOptionPane.ERROR_MESSAGE );
                            passwordArea.setText("");
                         return;
                    }
                else if (userType.equals("Student")){

                   User user = new Student(email, name, password, mainFrame);
                   ((Student) user).registerUser(email, name, password, userType);

                   JOptionPane.showMessageDialog(SignUpPage.this, "Registration Successful! Please login to enter", 
                   "success", JOptionPane.INFORMATION_MESSAGE);

                } else if (userType.equals("Evaluator")){
                    User user = new Evaluator(email, name, password, mainFrame);
                    ((Evaluator) user).registerUser(email, name, password, userType);

                    JOptionPane.showMessageDialog(SignUpPage.this, "Registration Successful! Please login to enter", 
                   "success", JOptionPane.INFORMATION_MESSAGE);
                   
                }
                else if (userType.equals("Coordinator")){
                    User user = new Coordinator(email, name, password, mainFrame);
                    ((Coordinator) user).registerUser(email, name, password, userType);
                    

                    JOptionPane.showMessageDialog(SignUpPage.this, "Registration Successful! Please login to enter", 
                   "success", JOptionPane.INFORMATION_MESSAGE);
                   
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
