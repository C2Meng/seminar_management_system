package Controller;

import InterfaceLib.Navigator;
import InterfaceLib.Role;
import InterfaceLib.SignIn;
import InterfaceLib.SignUp;
import Models.WriteToCSV;
import Controller.User;



public class Student extends User implements SignUp {

    private String email;
    private String password;
    private String name;
    private String userType;
    private WriteToCSV writeToCSV = new WriteToCSV();
    private boolean isRegistered = false;
    private String line;
    private Navigator navigator;
    

    public Student (String email, String name , String password , Navigator navigator ){
        super(email , name , password , Role.STUDENT);
        this.navigator = navigator;
    }


    @Override
    public void registerUser(String email , String name , String password , String userType){
         
      
         writeToCSV.getFilePath();
         line = email + "," + name + "," + password + "," + userType;
         isRegistered = true;
         writeToCSV.writeData(line);
         navigator.goTo("LoginPage");

    }

 

  

   


    public void registerForSeminar(String seminarId){
        // Implementation for registering a student for a seminar //
    }


}


