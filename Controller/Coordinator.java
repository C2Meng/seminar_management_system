package Controller;

import InterfaceLib.Navigator;
import InterfaceLib.Role;

public class Coordinator extends User {

    private int coordinatorID;
    private String name;
    private String faculty;
   
    private Navigator navigator;

     public Coordinator (String email, String name , String password , Navigator navigator ){
        super(email , name , password , Role.PROCOORDINATOR);
        this.navigator = navigator;
    }

    Seminar createSeminar(String title){
        return null;

    }

    void assignEvaluators(Evaluator name, Seminar title){

    }

    void nominateAward(Award winner){
        
    }

    


    
}