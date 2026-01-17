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

    private void refreshTableData() {
        seminarList = csvModel.readSeminars();
        tableModel.setRowCount(0);

        for (Seminar s : seminarList) {
            Object[] row = { s.getSeminarID(), s.getTitle(), s.getDescription(), s.getVenue(), s.getDate(),
                    s.getStartTime(), s.getEndTime() };
            tableModel.addRow(row);
        }
    }

    public ManageSeminarPage(MainFrame mainFrame) {
        setLayout(new BorderLayout());

        // --- Header Section ---
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Manage Seminars");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(label, BorderLayout.NORTH);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel();
        JButton createButton = new JButton("Create");
        JButton sessionButton = new JButton("Sessions");
        JButton delButton = new JButton("Delete");
        JButton backButton = new JButton("Back");

        buttonPanel.add(createButton);
        buttonPanel.add(sessionButton);
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
            if (title == null || title.trim().isEmpty()) return;

            String description = JOptionPane.showInputDialog(this, "Enter Description");
            String venue = JOptionPane.showInputDialog(this, "Enter the Venue");
            String date = JOptionPane.showInputDialog(this, "Enter the date (DD/MM/YYYY)");
            String startTime = JOptionPane.showInputDialog(this, "Enter the start time");
            String endTime = JOptionPane.showInputDialog(this, "Enter the end time");

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

            String[] columns = { "Seminar ID","Session ID", "Type", "Start Time", "End Time", "Evaluator", "Presenter" };
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
                            sess.getEvaluator(), // Now reads from correct column
                            sess.getPresenter()  // Now reads from correct column
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
                int newSessionID = (currentSessList.isEmpty()) ? 1 : currentSessList.get(currentSessList.size()-1).getSessionID() + 1;

                String sessionType = JOptionPane.showInputDialog(viewDialog, "Enter Session Type (Oral/Poster):");
                if (sessionType == null || sessionType.trim().isEmpty()) return;

                String startTime = JOptionPane.showInputDialog(viewDialog, "Enter Start Time:");
                String endTime = JOptionPane.showInputDialog(viewDialog, "Enter End Time:");

                Session newSess = new Session(finalSem, newSessionID, sessionType, startTime, endTime);
                newSess.setPresenter("TBD");
                newSess.setEvaluator("TBD");

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
                
                // Get User Data
                Map<String, ArrayList<String>> userData = csvModel.readData();
                ArrayList<String> evaluators = userData.get("evaluatorNameList");
                ArrayList<String> students = userData.get("studentNameList");

                // 1. Get existing details from the table so we don't lose them during update
                int currentseminarID = (int) sessionModel.getValueAt(selectedSessionRow, 0);
                int sessID = (int) sessionModel.getValueAt(selectedSessionRow, 1);
                String currentType = (String) sessionModel.getValueAt(selectedSessionRow, 2);
                String currentStart = (String) sessionModel.getValueAt(selectedSessionRow, 3);
                String currentEnd = (String) sessionModel.getValueAt(selectedSessionRow, 4);

                // 2. Select Evaluator
                String[] evalColumns = { "Evaluators" };
                DefaultTableModel evalModel = new DefaultTableModel(evalColumns, 0);
                for (String name : evaluators) evalModel.addRow(new Object[] { name });
                JTable evalTable = new JTable(evalModel);

                int evalResult = JOptionPane.showConfirmDialog(viewDialog, new JScrollPane(evalTable),
                        "Select Evaluator", JOptionPane.OK_CANCEL_OPTION);
                if (evalResult != JOptionPane.OK_OPTION || evalTable.getSelectedRow() == -1) return;
                String selectedEval = (String) evalModel.getValueAt(evalTable.getSelectedRow(), 0);

                // 3. Select Presenter
                String[] stuColumns = { "Students" };
                DefaultTableModel stuModel = new DefaultTableModel(stuColumns, 0);
                for (String name : students) stuModel.addRow(new Object[] { name });
                JTable stuTable = new JTable(stuModel);

                int studResult = JOptionPane.showConfirmDialog(viewDialog, new JScrollPane(stuTable),
                        "Select Presenter (Student)", JOptionPane.OK_CANCEL_OPTION);
                if (studResult != JOptionPane.OK_OPTION || stuTable.getSelectedRow() == -1) return;
                String selectedStu = (String) stuModel.getValueAt(stuTable.getSelectedRow(), 0);

                // 4. Create a complete Session object with the new assignments
               Session updatedSession = new Session(new Seminar(currentseminarID, ""), sessID, currentType, currentStart, currentEnd);
                updatedSession.setEvaluator(selectedEval);
                updatedSession.setPresenter(selectedStu);

                // 5. Save to CSV
                csvModel.updateSession(updatedSession, currentseminarID);

                // 6. Refresh UI
                refreshSessionList.run(); 
                JOptionPane.showMessageDialog(viewDialog, "Assignment Saved!");
            });

            viewDialog.setLocationRelativeTo(this);
            viewDialog.setVisible(true);
        });
    }
}