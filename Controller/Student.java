package Controller;

import InterfaceLib.SignUp;
import Models.WriteToCSV;



public class Student implements SignUp{

    private String email;
    private String password;
    private String name;
    private String userType;
    private WriteToCSV writeToCSV = new WriteToCSV();
    private boolean isRegistered = false;
    private String line;

    
    public Student (String email, String password , String name , String userType ){
        this.email = email;
        this.password = password;
        this.name = name;
        this.userType = userType;
    }


    @Override
    public void registerUser(String name , String password , String email , String userType){
         
         writeToCSV.getFilePath();
         line = name + "," + password + "," + email + "," + userType;
         isRegistered = true;
         writeToCSV.writeData(line);

    }

    @Override
    public void deleteUser(String username , String userType){
          // Implementation for deleting a user //
    }
}


