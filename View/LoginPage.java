package View;
import javax.swing.*;

public class LoginPage extends JFrame {
    public LoginPage(){
        JLabel label = new JLabel("Login Page");
        add(label);
        

        setSize(800 , 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
