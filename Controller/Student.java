package Controller;

import InterfaceLib.Navigator;
import InterfaceLib.Role;
import InterfaceLib.SignUp;
import Models.WriteToCSV;
import java.io.File;



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
         
      
         writeToCSV.registerUser(this.id, email, name, password, userType);
         isRegistered = true;
         navigator.goTo("LoginPage");

    }

    @Override
    public void deleteUser(String email , String password ){
       // Authentication logic for Student //
    }


   public void registerSeminar(String submissionID, String seminarId,String studentID , String title, String abstractText, String attachment, String supervisor, String presentationType, String graded) {
    // Pass everything to the Model

    // 1. Define the internal storage path
    File destinatorDir = new File("Data/Attachments/" + submissionID);
    if (!destinatorDir.exists()) {
        destinatorDir.mkdirs();
    }

    File sourceFile = new File(attachment);
    // Use the original filename to keep the extension (.pdf)
    File finalDestFile = new File(destinatorDir, sourceFile.getName());

    try {
        // 2. Perform the actual physical copy
        if (sourceFile.exists()) {
            java.nio.file.Files.copy(
                sourceFile.toPath(), 
                finalDestFile.toPath(), 
                java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );
        }
    } catch (java.io.IOException e) {
        System.out.println("File upload failed: " + e.getMessage());
    }

    writeToCSV.registerSeminar(submissionID, seminarId, studentID, title, abstractText, attachment, supervisor, presentationType, graded , finalDestFile.getAbsolutePath());
    
    // Redirect if needed (ensure "StudentDashboard" matches your MainFrame routing)
    if (navigator != null) {
        navigator.goTo("StudentDashboard");
    }
}



 // =================================== method to register for seminar =========================================== //
 



}

