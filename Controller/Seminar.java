
package Controller;
import java.util.ArrayList;
import java.util.List;


public class Seminar {


    private int seminarID;
    private static int nextID = 1;
    private String title;
    private String description;
    private String presenterName;
    private String sessionType;
    private ArrayList<Seminar> seminarsList = new ArrayList<>();
    private ArrayList<Session> sessionsList;
    
  
   
    //create a seminar first, then PC have to fill in the rest of the details
    public Seminar(int id, String title){
        this.seminarID = id;
        this.title = title;
        this.sessionsList = new ArrayList<>();
        seminarsList.add(this);
        
        
    }


    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }

    public void setPresenter(String presenter){
        this.presenterName = presenter;
    }
    
    public String getPresenter(){
        return this.presenterName;
    }

    public void setDescription(String descrip){
        this.description = descrip;
    }
    
    public String getDescription(){
        return this.description;
    }

    public void setSessionType(String sessionType){
        this.sessionType = sessionType;
    }

    public String getSession(){
        return this.sessionType;
    }

   
    public void addSession(Session session) {
        session.setSeminar(this); // Link the session back to this seminar
        this.sessionsList.add(session); // Add session to the seminar's session list
    }

    

    //to add removers 
}
