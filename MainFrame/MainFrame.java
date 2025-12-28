package MainFrame;
import View.HomePage;
import View.LoginPage;
import View.SignUpPage;
import View.StudentDashboard;
import java.awt.CardLayout;
import javax.swing.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

    
    public  MainFrame(){

        LoginPage loginPage = new LoginPage(this);
        SignUpPage signUpPage = new SignUpPage(this);
        HomePage homePage = new HomePage(this);
        StudentDashboard studentDashboard = new StudentDashboard(this);


        mainPanel.add(homePage, "HomePage");
        mainPanel.add(loginPage, "LoginPage");
        mainPanel.add(signUpPage, "SignUpPage");
        mainPanel.add(studentDashboard, "StudentDashboard");

        add(mainPanel);


        setTitle("Seminar Management System");
        setSize(800 , 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public void showPage(String pageName){
        cardLayout.show(mainPanel, pageName);
    }
}
