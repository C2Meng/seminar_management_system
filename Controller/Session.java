package Controller;

import java.util.ArrayList;
import java.util.List;

public class Session {

    private String sessionID;
    private int startTime;
    private int endTime;

    private Seminar seminar;
     private List<Evaluator> evaluators = new ArrayList<>();
    private List<Student> presenters = new ArrayList<>();
    //this creates a
    public Session(String sID, int sTime, int eTime){
        this.sessionID = sID;
        this.startTime = sTime;
        this.endTime = eTime;
    };

    public Seminar getSeminar(){
        return seminar;
    }

    public void setSeminar(Seminar seminar){
        this.seminar = seminar;
    }
    
    public void addEvaluator(Evaluator evaluator){

        this.evaluators.add(evaluator);
    }

    public void addPresenter(Student student){

        this.presenters.add(student);
    }

    // Getters for the lists 
    public List<Student> getPresenters() {
        return presenters;
    }

    public List<Evaluator> getEvaluators() {
        return evaluators;
    }

    //to add removers later
}
