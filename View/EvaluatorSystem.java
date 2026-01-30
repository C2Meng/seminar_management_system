package View;

import Controller.Evaluator;
import Controller.Session;
import MainFrame.MainFrame;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class EvaluatorSystem extends JFrame {

    private List<Session> sessions = new ArrayList<>();
    private Set<String> gradedSessionIDs = new HashSet<>();
    private JTable table;
    private DefaultTableModel tableModel;
    private String evaluatorID;
    private Evaluator controller;

    public EvaluatorSystem(String evaluatorID) {
        this.evaluatorID = evaluatorID;
        controller = new Evaluator();

        setTitle("Seminar Evaluator - Session View");
        setSize(1000, 500); // Widened for Title column
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loadData();

        // Added "Project Title" column
        String[] columnNames = { "Session ID", "Date/Time", "Student Name", "Project Title", "Status" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        updateTableData();

        table = new JTable(tableModel);
        table.setRowHeight(30);
        // Set column width for Title to be wider
        table.getColumnModel().getColumn(3).setPreferredWidth(250);

        JScrollPane scrollPane = new JScrollPane(table);

        JButton logOutButton = new JButton("Logout");
        logOutButton.addActionListener(e -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
            this.dispose();
        });

        JButton btnGrade = new JButton("Grade Selected Session");
        btnGrade.setFont(new Font("Arial", Font.BOLD, 14));
        btnGrade.addActionListener(e -> openGradingWindow());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(logOutButton);
        bottomPanel.add(btnGrade);

        add(new JLabel("  My Assigned Sessions"), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        sessions = controller.loadSessions(evaluatorID);
        gradedSessionIDs = controller.loadGradedSessionIDs();
    }

    private void updateTableData() {
        tableModel.setRowCount(0);
        for (Session s : sessions) {
            String timeStr = s.getStartTime() + " - " + s.getEndTime();
            String status = gradedSessionIDs.contains(String.valueOf(s.getSessionID())) ? "Graded" : "Pending";

            // s.getProjectTitle() pulls from the linked Submission object
            tableModel.addRow(new Object[] {
                    s.getSessionID(),
                    timeStr,
                    s.getPresenter(),
                    s.getProjectTitle(),
                    status
            });
        }
    }

    private void openGradingWindow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a session to grade.");
            return;
        }
        Session selectedSession = sessions.get(selectedRow);
        new GradingDialog(this, selectedSession, controller).setVisible(true);
    }

    class GradingDialog extends JDialog {
        private Session session;
        private Evaluator controller;
        private JSlider sliderClarity, sliderMethodology, sliderResults, sliderPresentation;
        private JLabel lblTotal;
        private JTextArea txtComments;

        public GradingDialog(JFrame parent, Session s, Evaluator evaluator) {
            super(parent, "Grading: " + s.getPresenter(), true);
            this.controller = evaluator;
            this.session = s;
            setSize(600, 750);
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout(10, 10));

            // --- Header Info ---
            JPanel pnlInfo = new JPanel(new GridLayout(5, 1));
            pnlInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            pnlInfo.setBackground(new Color(240, 240, 240));
            pnlInfo.add(new JLabel("Seminar ID: " + s.getSeminar().getSeminarID()));
            pnlInfo.add(new JLabel("Session ID: " + s.getSessionID()));
            pnlInfo.add(new JLabel("Student: " + s.getPresenter()));
            pnlInfo.add(new JLabel("Title: " + s.getProjectTitle()));

            // Show Abstract from the linked submission
            String abstractText = (s.getSubmission() != null) ? s.getSubmission().getAbstractText() : "N/A";
            pnlInfo.add(new JLabel("Abstract Snippet: "
                    + (abstractText.length() > 50 ? abstractText.substring(0, 50) + "..." : abstractText)));

            // --- Rubric Form ---
            JPanel pnlForm = new JPanel(new GridLayout(10, 1, 5, 5));
            pnlForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            sliderClarity = createSlider(10);
            sliderMethodology = createSlider(30);
            sliderResults = createSlider(40);
            sliderPresentation = createSlider(20);

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
            pnlForm.add(new JScrollPane(txtComments));

            JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            lblTotal = new JLabel("Total Score: 0/100  ");

            JButton btnSave = new JButton("Submit Evaluation");
            btnSave.addActionListener(e -> saveGrade());

            pnlFooter.add(lblTotal);
            pnlFooter.add(btnSave);

            add(pnlInfo, BorderLayout.NORTH);
            add(pnlForm, BorderLayout.CENTER);
            add(pnlFooter, BorderLayout.SOUTH);
        }

        private JSlider createSlider(int max) {
            JSlider slider = new JSlider(0, max, 0);
            slider.setMajorTickSpacing(max / 5 > 0 ? max / 5 : 1);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.addChangeListener(e -> {
                int total = sliderClarity.getValue() + sliderMethodology.getValue() + sliderResults.getValue()
                        + sliderPresentation.getValue();
                lblTotal.setText("Total Score: " + total + "/100  ");
            });
            return slider;
        }

        private void saveGrade() {
            String comment = txtComments.getText().replace("\n", " ").replace(",", ";");
            if (comment.isEmpty())
                comment = "N/A";

            String seminarID = (session.getSeminar() != null)
                    ? String.valueOf(session.getSeminar().getSeminarID())
                    : "0";
            controller.saveGrade(seminarID, session, sliderClarity.getValue(), sliderMethodology.getValue(),
                    sliderResults.getValue(), sliderPresentation.getValue(), comment);
            ((EvaluatorSystem) getParent()).gradedSessionIDs.add(String.valueOf(session.getSessionID()));
            ((EvaluatorSystem) getParent()).updateTableData();
            dispose();
        }
    }

}