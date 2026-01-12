package Controller;

import java.util.ArrayList;
import java.util.List;

public class Session {
    
    private Seminar seminar;
    
    private String sessionID;
    private String startTime;
    private String endTime;
    private int sessionType;

    private List<Evaluator> evaluators = new ArrayList<>();
    private List<Student> presenters = new ArrayList<>();

    // this creates a
    public Session(String sID, String sTime, String eTime) {
        this.sessionID = sID;
        this.startTime = sTime;
        this.endTime = eTime;
    };

    public Seminar getSeminar() {
        return seminar;
    }

    public void setSeminar(Seminar s) {
        this.seminar = s;
    }

     public void setStartTime(String start) {
        this.startTime = start;
    }

     public void setEndTime(String end) {
        this.endTime = end;
    }

     public String getStartTime() {
        return this.startTime;
    }

     public String getEndTime() {
        return this.endTime;
    }

    public void addEvaluator(Evaluator e) {

        this.evaluators.add(e);
    }

    public void addPresenter(Student s) {

        this.presenters.add(s);
    }

    // Getters for the lists
    public List<Student> getPresenters() {
        return presenters;
    }

    public List<Evaluator> getEvaluators() {
        return evaluators;
    }

    // removers
    public void removePresenter(Student student) {
        if (student != null) {
            presenters.remove(student);
        }
    }

    public void removeEvaluator(Evaluator evaluator) {
        if (evaluator != null) {
            evaluators.remove(evaluator);
        }
    }

}
