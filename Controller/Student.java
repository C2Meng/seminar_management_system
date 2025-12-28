package Controller;

import InterfaceLib.Navigator;
import InterfaceLib.SignIn;
import InterfaceLib.SignUp;
import Models.WriteToCSV;



public class Student implements SignUp , SignIn{

    private String email;
    private String password;
    private String name;
    private String userType;
    private WriteToCSV writeToCSV = new WriteToCSV();
    private boolean isRegistered = false;
    private String line;
    private Navigator navigator;
    
    public Student (String email, String password , String name , String userType , Navigator navigator ){
        this.email = email;
        this.password = password;
        this.name = name;
        this.userType = userType;
        this.navigator = navigator;
    }


    @Override
    public void registerUser(String name , String password , String email , String userType){
         
         writeToCSV.getFilePath();
         line = name + "," + password + "," + email + "," + userType;
         isRegistered = true;
         writeToCSV.writeData(line);
         navigator.goTo("LoginPage");
         
         

    }

    @Override
    public void authUser(String email , String password ){
       writeToCSV.getFilePath();
       
    }

    @Override
    public void logOut(){
       // implementation for logging out a user //
    }

    @Override
    public void deleteUser(String username , String userType){
          // Implementation for deleting a user //
    }
}


