
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Chris Fowles on 3/22/14.
 */
public class TeacherView extends JFrame{
    static final int WIDTH = 850;
    static final int HEIGHT = 650;
    private JTable studentTable;
    private JPanel panel1;
    private static JButton addStudentButton;
    private JButton loadButton;
    private final DefaultTableModel tableModel = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private DBWrapper dBase;
    private String teacherUName;
    private ArrayList<StudentInfo> studentList = new ArrayList<>();
    protected static boolean eMode;



    public TeacherView(DBWrapper db, String tUName){
        super("Math Baseball");

        dBase = db;
        teacherUName = tUName;
        eMode = false;
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setBackground(Color.WHITE);
        panel1 = new JPanel();
        $$$setupUI$$$();
        getContentPane().add(panel1);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        loadData();
        addStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!eMode){
                    new SwingWorker<Void, Void>() {

                        protected Void doInBackground() throws Exception {
                            addStudent();
                            return null;
                        }
                    }.execute();
                }
            }
        });
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        loadData();
                        return null;
                    }
                }.execute();
            }
        });
        studentTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // This is for double click event on anywhere on JTable
                if (e.getClickCount() == 2 && !eMode) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    int column = target.getSelectedColumn();
                    if(row != 0){
                        eMode = true;

                        addStudentButton.setText("EDIT STUDENT");
                        final String firstName = tableModel.getValueAt(row, 0).toString();
                        final String lastName = tableModel.getValueAt(row, 1).toString();
                        final String userName = tableModel.getValueAt(row, 2).toString();
                        final String passWord = tableModel.getValueAt(row, 3).toString();
                        addStudentButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                new SwingWorker<Void, Void>() {
                                    @Override
                                    protected Void doInBackground() throws Exception {
                                        addStudent(firstName, lastName, userName, passWord);
                                        return null;
                                    }
                                }.execute();
                            }
                        });

                    }
                }
            }

        });
    }

    protected static void resetPanel(){
        eMode = false;
        addStudentButton.setText("Add Student");
    }

    private void addStudent(){
        AddStudent a = new AddStudent(dBase, teacherUName);
    }

    private void addStudent(String f, String l, String u, String p){
        AddStudent a = new AddStudent(dBase, f, l, u, p, teacherUName);
    }

    private void loadData() {

        loadButton.setEnabled(false);

        studentList = dBase.getStudentInfoForTeacher(teacherUName);


        Vector<String> columnNames = new Vector<String>();
        columnNames.add("");
        columnNames.add("First Name");
        columnNames.add("Last Name");
        columnNames.add("Username");
        columnNames.add("Password");
        columnNames.add("Score");
        columnNames.add("Number Attempted");
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();

        Vector<Object> vector2 = new Vector<Object>();
        vector2.add("First Name");
        vector2.add("Last Name");
        vector2.add("Username");
        vector2.add("Password");
        vector2.add("Score");
        vector2.add("Number Attempted");
        data.add(vector2);

        for(StudentInfo s:studentList){
            Vector<Object> vector = new Vector<Object>();
            vector.add(s.getFirstName());
            vector.add(s.getLastName());
            String uName = s.getUsername();
            vector.add(uName);
            vector.add(s.getPassword());
            vector.add(dBase.getScoreForStudent(uName));
            vector.add(dBase.getNumberOfQuestionsForStudent(uName));
            data.add(vector);
        }

        tableModel.setDataVector(data, columnNames);

        loadButton.setEnabled(true);

    }

    //
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        studentTable = new JTable(tableModel);
        panel1.add(studentTable, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        addStudentButton = new JButton();
        addStudentButton.setText("Add Student");
        panel1.add(addStudentButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loadButton = new JButton();
        loadButton.setText("Load Data");
        panel1.add(loadButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}

