package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EvaluatorSystem extends JFrame {

    // --- Data Model (should follow class diagram, subject to change) ---
    static class Student {
        String id;
        String name;
        String title;
        String type; // Oral or Poster
        String session;

        //---Rubrics Start-----
        int scoreMethodology;
        int scoreResults;
        int scorePresentation;
        String comment;           
        //--Rubrics End--       
        String status; // Pending or Graded

        public Student(String[] data) {
            this.id = data[0];
            this.name = data[1];
            this.title = data[2];
            this.type = data[3];
            this.session = data[4];
            this.scoreMethodology = Integer.parseInt(data[5]);
            this.scoreResults = Integer.parseInt(data[6]);
            this.scorePresentation = Integer.parseInt(data[7]);
            this.comment = data[8];
            this.status = data[9];
        }
        
        public int getTotalScore() {
            return scoreMethodology + scoreResults + scorePresentation;
        }
    }

    //holds a list of students
    private List<Student> students = new ArrayList<>();
    private JTable table;
    private DefaultTableModel tableModel;

    // --- Main Dashboard UI ---
    public EvaluatorSystem() {
        setTitle("Seminar Evaluator Portal");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load Data
        loadCSV("examplestudentdata.csv");

        //DefaultTableModel from >> https://docs.oracle.com/javase/8/docs/api/javax/swing/table/DefaultTableModel.html
        String[] columnNames = {"ID", "Student Name", "Title", "Type", "Session/Board", "Total Score", "Status"};
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


        JButton btnGrade = new JButton("Grade Selected Student");
        btnGrade.setFont(new Font("Arial", Font.BOLD, 14));
        btnGrade.addActionListener(e -> openGradingWindow());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnGrade);

        // Laying the out 
        add(new JLabel("  My Assignments (Evaluator View)"), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    //Code Segment to Read CSV file and fill the students list
    private void loadCSV(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if(values.length >= 10) {
                    students.add(new Student(values));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading CSV: " + e.getMessage());
        }
    }

    private void updateTableData() {
        tableModel.setRowCount(0); // Clear existing
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                s.id, s.name, s.title, s.type, s.session, 
                s.status.equals("Pending") ? "-" : s.getTotalScore(), 
                s.status
            });
        }
    }


    private void openGradingWindow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to grade.");
            return;
        }

        Student student = students.get(selectedRow);
        new GradingDialog(this, student).setVisible(true);
    }

    // --- Grading Window (Inner Class) ---
    class GradingDialog extends JDialog {
        private Student student;
        private JSlider sliderMethodology, sliderResults, sliderPresentation;
        private JLabel lblTotal;
        private JTextArea txtComments;

        public GradingDialog(JFrame parent, Student s) {
            //modal:true means you must finish the popout window first before returning to main window
            super(parent, "Grading: " + s.name, true);
            this.student = s;
            setSize(500, 600);
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout(10, 10));

            // Header Info
            JPanel pnlInfo = new JPanel(new GridLayout(4, 1));
            pnlInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            pnlInfo.setBackground(new Color(240, 240, 240));
            pnlInfo.add(new JLabel("Student: " + s.name + " (" + s.id + ")"));
            pnlInfo.add(new JLabel("Title: " + s.title));
            pnlInfo.add(new JLabel("Session: " + s.session));
            
            // Rubric Form
            JPanel pnlForm = new JPanel(new GridLayout(8, 1, 5, 5));
            pnlForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            sliderMethodology = createSlider("Methodology (0-40)", 40, s.scoreMethodology);
            sliderResults = createSlider("Results & Analysis (0-40)", 40, s.scoreResults);
            sliderPresentation = createSlider("Presentation Skills (0-20)", 20, s.scorePresentation);

            pnlForm.add(new JLabel("Methodology (40%):"));
            pnlForm.add(sliderMethodology);
            pnlForm.add(new JLabel("Results (40%):"));
            pnlForm.add(sliderResults);
            pnlForm.add(new JLabel("Presentation (20%):"));
            pnlForm.add(sliderPresentation);
            
            pnlForm.add(new JLabel("Evaluator Comments:"));
            txtComments = new JTextArea(3, 20);
            txtComments.setText(s.comment.equals("N/A") ? "" : s.comment);
            pnlForm.add(new JScrollPane(txtComments));

            // Footer / Actions
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
            slider.setMajorTickSpacing(10);
            slider.setMinorTickSpacing(1);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.addChangeListener(e -> updateTotal());
            return slider;
        }

        private void updateTotal() {
            int total = sliderMethodology.getValue() + sliderResults.getValue() + sliderPresentation.getValue();
            lblTotal.setText("Total Score: " + total + "/100  ");
        }

        private void saveGrade() {
            // Update student Object, but should be Submission object  
            student.scoreMethodology = sliderMethodology.getValue();
            student.scoreResults = sliderResults.getValue();
            student.scorePresentation = sliderPresentation.getValue();
            student.comment = txtComments.getText().replace("\n", " ").replace(",", ";"); // Sanitize for CSV
            student.status = "Graded";

            // Update UI
            updateTableData();
            
            // In a real app, write back to CSV here
            System.out.println("Saved Grade for " + student.name);
            
            dispose();
        }
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

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> new EvaluatorSystem().setVisible(true));
    }
}