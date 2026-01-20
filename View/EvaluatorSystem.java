package View;

import Controller.Evaluator;
import Controller.Submission;
import MainFrame.MainFrame;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class EvaluatorSystem extends JFrame {

    //holds a list of submissions
    private List<Submission> submissions = new ArrayList<>();
    private JTable table;
    private DefaultTableModel tableModel;

    private Evaluator controller; // evaluator controller instance
    // --- Main Dashboard UI ---
    public EvaluatorSystem() {
        controller = new Evaluator();
        setTitle("Seminar Evaluator Portal");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load Data from controller
        submissions = controller.loadSubmissions();

        //DefaultTableModel from >> https://docs.oracle.com/javase/8/docs/api/javax/swing/table/DefaultTableModel.html
        String[] columnNames = {"Student Email", "Seminar ID", "Title", "Abstract", "FilePath", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0)
        {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } //if removed, cells become editable
        };
        
        updateTableData();

        table = new JTable(tableModel);
        table.setRowHeight(30);
        
        // Custom Renderer to color the Status column
       // table.getColumnModel().getColumn(6).setCellRenderer(new StatusRenderer());

        JScrollPane scrollPane = new JScrollPane(table);


        // added the logout button to bring back to login page 
        JButton logOutButton = new JButton("Logout");
        logOutButton.setFont(new Font("Arial" , Font.BOLD , 14));
        logOutButton.addActionListener(e ->{
           MainFrame mainFrame = new MainFrame();
           mainFrame.setVisible(true);
           this.dispose();
        });

        JButton btnGrade = new JButton("Grade Selected Student");
        btnGrade.setFont(new Font("Arial", Font.BOLD, 14));
        btnGrade.addActionListener(e -> openGradingWindow());

       

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(logOutButton);
        bottomPanel.add(btnGrade);
        

        // Laying the out 
        add(new JLabel("  My Assignments (Evaluator View)"), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    //Code Segment to Read CSV file and fill the students list, this is now in Controller/Evaluator.java
    // private void loadCSV(String filename) {
    //     File file = new File(filename);
    //     System.out.println("Loading CSV from: " + file.getAbsolutePath());
    //     try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
    //         String line;
    //         while ((line = br.readLine()) != null) {
    //             String[] values = line.split(",");
    //             submissions.add(new Submission(values));
    //         }
    //     } catch (IOException e) {
    //         JOptionPane.showMessageDialog(this, "Error loading CSV: " + e.getMessage());
    //     }
    // }

    private void updateTableData() {
        tableModel.setRowCount(0); // Clear existing
        for (Submission s : submissions) {
            tableModel.addRow(new Object[]{
                s.submissionId, s.studentName, s.title, s.type, s.evaluatorId, s.status
            });
        }
    }


    private void openGradingWindow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to grade.");
            return;
        }

        Submission submission = submissions.get(selectedRow);
        new GradingDialog(this, submission,controller).setVisible(true);
    }

    // --- Grading Window (Inner Class) ---
class GradingDialog extends JDialog {
        private Submission submission;
        private Evaluator controller;
        // Added sliderClarity
        private JSlider sliderClarity, sliderMethodology, sliderResults, sliderPresentation;
        private JLabel lblTotal;
        private JTextArea txtComments;

        public GradingDialog(JFrame parent, Submission s,Evaluator evaluator) {
            super(parent, "Grading: " + s.studentName, true);
            this.controller = evaluator;
            this.submission = s;
            setSize(500, 700); // Increased height slightly
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout(10, 10));

            // --- Header Info ---
            JPanel pnlInfo = new JPanel(new GridLayout(4, 1));
            pnlInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            pnlInfo.setBackground(new Color(240, 240, 240));
            pnlInfo.add(new JLabel("Student: " + s.studentName + " (" + s.submissionId + ")"));
            pnlInfo.add(new JLabel("Title: " + s.title));
            pnlInfo.add(new JLabel("Abstract: " + s.type));            
            
