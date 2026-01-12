package Controller;

import InterfaceLib.Navigator;
import InterfaceLib.Role;
import InterfaceLib.SignUp;
import Models.WriteToCSV;



public class Student extends User implements SignUp {

    private String email;
    private String password;
    private String name;
    private String userType;
    private String seminarId;
    private String title;
    private String abstractText;
    private WriteToCSV writeToCSV = new WriteToCSV();
    public boolean isRegistered = false;
    private String line;
    private Navigator navigator;
    

    public Student (String email, String name , String password , Navigator navigator ){
        super(email , name , password , Role.STUDENT);
        this.email = email;
        this.navigator = navigator;
    }


    @Override
    public void registerUser(String email , String name , String password , String userType){
         
      
         writeToCSV.getFilePath();
         line = email + "," + name + "," + password + "," + userType;
         writeToCSV.writeData(line);
         isRegistered = true;
         navigator.goTo("LoginPage");

    }

    @Override
    public void deleteUser(String email , String password ){
       // Authentication logic for Student //
    }

 // =================================== method to register for seminar =========================================== //
 
    public void registerForSeminar(String seminarId , String title , String abstractText , String attachment , String presentationType){
        // Seminar registration logic for Student //
        writeToCSV.setFilePath("Data/SeminarRegistrations.csv");
        String line = email + "," + seminarId + "," + title + "," + abstractText + "," + attachment + "," + presentationType;
        writeToCSV.writeData(line);

    }


}


