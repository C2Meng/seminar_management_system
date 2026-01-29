package MainFrame;
import Controller.Student;
import InterfaceLib.Navigator;
import View.AwardPage;
import View.CoordinatorDashboard;
import View.HomePage;
import View.LoginPage;
import View.ManageSeminarPage;
import View.RegisterSeminarPage;
import View.ReportPage;
import View.SignUpPage;
import View.StudentDashboard;
import View.ViewRegisteredSeminarsPage;
import java.awt.CardLayout;
import javax.swing.*;

public class MainFrame extends JFrame implements Navigator {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    private Student currentStudent;
    private String currentUserID;

    
    public  MainFrame(){

        LoginPage loginPage = new LoginPage(this);
        SignUpPage signUpPage = new SignUpPage(this);
        HomePage homePage = new HomePage(this);
      
    
       
    
    


        mainPanel.add(homePage, "HomePage");
        mainPanel.add(loginPage, "LoginPage");
        mainPanel.add(signUpPage, "SignUpPage");
       


        add(mainPanel);


        setTitle("Seminar Management System");
        setSize(800 , 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

// ===================================== method to set current student when logged in ========================================= //
    public void setCurrentStudent(Student student){
        this.currentStudent = student;
    }

    public Student getCurrentStudent(){
        return this.currentStudent;
    }


// ===================================== method to set current user ID when logged in ========================================= //
    public void setCurrentUserID(String userID){
        this.currentUserID = userID;
    }

    public String getCurrentUserID(){
        return this.currentUserID;
    }



// ==================================== method to go to register seminar page ========================================== //

       public void goToRegisterSeminarPage() {

        // create a new register seminar page with current student , showing that the student is logged in //
        RegisterSeminarPage page = new RegisterSeminarPage(this, currentUserID);
        mainPanel.add(page, "RegisterSeminarPage");
        showPage("RegisterSeminarPage");
    }

// ==================================== method to go to view registered seminars page ========================================== //

    public void goToViewRegisteredSeminarsPage() {
        // create a new view registered seminars page with current student
        ViewRegisteredSeminarsPage page = new ViewRegisteredSeminarsPage(this, currentUserID);
        mainPanel.add(page, "ViewRegisteredSeminarsPage");
        showPage("ViewRegisteredSeminarsPage");
    }



    
    @Override
    public void goTo(String pageName){
        showPage(pageName);
    }


    


    public void showPage(String pageName){

        if (pageName.equals("StudentDashboard")) {
            // Recreate StudentDashboard to ensure it has the latest data
            StudentDashboard studentDashboard = new StudentDashboard(this , currentUserID);
            mainPanel.add(studentDashboard, "StudentDashboard");
        }

         if (pageName.equals("CoordinatorDashboard")) {
            // Recreate CoordinatorDashboard to ensure it has the latest data
            CoordinatorDashboard coordinatorDashboard = new CoordinatorDashboard(this , currentUserID);
            mainPanel.add(coordinatorDashboard, "CoordinatorDashboard");
        }

         if (pageName.equals("ManageSeminarPage")) {
            // Recreate ManageSeminarPage to ensure it has the latest data
            ManageSeminarPage manageSeminarPage = new ManageSeminarPage(this , currentUserID);
            mainPanel.add(manageSeminarPage, "ManageSeminarPage");
        }

        if (pageName.equals("ReportPage")) {
            // Recreate ReportPage to ensure it has the latest data
            ReportPage reportPage = new ReportPage(this);
            mainPanel.add(reportPage, "ReportPage");
        }

        if (pageName.equals("RegisterSeminarPage")) {
            // Recreate RegisterSeminarPage to ensure it has the latest data
            RegisterSeminarPage registerSeminarPage = new RegisterSeminarPage(this , currentUserID);
            mainPanel.add(registerSeminarPage, "RegisterSeminarPage");
        }

         if (pageName.equals("ViewRegisteredSeminarsPage")) {
            // Recreate ViewRegisteredSeminarsPage to ensure it has the latest data
            ViewRegisteredSeminarsPage viewRegisteredSeminarsPage = new ViewRegisteredSeminarsPage(this , currentUserID);
            mainPanel.add(viewRegisteredSeminarsPage, "ViewRegisteredSeminarsPage");
        } 

        if(pageName.equals("AwardPage")){
            AwardPage awardPage = new AwardPage(this , currentUserID);
            mainPanel.add(awardPage , "AwardPage");
        }
        
        cardLayout.show(mainPanel, pageName);
    }
}
