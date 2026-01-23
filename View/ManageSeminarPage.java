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
            if (title == null || title.trim().isEmpty())
                return;

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

                // --- 1. Get Session Details ---
                int currentseminarID = (int) sessionModel.getValueAt(selectedSessionRow, 0);
                int sessID = (int) sessionModel.getValueAt(selectedSessionRow, 1);
                String currentType = (String) sessionModel.getValueAt(selectedSessionRow, 2);
                String currentStart = (String) sessionModel.getValueAt(selectedSessionRow, 3);
                String currentEnd = (String) sessionModel.getValueAt(selectedSessionRow, 4);

                // --- 2. Load Data ---
                Map<String, ArrayList<String>> userData = csvModel.readData();
                // Get list of Student UUIDs who submitted to this seminar
                ArrayList<String> submissionUserID = csvModel.readSubmission(currentseminarID);

                // Evaluator Data
                ArrayList<String> evalNames = userData.get("evaluatorNameList");
                ArrayList<String> evalIDs = userData.get("evaluatorIDList");

                // Student Data Containers
                ArrayList<String> studNames = new ArrayList<>();
                ArrayList<String> studIDs = new ArrayList<>();

                // --- FIX: Correctly fetch Student Names using UUIDs ---
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

                // --- 3. Select Evaluator Table ---
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

                // --- 4. Select Presenter Table (Now Populated!) ---
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