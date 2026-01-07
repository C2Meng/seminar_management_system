package MainFrame;
import Controller.Student;
import InterfaceLib.Navigator;
import View.CoordinatorDashboard;
import View.HomePage;
import View.LoginPage;
import View.ManageSeminarPage;
import View.RegisterSeminarPage;
import View.ReportPage;
import View.SignUpPage;
import View.StudentDashboard;
import java.awt.CardLayout;
import javax.swing.*;

public class MainFrame extends JFrame implements Navigator {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    private Student currentStudent;

    
    public  MainFrame(){

        LoginPage loginPage = new LoginPage(this);
        SignUpPage signUpPage = new SignUpPage(this);
        HomePage homePage = new HomePage(this);
        StudentDashboard studentDashboard = new StudentDashboard(this);
        RegisterSeminarPage registerSeminarPage = new RegisterSeminarPage(this , currentStudent);
        CoordinatorDashboard coordinatorDashboard = new CoordinatorDashboard(this);
        ManageSeminarPage manageSeminarPage = new ManageSeminarPage(this);
        ReportPage reportPage = new ReportPage(this);
    
    


        mainPanel.add(homePage, "HomePage");
        mainPanel.add(loginPage, "LoginPage");
        mainPanel.add(signUpPage, "SignUpPage");
        mainPanel.add(studentDashboard, "StudentDashboard");
        mainPanel.add(registerSeminarPage, "RegisterSeminarPage");
        mainPanel.add(coordinatorDashboard, "CoordinatorDashboard");
        mainPanel.add(manageSeminarPage, "ManageSeminarPage");
        mainPanel.add(reportPage, "ReportPage");
       


        add(mainPanel);


        setTitle("Seminar Management System");
        setSize(800 , 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

// ===================================== method to set current student when logged in ========================================= //
    public void setCurrentStudent(Student student){
        this.currentStudent = student;
    }



// ==================================== method to go to register seminar page ========================================== //

       public void goToRegisterSeminarPage() {

        // create a new register seminar page with current student , showing that the student is logged in //
        RegisterSeminarPage page = new RegisterSeminarPage(this, currentStudent);
        mainPanel.add(page, "RegisterSeminarPage");
        showPage("RegisterSeminarPage");
    }



    
    @Override
    public void goTo(String pageName){
        showPage(pageName);
    }


    


    public void showPage(String pageName){
        cardLayout.show(mainPanel, pageName);
    }
}
