package View;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class HomePage extends JPanel{
    public HomePage(){

// =========================== Home Page Components ============================ //
       
        JLabel homeTextArea = new JLabel("Welcome to Seminar Management System!");
        add(homeTextArea);
        

// =========================== Login Button ============================= //

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                LoginPage loginPage = new LoginPage();
                loginPage.setVisible(true);
            }
        });

// =========================== Sign Up Button ============================= //

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e){
                SignUpPage signUpPage = new SignUpPage();
                signUpPage.setVisible(true);
            }
        });



// ============================= Layout Setup ============================== //


        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER , 10 , 100));

// =========================== Set Box Layout to center components vertically ============================ //

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalGlue());

// =========================== Add components to panel ============================= //

        homeTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(50)); // spacing from top to 50 pixels //
        panel.add(homeTextArea);
      
// ============================== Add Login Button to panel ============================== //

        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(50)); // spacing from top to 50 pixels //
        panel.add(loginButton);

// ============================== Add Register Button to panel =============================== //
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(50)); // spacing from top to 50 pixels //
        panel.add(signUpButton);


        add(panel);



        

       
    }
}
