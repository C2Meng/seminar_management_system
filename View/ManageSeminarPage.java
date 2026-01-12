package View;

import Controller.Seminar;
import Controller.Session;
import MainFrame.MainFrame;
import Models.WriteToCSV;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ManageSeminarPage extends JPanel {

        private JTable seminarTable;
        private DefaultTableModel tableModel;
        private WriteToCSV csvModel = new WriteToCSV();
        // list of seminars
        private ArrayList<Seminar> seminarList;

        private void refreshTableData() {
                seminarList = csvModel.readSeminars(); // Read latest from file
                tableModel.setRowCount(0); // Clear table

                for (Seminar s : seminarList) {
                        // Add row to table model
                        Object[] row = { s.getSeminarID(), s.getTitle(), s.getDescription(), s.getVenue(), s.getDate(),
                                        s.getStartTime(), s.getEndTime() };
                        tableModel.addRow(row);
                }

        }

        public ManageSeminarPage(MainFrame mainFrame) {
                setLayout(new BorderLayout());
                JLabel label = new JLabel("Manage Seminars");
                label.setHorizontalAlignment(SwingConstants.CENTER);
                add(label, BorderLayout.NORTH);

                String[] columnNames = { "ID", "Seminar Name", "Description", "Venue", "Date", "Start Time",
                                "End Time" };
                tableModel = new DefaultTableModel(columnNames, 0);
                seminarTable = new JTable(tableModel);

                add(new JScrollPane(seminarTable), BorderLayout.CENTER);

                // refreshes table
                refreshTableData();

                // a jpanel to hold multiple buttons
                JPanel buttonPanel = new JPanel();
                JButton createButton = new JButton("Create");
                JButton sessionButton = new JButton("Sessions");
                JButton delButton = new JButton("Delete");
                JButton backButton = new JButton("Back");

                buttonPanel.add(createButton);
                buttonPanel.add(sessionButton);
                buttonPanel.add(delButton);
                buttonPanel.add(backButton);
                add(buttonPanel, BorderLayout.NORTH);

                // back button redirects PC to coordinator dashboard
                backButton.addActionListener(e -> mainFrame.showPage("CoordinatorDashboard"));

                createButton.addActionListener(e -> {

                        // JOptionPane.showInputDialog: Pops up a small text box for the user to type
                        // into.
                        String title = JOptionPane.showInputDialog(this, "Enter Seminar Title");
                        if (title == null || title.trim().isEmpty())
                                return;

                        String description = JOptionPane.showInputDialog(this, "Enter Description");
                        // add desc word count limit
                        String venue = JOptionPane.showInputDialog(this, "Enter the Venue");

                        String date = JOptionPane.showInputDialog(this, "Enter the date of the seminar");

                        String startTime = JOptionPane.showInputDialog(this, "Enter the start time");

                        String endTime = JOptionPane.showInputDialog(this, "Enter the end time");

                        int newID = seminarList.size() + 1; // id generation

                        // create the seminar object from seminar.java
                        Seminar newSem = new Seminar(newID, title);
                        newSem.setDescription(description);
                        newSem.setTitle(title);
                        newSem.setVenue(venue);
                        newSem.setDate(date);
                        newSem.setStartTime(startTime);
                        newSem.setEndTime(endTime);

                        csvModel.writeSeminar(newSem); // write to csv file

                        refreshTableData();

                        JOptionPane.showMessageDialog(this, "Seminar Created Successfully!");

                });

                delButton.addActionListener(e -> {
                        int selectedRow = seminarTable.getSelectedRow();
                        if (selectedRow == -1) {
                                JOptionPane.showMessageDialog(this, "Please select a seminar to remove.");
                        }

                        seminarList.remove(selectedRow);
                        csvModel.updateSeminarCSV(seminarList);
                        refreshTableData();
                        JOptionPane.showMessageDialog(this, "Seminar deleted Successfully!");

                });

                // Session management
                sessionButton.addActionListener(e -> {

                        int selectedRow = seminarTable.getSelectedRow();
                        if (selectedRow == -1) {
                                JOptionPane.showMessageDialog(this, "Please select a seminar first!");
                                return;
                        }

                        // Get the ID of the selected seminar
                        int seminarID = (int) tableModel.getValueAt(selectedRow, 0);
                        String seminarTitle = (String) tableModel.getValueAt(selectedRow, 1);

                        // Session JDialog //this is ai generated
                        JDialog viewDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                                        "Sessions for: " + seminarTitle, true);
                        viewDialog.setSize(800, 800);
                        viewDialog.setLayout(new BorderLayout());

                        // Table for Sessions
                        String[] columns = { "Session ID", "Start Time", "End Time", "Presenters", "Evaluators" };
                        DefaultTableModel sessionModel = new DefaultTableModel(columns, 0);
                        JTable sessionTable = new JTable(sessionModel);
                        viewDialog.add(new JScrollPane(sessionTable), BorderLayout.CENTER);

                        // buttons
                        JPanel dialogbuttonPanel = new JPanel();
                        JButton addSessionButton = new JButton("Create Sessions");
                        JButton assignEvaluatorButton = new JButton("Assign Evaluator");
                        JButton delSessionButton = new JButton("Delete Session");

                        dialogbuttonPanel.add(addSessionButton);
                        dialogbuttonPanel.add(assignEvaluatorButton);
                        dialogbuttonPanel.add(delSessionButton);

                        addSessionButton.addActionListener(ev -> {
                                
                                String sessionType = JOptionPane.showInputDialog(this, "sessionType");
                                if (sessionType == null || sessionType.trim().isEmpty())
                                return;
                                String startTime = JOptionPane.showInputDialog(this, "startTime");
                                String endTime = JOptionPane.showInputDialog(this, "endTime");

                                //create session class
                                //set session type
                                //set session start time
                                //set session end time

                                //write to csv
                                //refresh
                                JOptionPane.showMessageDialog(this,"Successfully added a session!");

                        });

                        assignEvaluatorButton.addActionListener(ev -> {

                                int selectedSessionRow = sessionTable.getSelectedRow();
                                if (selectedSessionRow == -1) {
                                        JOptionPane.showMessageDialog(this, "Please select a session first!");
                                        return;
                                }

                                //get session id
                                //set evaluator to session

                                //write to csv
                                //refresh
                                JOptionPane.showMessageDialog(this,"Assigned evaluator to session!");

                        });

                        delSessionButton.addActionListener(ev -> {

                                int selectedSessionRow = sessionTable.getSelectedRow();
                                if (selectedSessionRow == -1) {
                                        JOptionPane.showMessageDialog(this, "Please select a session first!");
                                        return;
                                }

                                //get session id
                                //delete session

                                //write to csv
                                //refresh

                                JOptionPane.showMessageDialog(this,"Successfully deleted session!");

                        });

                        viewDialog.add(dialogbuttonPanel, BorderLayout.NORTH);

                        viewDialog.setLocationRelativeTo(this);
                        viewDialog.setVisible(true);
                });

        }

}
