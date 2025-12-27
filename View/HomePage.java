package View;

import MainFrame.MainFrame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.*;


public class HomePage extends JPanel{

    public HomePage(MainFrame mainFrame){ 
        
// ======================================== WELCOME LABEL ===========================================//

       setLayout(new GridBagLayout());
       GridBagConstraints gbc = new GridBagConstraints();
       gbc.gridx = 0;
       gbc.gridy = 0;
       gbc.weightx = 1.0;
       gbc.weighty = 0.1;
       gbc.anchor = GridBagConstraints.NORTH;
       gbc.insets = new Insets(20, 0, 0, 0);
       JLabel label = new JLabel("Welcome to the seminar management system");
       add(label , gbc);

// ======================================== LOGIN BUTTON ========================================== //

       gbc.gridy = 1;
       gbc.weighty = 1.0;
       gbc.anchor = GridBagConstraints.NORTH;
       gbc.insets = new Insets(0, 0, 0, 0);    // Reset margins
       JButton loginButton = new JButton("Login");
       loginButton.addActionListener(e-> 
           mainFrame.showPage("LoginPage")
       );
       add(loginButton, gbc);

// ======================================== SIGNUP BUTTON ========================================= //

       gbc.gridy = 1;
       gbc.weighty = 1.0;
       gbc.anchor = GridBagConstraints.NORTH;
       gbc.insets = new Insets(40, 0, 0, 0);    
       JButton signUpButton = new JButton("Sign Up");
       signUpButton.addActionListener(e->
         mainFrame.showPage("SignUpPage")
       );

       add(signUpButton, gbc);


        

       
    }
}
