
package Controller;

import MainFrame.MainFrame;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;

public class Seminar {

    private int seminarID;
    private String title;
    private String description;
    private String presenterName;
    private String sessionType;
    private String venue;
    private String startTime;
    private String endTime;
    private String date;
    private ArrayList<Seminar> seminarsList = new ArrayList<>();
    private ArrayList<Session> sessionsList;

    // create a seminar first, then PC have to fill in the rest of the details

    public Seminar(String id, String title) {
        this.seminarID = Integer.parseInt(id);
        this.title = title;
    }

    public Seminar(int id, String title) {
        this.seminarID = id;
        this.title = title;
        this.sessionsList = new ArrayList<>();

    }

    @Override
    public String toString() {
        return title + (" (ID: " + seminarID + ")");
    }

    public int getSeminarID() {
        return this.seminarID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setPresenter(String presenter) {
        this.presenterName = presenter;
    }

    public String getPresenter() {
        return this.presenterName;
    }

    public void setDescription(String descrip) {
        this.description = descrip;
    }

    public String getDescription() {
        return this.description;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public void setVenue(String v) {
        this.venue = v;
    }

    public String getVenue() {
        return this.venue;
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

    public void setDate(String d) {
        this.date = d;
    }

    public String getDate() {
        return this.date;
    }

    public String getSession() {
        return this.sessionType;
    }

    public ArrayList<Session> getSessionList() {
        return this.sessionsList;
    }

    public void clearSessions() {
        this.sessionsList.clear();
    }

    public void addSession(Session session) {
        session.setSeminar(this);
        if (!this.sessionsList.contains(session)) {
            this.sessionsList.add(session);
        }
    }

    // to add removers
    // Remove seminar by Seminar object
    public void removeSeminar(Seminar seminar) {
        if (seminar == null)
            return;
        seminarsList.remove(seminar);
    }

    // Remove seminar by seminarID
    public void removeSeminarByID(int seminarID) {
        for (int i = 0; i < seminarsList.size(); i++) {
            if (seminarsList.get(i).seminarID == seminarID) {
                seminarsList.remove(i);
                return; // stop after first match
            }
        }
    }

    // Remove session from this seminar
    public void removeSession(Session session) {
        if (session == null)
            return;

        if (sessionsList.remove(session)) {
            session.setSeminar(this); // break bidirectional link
        }
    }

    public void exportReport(MainFrame mainFrame) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("seminar_report.txt")); // default file name
        int userSelection = fileChooser.showSaveDialog(mainFrame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            System.out.println("Save as file: " + fileToSave.getAbsolutePath());
        } else {
            System.out.println("Save command cancelled by user.");
        }

    }

}
