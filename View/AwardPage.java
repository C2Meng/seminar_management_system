package View;

import Controller.Seminar;
import Controller.Session;
import MainFrame.MainFrame;
import Models.WriteToCSV;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
;

public class AwardPage extends JPanel {

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

   public AwardPage(MainFrame mainFrame , String currentUserID) {

      setLayout(new BorderLayout());

      // --- Header Section ---

      JPanel topPanel = new JPanel(new BorderLayout());
      add(Box.createVerticalStrut(15));
      JLabel label = new JLabel("Awards Page");
      label.setFont(new Font("Arial", Font.BOLD, 16));
      label.setHorizontalAlignment(SwingConstants.CENTER);
      topPanel.add(label, BorderLayout.NORTH);

      // --- Button Panel ---
      JPanel buttonPanel = new JPanel();
      JButton awardButton = new JButton("Give Award");
      JButton viewButton = new JButton("View Award");
      JButton backButton = new JButton("Back");

      buttonPanel.add(awardButton);
      buttonPanel.add(viewButton); // view award wil be like BEST ORAL: SESSION NUM || PRESENTER NAME
      buttonPanel.add(backButton);
      topPanel.add(buttonPanel, BorderLayout.SOUTH);

      add(topPanel, BorderLayout.NORTH);

      // --- Table Section ---
      String[] columnNames = { "ID", "Seminar Name", "Description", "Venue", "Date", "Start Time", "End Time" };
      tableModel = new DefaultTableModel(columnNames, 0);
      seminarTable = new JTable(tableModel);
      add(new JScrollPane(seminarTable), BorderLayout.CENTER);


      refreshTableData();

      awardButton.addActionListener(e -> {

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

         String[] columns = { "Seminar ID", "Session ID", "Type", "Start Time", "End Time", "Evaluator",
               "Presenter" };
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
                     sess.getPresenter() // Now reads from correct column
               });
            }
         };

         // Run initial refresh
         refreshSessionList.run();

         // Dialog Buttons
         JPanel dialogButtonPanel = new JPanel();
         JButton inspectScoreBtn = new JButton("Inspect Scores");
         JButton assignAwardBtn = new JButton("Assign Award");
         JButton removeAwardBtn = new JButton("Remove Award");

         dialogButtonPanel.add(inspectScoreBtn);
         dialogButtonPanel.add(assignAwardBtn);
         dialogButtonPanel.add(removeAwardBtn);


         viewDialog.add(dialogButtonPanel, BorderLayout.NORTH);


         inspectScoreBtn.addActionListener(ev -> {
            int sessRow = sessionTable.getSelectedRow();
            if (sessRow == -1) {
               JOptionPane.showMessageDialog(viewDialog, "Please select a session first!");
               return;
            }

            int sessionID = (int) sessionModel.getValueAt(sessRow, 1);
            int seminarIDInt = (int) sessionModel.getValueAt(sessRow, 0);

             csvModel.readEvaluationScores(String.valueOf(seminarIDInt), String.valueOf(sessionID));


         });

        

         viewDialog.setLocationRelativeTo(this);
         viewDialog.setVisible(true);

         
      });

      viewButton.addActionListener(e -> {});

      backButton.addActionListener(e -> {
         mainFrame.showPage("CoordinatorDashboard");
      });

   }

}