            // --- Rubric Form ---
            // Increased rows to 10 to fit the new slider
            JPanel pnlForm = new JPanel(new GridLayout(10, 1, 5, 5));
            pnlForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Initialize Sliders (Adjust MAX values as needed, Total should be 100)
            sliderClarity = createSlider("Problem Clarity (0-10)", 10, s.scoreClarity);
            sliderMethodology = createSlider("Methodology (0-30)", 30, s.scoreMethodology);
            sliderResults = createSlider("Results & Analysis (0-40)", 40, s.scoreResults);
            sliderPresentation = createSlider("Presentation Skills (0-20)", 20, s.scorePresentation);

            // Add to Panel
            pnlForm.add(new JLabel("Problem Clarity (10%):"));
            pnlForm.add(sliderClarity);
            
            pnlForm.add(new JLabel("Methodology (30%):"));
            pnlForm.add(sliderMethodology);
            
            pnlForm.add(new JLabel("Results & Analysis (40%):"));
            pnlForm.add(sliderResults);
            
            pnlForm.add(new JLabel("Presentation (20%):"));
            pnlForm.add(sliderPresentation);
            
            pnlForm.add(new JLabel("Evaluator Comments:"));
            txtComments = new JTextArea(3, 20);
            txtComments.setLineWrap(true);
            txtComments.setText(s.comment.equals("N/A") ? "" : s.comment);
            pnlForm.add(new JScrollPane(txtComments));

            // --- Footer / Actions ---
            JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            lblTotal = new JLabel("Total Score: " + s.getTotalScore() + "/100  ");
            lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
            
            JButton btnSave = new JButton("Submit Evaluation");
            btnSave.addActionListener(e -> saveGrade());

            pnlFooter.add(lblTotal);
            pnlFooter.add(btnSave);

            add(pnlInfo, BorderLayout.NORTH);
            add(pnlForm, BorderLayout.CENTER);
            add(pnlFooter, BorderLayout.SOUTH);
        }

        private JSlider createSlider(String title, int max, int currentVal) {
            JSlider slider = new JSlider(0, max, currentVal);
            // Dynamic ticks based on max value
            slider.setMajorTickSpacing(max / 5 > 0 ? max / 5 : 1); 
            slider.setMinorTickSpacing(1);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.addChangeListener(e -> updateTotal());
            return slider;
        }

        private void updateTotal() {
            int total = sliderClarity.getValue() + 
                        sliderMethodology.getValue() + 
                        sliderResults.getValue() + 
                        sliderPresentation.getValue();
            lblTotal.setText("Total Score: " + total + "/100  ");
        }

        private void saveGrade() {
            // get the scores and comments from UI
            submission.scoreClarity = sliderClarity.getValue();
            submission.scoreMethodology = sliderMethodology.getValue();
            submission.scoreResults = sliderResults.getValue();
            submission.scorePresentation = sliderPresentation.getValue();
            
            //sanitize comments to avoid CSV issues
            submission.comment = txtComments.getText().replace("\n", " ").replace(",", ";"); 
            submission.status = "Graded";

            // pass the submission object to controller to save

            controller.saveGrade(submission);
            // saveEvaluationToCSV(submission);
            // updateSubmissionStatusInCSV(submission.submissionId, "Graded");

            // 3. Update UI
            ((EvaluatorSystem) getParent()).updateTableData();
            System.out.println("Saved Grade for " + submission.studentName);
            dispose();
        }



    // --- Custom Renderer for Colors ---
    // class StatusRenderer extends DefaultTableCellRenderer {
    //     @Override
    //     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
    //         Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
    //         String status = (String) value;
    //         if ("Graded".equals(status)) {
    //             c.setForeground(new Color(0, 128, 0)); // Dark Green
    //             c.setFont(c.getFont().deriveFont(Font.BOLD));
    //         } else {
    //             c.setForeground(Color.RED);
    //         }
    //         return c;
    //     }
    // }
    }

    public static void main(String[] args) {
        
        new EvaluatorSystem().setVisible(true);
    }
}