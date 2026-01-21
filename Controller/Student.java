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
    private String filePath = "Data/Submission.csv";
    

    public Student (String email, String name , String password , Navigator navigator ){
        super(email , name , password , Role.STUDENT);
        this.email = email;
        this.navigator = navigator;
    }


    public String setEmail(String email){
        this.email = email;
        return email;
    }

    public String getEmail(){
        return this.email;
    }

    public String setPassword(String password){
        this.password = password;
        return password;
    }

    public String getPassword(){
        return this.password;
    }

    public String setName( String name){
        this.name = name;
        return name;
    }

    public String getName(){
        return this.name;
    }

    public String setUserType(String userType){
        this.userType = userType;
        return userType;
    }

    

    public String getUserType(){
        return this.userType;
    }

    public String setSeminarId(String seminarId){
        this.seminarId = seminarId;
        return seminarId;
    }

    public String getSeminarId(){
        return this.seminarId;
    }

    public String setTitle(String title){
        this.title = title;
        return title;
    }

    public String getTitle(){
        return this.title;
    }


    public String setAbstractText(String abstractText){
        this.abstractText = abstractText;
        return abstractText;
    }

    public String getAbstractText(){
        return this.abstractText;
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
 
    public void registerForSeminar(String submissionID, String seminarId , String currentUserId ,  String title , String abstractText , String attachment , String supervisor , String presentationType){
        // Seminar registration logic for Student //
        writeToCSV.setFilePath(filePath);
        String line = submissionID  + "," + seminarId + "," + currentUserId + "," + title + "," + abstractText + "," + attachment + "," + supervisor + "," + presentationType;
        writeToCSV.writeData(line);

    }


}

