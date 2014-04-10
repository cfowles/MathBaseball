import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Chris Fowles on 3/23/14. Panel for a teacher to add a student
 */
public class AddStudent extends JFrame implements ActionListener {
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton enterButton;
    private DBWrapper dBase;
    private String teacherUName;
    private int WIDTH = 300;
    private int HEIGHT = 500;
    private JPanel panel1;
    private Object changed;
    private String oldFirstName;
    private String oldLastName;
    private String oldUserName;
    private String oldPassWord;

    {
        $$$setupUI$$$();
    }

    public AddStudent(DBWrapper db, String tUName){
        super("Math Baseball");

        dBase = db;
        teacherUName = tUName;
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setBackground(Color.WHITE);
        panel1 = new JPanel();
        $$$setupUI$$$();
        getContentPane().add(panel1);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        addStudentToDataBase();
                        return null;
                    }
                }.execute();
            }
        });
    }

    public AddStudent(DBWrapper db, String firstN, String lastN, String uName, String pWord, String tUName){
        super("Math Baseball");

        dBase = db;
        teacherUName = tUName;
        oldFirstName = firstN;
        oldLastName = lastN;
        oldUserName = pWord;
        oldPassWord = uName;
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setBackground(Color.WHITE);
        panel1 = new JPanel();
        $$$setupUI$$$();
        textField1.setText(lastN);
        textField2.setText(firstN);
        textField3.setText(pWord);
        textField4.setText(uName);
        getContentPane().add(panel1);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        editStudent();
                        return null;
                    }
                }.execute();
            }
        });
    }

    private void editStudent(){
        TeacherView.resetPanel();
        if(! oldUserName.equals(textField3.getText())){
            dBase.changeUsername(oldUserName, textField3.getText());
            oldUserName = textField3.getText();
        }
        if(! oldFirstName.equals(textField1.getText()))
            dBase.changeFirstName(oldUserName, textField1.getText());
        if(! oldLastName.equals(textField2.getText()))
            dBase.changeLastName(oldUserName, textField2.getText());
        if(! oldPassWord.equals(textField4.getText()))
            dBase.changePassword(oldUserName, textField4.getText());
        this.dispose();
    }

    private void addStudentToDataBase(){
        String lastName = textField1.getText();
        String firstName = textField2.getText();
        String sUserName = textField3.getText();
        String sPWord = textField4.getText();
        dBase.addNewStudent(sUserName, sPWord, firstName, lastName, teacherUName); //dBase.addStudent(...)
        this.dispose();
    }

    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("First Name");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField2 = new JTextField();
        panel1.add(textField2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Username:");
        panel1.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Password:");
        panel1.add(label3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        enterButton = new JButton();
        enterButton.setText("Enter");
        panel1.add(enterButton, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField1 = new JTextField();
        panel1.add(textField1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Last Name:");
        panel1.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField3 = new JTextField();
        panel1.add(textField3, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        textField4 = new JTextField();
        panel1.add(textField4, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
