package MainFrame;
import InterfaceLib.Navigator;
import View.CoordinatorDashboard;
import View.HomePage;
import View.LoginPage;
import View.RegisterSeminarPage;
import View.SignUpPage;
import View.StudentDashboard;
import java.awt.CardLayout;
import javax.swing.*;

public class MainFrame extends JFrame implements Navigator {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

    
    public  MainFrame(){

        LoginPage loginPage = new LoginPage(this);
        SignUpPage signUpPage = new SignUpPage(this);
        HomePage homePage = new HomePage(this);
        StudentDashboard studentDashboard = new StudentDashboard(this);
        RegisterSeminarPage registerSeminarPage = new RegisterSeminarPage(this);
        CoordinatorDashboard coordinatorDashboard = new CoordinatorDashboard(this);
    


        mainPanel.add(homePage, "HomePage");
        mainPanel.add(loginPage, "LoginPage");
        mainPanel.add(signUpPage, "SignUpPage");
        mainPanel.add(studentDashboard, "StudentDashboard");
        mainPanel.add(registerSeminarPage, "RegisterSeminarPage");
        mainPanel.add(coordinatorDashboard, "CoordinatorDashboard");


        add(mainPanel);


        setTitle("Seminar Management System");
        setSize(800 , 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    
    @Override
    public void goTo(String pageName){
        showPage(pageName);
    }


    


    public void showPage(String pageName){
        cardLayout.show(mainPanel, pageName);
    }
}
