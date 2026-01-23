package Controller;

import InterfaceLib.Navigator;
import InterfaceLib.Role;
import InterfaceLib.SignUp;
import Models.WriteToCSV;


public class Coordinator extends User implements SignUp {

    private int coordinatorID;
    private String name;
    private String faculty;

    private String email;
    private String password;

    private String userType;
    private WriteToCSV writeToCSV = new WriteToCSV();
    private boolean isRegistered = false;
    private String line;

   
    private Navigator navigator;

     public Coordinator (String email, String name , String password , Navigator navigator ){
        super(email , name , password , Role.PROCOORDINATOR);
        this.navigator = navigator;
    }

    @Override
    public void registerUser(String email , String name , String password , String userType){
         
      
         writeToCSV.registerUser(this.id, email, name, password, userType);
         isRegistered = true;
         navigator.goTo("LoginPage");


    }

    @Override
    public void deleteUser(String name, String userType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    

  

    void assignEvaluators(Evaluator name, Seminar idSeminar){
        
    }

    void nominateAward(Award winner){
        
    }

    

    //testing git


    
}