package View;
import MainFrame.MainFrame;
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

        JLabel usernameLabel = new JLabel("Username: " );
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(usernameLabel);

        TextField usernameArea = new TextField();
        usernameArea.setMaximumSize(new Dimension(200 , 200)); 
        add(usernameArea);       
        
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


        add(Box.createVerticalStrut(10));

// =================================================DISPLAY SIGN UP====================================================== //

        JButton signUpButton = new JButton("Already have an account? Login");
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(signUpButton);


        signUpButton.addActionListener(e-> 
            mainFrame.showPage("SignUpPage")
        );
       
        add(Box.createVerticalGlue());
    }
}
