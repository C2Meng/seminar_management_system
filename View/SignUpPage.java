package View;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class SignUpPage extends JFrame{
    public SignUpPage(){
       JLabel label = new JLabel("Sign Up Page");
       add(label);

        setSize(800 , 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
