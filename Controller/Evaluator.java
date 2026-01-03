package Controller;

import InterfaceLib.Navigator;
import InterfaceLib.Role;
import InterfaceLib.SignIn;
import InterfaceLib.SignUp;
import Models.WriteToCSV;


public class Evaluator extends User implements SignUp , SignIn{

    private String email;
    private String password;
    private String name;
    private String userType;
    private WriteToCSV writeToCSV = new WriteToCSV();
    private boolean isRegistered = false;
    private String line;
    private Navigator navigator;
    
    public Evaluator (String email, String name , String password , Navigator navigator ){
        super(email , name , password , Role.EVALUATOR);
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

    @Override
    public void authUser(String email , String password ){
       writeToCSV.getFilePath();
       boolean isAuthenticated = writeToCSV.verifyUser(email, password , navigator);
      
       if (isAuthenticated){
           System.out.println("User authenticated successfully");
       } else {
           System.out.println("Authentication failed. Invalid email or password.");
       }
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

