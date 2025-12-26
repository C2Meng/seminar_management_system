package MainFrame;
import View.HomePage;
import javax.swing.*;

public class MainFrame extends JFrame {
    
    public  MainFrame(){
        // set the title of the frame //
        setTitle("Seminar Management System");


        // calling HomePage //
        HomePage homePage = new HomePage();
        add(homePage);
        
        // set the size , close operation and visibility of the frame //
        
        setSize(800 , 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
