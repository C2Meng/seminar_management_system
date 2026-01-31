package View;
import Controller.Student;
import MainFrame.MainFrame;
import Models.WriteToCSV;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.TextField;
import javax.swing.*;

public class LoginPage extends JPanel {
    public LoginPage(MainFrame mainFrame){

        JLabel label = new JLabel("Login Page");
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

        JLabel passwordLabel = new JLabel("Password: ");
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(passwordLabel);

        TextField passwordArea = new TextField();
        passwordArea.setMaximumSize(new Dimension(200 , 200));
        add(passwordArea);

        add(Box.createVerticalStrut(10)); // spacing

        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(loginButton);

        loginButton.addActionListener(e -> {
            String email = emailArea.getText();
            String password = passwordArea.getText();
          
            if (email.isEmpty() || password.isEmpty()){
                JOptionPane.showMessageDialog(LoginPage.this, "Please enter both email and password" , 
                    "Error" , JOptionPane.ERROR_MESSAGE
                );
                 return;


            } else if (!email.contains("@") || !email.contains(".")){
                  JOptionPane.showMessageDialog(LoginPage.this, "Please enter a valid email address" , 
                    "Error" , JOptionPane.ERROR_MESSAGE
                );
                 return;
                 
            } else if (password.length() < 6) {
                 JOptionPane.showMessageDialog(LoginPage.this, "Password must be at least 6 characters long" , 
                    "Error" , JOptionPane.ERROR_MESSAGE
                );
                 return;
            }
            
            else {

           
            WriteToCSV writeToCSV = new WriteToCSV();
            String[] userData =  writeToCSV.verifyUser(email, password, mainFrame);


            if (userData != null) {
            String userId = userData[0]; // The UUID from column 1
            String userRole = userData[1];   // The Role from column 5
           

            mainFrame.setCurrentUserID( userId ); // set current user ID in main frame

            if ("Student".equals(userRole)) {
               Student student = new Student(email, null, password, mainFrame);

             // set the current student in main frame , showing that the student is logged in //
             
               mainFrame.setCurrentStudent(student);
               mainFrame.showPage("StudentDashboard");

            } else if ("Evaluator".equals(userRole)) {
              EvaluatorSystem evaluatorSystem = new EvaluatorSystem(userId);
              evaluatorSystem.setVisible(true);
              mainFrame.dispose();
            }
              else if ("Coordinator".equals(userRole)) {
              //redirect to coordinator frame
              mainFrame.showPage("CoordinatorDashboard");
              //set visible
              //mainFrame.dispose(); close login page

            } 
            else {
                
               JOptionPane.showMessageDialog(LoginPage.this, "Invalid email or password",
                "Error", JOptionPane.ERROR_MESSAGE);
               };

           
    }
     }
        });
       
        add(Box.createVerticalStrut(10));

// =================================================DISPLAY SIGN UP====================================================== //

        JButton signUpButton = new JButton("Don't have an account? Sign Up");
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(signUpButton);


        signUpButton.addActionListener(e-> 
            mainFrame.showPage("SignUpPage")
        );
       
        add(Box.createVerticalGlue());
    }
}
