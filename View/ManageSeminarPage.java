package View;

import Controller.Seminar;
import Controller.Session;
import MainFrame.MainFrame;
import Models.WriteToCSV;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ManageSeminarPage extends JPanel {

    private JTable seminarTable;
    private DefaultTableModel tableModel;
    private WriteToCSV csvModel = new WriteToCSV();
    private ArrayList<Seminar> seminarList;

    private boolean isEmpty(String value, String fieldName) {
        if (value == null) // for cancel button
            return true;

        if (value == null || value.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    fieldName + " cannot be empty");
            return true;
        } else {
            return false;
        }
    };

    private void refreshTableData() {
        seminarList = csvModel.readSeminars();
        tableModel.setRowCount(0);

        for (Seminar s : seminarList) {
            Object[] row = { s.getSeminarID(), s.getTitle(), s.getDescription(), s.getVenue(), s.getDate(),
                    s.getStartTime(), s.getEndTime() };
            tableModel.addRow(row);
        }
    }

    private boolean validateDate(String date) {
        // Basic format check: DD/MM/YYYY
        String datePattern = "^\\d{2}/\\d{2}/\\d{4}$";
        if (!date.matches(datePattern)) {
            JOptionPane.showMessageDialog(this, "Please enter the date in DD/MM/YYYY format!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Further validation can be added here (e.g., valid day/month ranges)
        return true;
    }

    // error check stuff alnafdcsaddcm
    private int toMinutes(String time) {
        if (time == null) // for cancel button
            return -1;

        time = time.trim().toUpperCase();
        String amPmAtEndPattern = "(?i)(AM|PM)$"; // Normalize: ensure space before AM/PM
        String multipleSpacesPattern = "\\s+"; // Remove extra spaces
        time = time.replaceAll(amPmAtEndPattern, " $1");
        time = time.replaceAll(multipleSpacesPattern, " ");
        time = time.trim();

        String[] parts = time.split(" ");
        if (parts.length != 2) {
            JOptionPane.showMessageDialog(this, "Include AM/PM in time input!",
                    "Error", JOptionPane.ERROR_MESSAGE);

        }

        String[] hm = parts[0].split(":");
        if (hm.length != 2) {
            String[] newHm = new String[2];
            newHm[0] = hm[0];
            newHm[1] = "00"; // asume minutes as 00 if not provided
            hm = newHm;
        }

        int hour = Integer.parseInt(hm[0]);
        int min = Integer.parseInt(hm[1]);
        String am_pm = parts[1];

        if (hour < 1 || hour > 12 || min < 0 || min > 59) {
            JOptionPane.showMessageDialog(this, "Please enter a valid time!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        if ("AM".equals(am_pm)) {
            if (hour == 12)
                hour = 0;
        } else if ("PM".equals(am_pm)) {
            if (hour != 12)
                hour += 12;
        }

        return hour * 60 + min;
    }

    private boolean overlaps(int startA, int endA, int startB, int endB) {
        // If A ends before B starts, no overlap.
        if (endA <= startB)
            return false;

        // If A starts after B already finished, no overlap.
        if (startA >= endB)
            return false;

        return true;
    }

    private boolean validateSeminar(String date, String semStart, String semEnd) {

        int start = toMinutes(semStart);
        int end = toMinutes(semEnd);

        if (start >= end) {
            JOptionPane.showMessageDialog(this,
                    "Seminar start time must be before end time");
            return false;
        }

        // go thru the csv file and check for overlapping seminars
        for (Seminar sem : seminarList) {

            // Only check for time overlap if the DATE is the same
            // Assuming date format is consistent (e.g., DD/MM/YYYY)
            if (sem.getDate().trim().equals(date.trim())) {

                int existingStart = toMinutes(sem.getStartTime());
                int existingEnd = toMinutes(sem.getEndTime());

                if (overlaps(start, end, existingStart, existingEnd)) {
                    JOptionPane.showMessageDialog(this,
                            "Seminar time overlaps with another seminar on " + date);
                    return false;
                }
            }
        }

        return true;
    }


    private boolean validateSession(int semID, String sessStart, String sessEnd) {

        int start = toMinutes(sessStart);
        int end = toMinutes(sessEnd);
         Seminar sessionSem = null;
            for (Seminar sem : seminarList) {
                if (sem.getSeminarID() == semID) {

                   if(start < toMinutes(sem.getStartTime()) || start > toMinutes(sem.getEndTime()) 
                     || end < toMinutes(sem.getStartTime()) || end > toMinutes(sem.getEndTime())) {
                       JOptionPane.showMessageDialog(this,
                               "Session time must be within the seminar time frame.");
                       return false;
                    }

                    sessionSem = sem;
                    break;
                }
            }
        Session tempSession = new Session(sessionSem);
        ArrayList<Session> sessionsList = csvModel.readSessions(tempSession);
        for (Session sess : sessionsList) {

            // Only check for time overlap if the DATE is the same
            // Assuming date format is consistent (e.g., DD/MM/YYYY)
            if (sess.getSeminarID() == semID) {

                int existingStart = toMinutes(sess.getStartTime());
                int existingEnd = toMinutes(sess.getEndTime());

                if (overlaps(start, end, existingStart, existingEnd)) {
                    JOptionPane.showMessageDialog(this,
                            "Session time overlaps with another session in the same seminar.");
                    return false;
                }


            }
        }

        return true;
    }
    

    public ManageSeminarPage(MainFrame mainFrame, String currentUserID) {
        setLayout(new BorderLayout());

        // --- Header Section ---

        JPanel topPanel = new JPanel(new BorderLayout());
        add(Box.createVerticalStrut(15));
        JLabel label = new JLabel("Manage Seminars");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(label, BorderLayout.NORTH);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel();
        JButton createButton = new JButton("Create");
        JButton sessionButton = new JButton("Sessions");
        JButton scheduleButton = new JButton("Generate Schedule");
        JButton delButton = new JButton("Delete");
        JButton backButton = new JButton("Back");

        System.out.println("Current User ID in ManageSeminarPage: " + currentUserID);

        buttonPanel.add(createButton);
        buttonPanel.add(sessionButton);
        buttonPanel.add(scheduleButton);
        buttonPanel.add(delButton);
        buttonPanel.add(backButton);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // --- Table Section ---
        String[] columnNames = { "ID", "Seminar Name", "Description", "Venue", "Date", "Start Time", "End Time" };
        tableModel = new DefaultTableModel(columnNames, 0);
        seminarTable = new JTable(tableModel);
        add(new JScrollPane(seminarTable), BorderLayout.CENTER);

        refreshTableData();

        // --- Action Listeners ---
        backButton.addActionListener(e -> mainFrame.showPage("CoordinatorDashboard"));

        createButton.addActionListener(e -> {
            String title = JOptionPane.showInputDialog(this, "Enter Seminar Title");
            if (isEmpty(title, "Title"))
                return;

            String description = JOptionPane.showInputDialog(this, "Enter Description");
            if (isEmpty(description, "Description"))
                return;

            String venue = JOptionPane.showInputDialog(this, "Enter the Venue");
            if (isEmpty(venue, "Venue"))
                return;

            String date = JOptionPane.showInputDialog(this, "Enter the date (DD/MM/YYYY)");
            if (isEmpty(date, "Date")) {
                return;
            }

            if (!validateDate(date)) {
                return;
            }
            ;

            String startTime = JOptionPane.showInputDialog(this, "Enter the start time");
            if (isEmpty(startTime, "Start Time")) {
                return;
            }

            String endTime = JOptionPane.showInputDialog(this, "Enter the end time");
            if (isEmpty(endTime, "End Time")) {
                return;
            }

            // check for overlapping seminars
            if (!validateSeminar(date, startTime, endTime)) {
                return;
            }

            int newID = (seminarList.isEmpty()) ? 1 : seminarList.get(seminarList.size() - 1).getSeminarID() + 1;

            Seminar newSem = new Seminar(newID, title);
            newSem.setDescription(description);
            newSem.setVenue(venue);
            newSem.setDate(date);
            newSem.setStartTime(startTime);
            newSem.setEndTime(endTime);

            csvModel.writeSeminar(newSem);
            refreshTableData();
            JOptionPane.showMessageDialog(this, "Seminar Created Successfully!");
        });

        scheduleButton.addActionListener(e -> {
        });

        delButton.addActionListener(e -> {
            int selectedRow = seminarTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a seminar to remove.");
                return;
            }

            seminarList.remove(selectedRow);
            csvModel.updateSeminarCSV(seminarList);
            refreshTableData();
            JOptionPane.showMessageDialog(this, "Seminar deleted Successfully!");
        });

        sessionButton.addActionListener(e -> {
            int selectedRow = seminarTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a seminar first!");
                return;
            }

            int seminarID = (int) tableModel.getValueAt(selectedRow, 0);
            String seminarTitle = (String) tableModel.getValueAt(selectedRow, 1);

            Seminar sessionSem = null;
            for (Seminar s : seminarList) {
                if (s.getSeminarID() == seminarID) {
                    sessionSem = s;
                    break;
                }
            }

            final Session tempSession = new Session(sessionSem);

            JDialog viewDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                    "Sessions for: " + seminarTitle, true);
            viewDialog.setSize(800, 400);
            viewDialog.setLayout(new BorderLayout());

            String[] columns = { "Seminar ID", "Session ID", "Type", "Start Time", "End Time", "Presenter",
                    "Presenter ID",
                    "Evaluator", "Evaluator ID" };
            DefaultTableModel sessionModel = new DefaultTableModel(columns, 0);
            JTable sessionTable = new JTable(sessionModel);
            viewDialog.add(new JScrollPane(sessionTable), BorderLayout.CENTER);

            // Define Refresh Logic
            Runnable refreshSessionList = () -> {
                sessionModel.setRowCount(0);
                ArrayList<Session> sessionsList = csvModel.readSessions(tempSession);
                for (Session sess : sessionsList) {
                    sessionModel.addRow(new Object[] {
                            sess.getSeminarID(),
                            sess.getSessionID(),
                            sess.getSessionType(),
                            sess.getStartTime(),
                            sess.getEndTime(),
                            sess.getPresenter(),
                            sess.getPresenterID(),
                            sess.getEvaluator(),
                            sess.getEvaluatorID()

                    });
                }
            };

            // Run initial refresh
            refreshSessionList.run();

            // Dialog Buttons
            JPanel dialogButtonPanel = new JPanel();
            JButton addSessionBtn = new JButton("Create Session");
            JButton assignAttendeesButton = new JButton("Assign evaluators & presenters");
            JButton delSessionBtn = new JButton("Delete Session");
            dialogButtonPanel.add(addSessionBtn);
            dialogButtonPanel.add(assignAttendeesButton);
            dialogButtonPanel.add(delSessionBtn);

            viewDialog.add(dialogButtonPanel, BorderLayout.NORTH);

            final Seminar finalSem = sessionSem; // For lambda scope

            addSessionBtn.addActionListener(ev -> {
                ArrayList<Session> currentSessList = csvModel.readSessions(tempSession);
                int newSessionID = (currentSessList.isEmpty()) ? 1
                        : currentSessList.get(currentSessList.size() - 1).getSessionID() + 1;

                String sessionType = JOptionPane.showInputDialog(viewDialog, "Enter Session Type (Oral/Poster):");
                if (sessionType == null || sessionType.trim().isEmpty())
                    return;

                String startTime = JOptionPane.showInputDialog(viewDialog, "Enter Start Time:");
                String endTime = JOptionPane.showInputDialog(viewDialog, "Enter End Time:");
                if (!validateSession(seminarID, startTime, endTime)) {
                    return;
                }



                Session newSess = new Session(finalSem, newSessionID, sessionType, startTime, endTime);
                newSess.setPresenter("TBD");
                newSess.setPresenterID("TBD");
                newSess.setEvaluator("TBD");
                newSess.setEvaluatorID("TBD");

                csvModel.writeSession(newSess, seminarID);
                refreshSessionList.run();
                JOptionPane.showMessageDialog(viewDialog, "Successfully added a session!");
            });

            delSessionBtn.addActionListener(ev -> {
                int selectedSessionRow = sessionTable.getSelectedRow();
                if (selectedSessionRow == -1) {
                    JOptionPane.showMessageDialog(viewDialog, "Please select a session first!");
                    return;
                }
                int currentseminarID = (int) sessionModel.getValueAt(selectedSessionRow, 0);
                int sessID = (int) sessionModel.getValueAt(selectedSessionRow, 1);

                csvModel.deleteSession(currentseminarID, sessID);
                refreshSessionList.run();
                JOptionPane.showMessageDialog(viewDialog, "Successfully deleted the session!");
            });

            // ====================== ASSIGN ATTENDEES LOGIC ====================== //
            assignAttendeesButton.addActionListener(ev -> {
                int selectedSessionRow = sessionTable.getSelectedRow();
                if (selectedSessionRow == -1) {
                    JOptionPane.showMessageDialog(viewDialog, "Please select a session first!");
                    return;
                }

                // --- Get Session Details ---
                int currentseminarID = (int) sessionModel.getValueAt(selectedSessionRow, 0);
                int sessID = (int) sessionModel.getValueAt(selectedSessionRow, 1);
                String currentType = (String) sessionModel.getValueAt(selectedSessionRow, 2);
                String currentStart = (String) sessionModel.getValueAt(selectedSessionRow, 3);
                String currentEnd = (String) sessionModel.getValueAt(selectedSessionRow, 4);

                // --- Load Data ---
                Map<String, ArrayList<String>> userData = csvModel.readData();
                // Get list of Student UUIDs who submitted to this seminar
                ArrayList<String> submissionUserID = csvModel.readSubmission(currentseminarID);

                // Evaluator Data
                ArrayList<String> evalNames = userData.get("evaluatorNameList");
                ArrayList<String> evalIDs = userData.get("evaluatorIDList");

                // Student Data Containers
                ArrayList<String> studNames = new ArrayList<>();
                ArrayList<String> studIDs = new ArrayList<>();

                // ---  Correctly fetch Student Names using UUIDs ---
                for (String idString : submissionUserID) {
                    try {
                        // USE THE NEW METHOD HERE: readUserByID
                        ArrayList<String> userInfo = csvModel.readUserByID(idString);

                        if (userInfo != null && !userInfo.isEmpty()) {
                            // UserInfo format: [0]=id, [1]=email, [2]=name, [3]=role
                            String stuID = userInfo.get(0);
                            String stuName = userInfo.get(2);

                            studIDs.add(stuID);
                            studNames.add(stuName);

                        } else {
                            System.out.println("User ID found in submission but not in User.csv: " + idString);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                // ---  Select Evaluator Table ---
                String[] evalColumns = { "ID", "Name" };
                DefaultTableModel evalModel = new DefaultTableModel(evalColumns, 0) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

                if (evalNames != null && evalIDs != null) {
                    for (int i = 0; i < evalNames.size(); i++) {
                        evalModel.addRow(new Object[] { evalIDs.get(i), evalNames.get(i) });
                    }
                }

                JTable evalTable = new JTable(evalModel);
                int evalResult = JOptionPane.showConfirmDialog(viewDialog, new JScrollPane(evalTable),
                        "Select Evaluator", JOptionPane.OK_CANCEL_OPTION);

                if (evalResult != JOptionPane.OK_OPTION || evalTable.getSelectedRow() == -1)
                    return;

                String selectedEvalIDStr = (String) evalModel.getValueAt(evalTable.getSelectedRow(), 0);
                String selectedEvalName = (String) evalModel.getValueAt(evalTable.getSelectedRow(), 1);

                // --- Select Presenter Table (Now Populated!) ---
                String[] stuColumns = { "ID", "Name" };
                DefaultTableModel stuModel = new DefaultTableModel(stuColumns, 0) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

                if (studNames != null && !studNames.isEmpty()) {
                    for (int i = 0; i < studNames.size(); i++) {
                        stuModel.addRow(new Object[] { studIDs.get(i), studNames.get(i) });
                    }
                } else {
                    JOptionPane.showMessageDialog(viewDialog, "No students have submitted work for this seminar yet.");
                    return;
                }

                JTable stuTable = new JTable(stuModel);
                int studResult = JOptionPane.showConfirmDialog(viewDialog, new JScrollPane(stuTable),
                        "Select Presenter (Student)", JOptionPane.OK_CANCEL_OPTION);

                if (studResult != JOptionPane.OK_OPTION || stuTable.getSelectedRow() == -1)
                    return;

                String selectedStuIDStr = (String) stuModel.getValueAt(stuTable.getSelectedRow(), 0);
                String selectedStuName = (String) stuModel.getValueAt(stuTable.getSelectedRow(), 1);

                // --- 5. Save Assignment ---
                Session updatedSession = new Session(new Seminar(currentseminarID, ""), sessID, currentType,
                        currentStart, currentEnd);
                updatedSession.setEvaluator(selectedEvalName);
                updatedSession.setEvaluatorID(selectedEvalIDStr);
                updatedSession.setPresenter(selectedStuName);
                updatedSession.setPresenterID(selectedStuIDStr);

                csvModel.updateSession(updatedSession, currentseminarID);

                refreshSessionList.run();
                JOptionPane.showMessageDialog(viewDialog, "Assignment Saved!");
            });

            viewDialog.setLocationRelativeTo(this);
            viewDialog.setVisible(true);
        });
    }
}