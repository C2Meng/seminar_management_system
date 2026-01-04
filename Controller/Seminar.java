package Controller;

public class Seminar {


    private int seminarID;
    private static int nextID = 1;
    private String title;
    private String description;
    private String presenterName;
    private String sessionType;

  
    public Seminar(String title){
        this.title = title;
        //should assign id here, thinking abt keeping track of seminars in a dataset
    }

    
    public Seminar(int id, String title){
        this.seminarID = id;
        this.title = title;
        
    }

    public Seminar(String title, String presenterName){
        this.title = title;
        this.presenterName = presenterName;
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

    public void setSession(String sessionType){
        this.sessionType = sessionType;
    }

    public String getSession(){
        return this.sessionType;
    }
}
