import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

/**
 * Created by Febo on 2/17/14.
 */
public class PlayBall extends JFrame implements MouseListener {
    private JPanel panel1;
    private JTextArea textPane1;
    private JTextField textField1;
    private JButton SWINGButton;
    private JFormattedTextField formattedTextField1;
    private int firstNum;
    private int secondNum;
    private String answer;
    private String tempQuestion;
    private boolean qAnswered;
    private char questionType; //addition(a) subtraction(s) places(p)
    private Random gen = new Random();
    static final int WIDTH = 850;
    static final int HEIGHT = 650;
    private int score = 0;
    private String atBatPlayer;
    private JPanel panel2;
    private JButton addGame;
    private JButton subGame;
    private JButton placeGame;
    private JButton allGame;
    private boolean allQ;
    private int gameID;
    DBWrapper dBase;

    public PlayBall(char type, DBWrapper db) {

        super("Math Baseball");
        dBase = db;
        setSize(WIDTH, HEIGHT);
        setBackground(Color.WHITE);
        $$$setupUI$$$();
        getContentPane().add(panel1);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        this.getRootPane().setDefaultButton(SWINGButton);
        questionType = type;
        if(questionType == 'r')
            allQ = true;
        //gameID = db.startNewGame(MainMenu.studentUserName);

        SWINGButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        answerQuestion();
                        return null;
                    }
                }.execute();
            }
        });}

        private void answerQuestion(){
        answer = textField1.getText();
        if(qAnswered){
            MathBaseball.answerReceived(Integer.parseInt(answer), questionType);
            textField1.setText("");
        } else {
            int max = 50;
            int type = 1;
            if(allQ){
              //type = gen.nextInt(2) + 1;
            }
            else{
            if(questionType == 'a')
                    MathBaseball.generateQuestion(max, 1);
            else if(questionType == 's'){
                    MathBaseball.generateQuestion(max, 2);
                }
            else
                    MathBaseball.generateQuestion(max, 3);
            }
            MathBaseball.generateQuestion(max, type);
        }

        textField1.getDocument().addDocumentListener(new DocumentListener() {
            //Listen for changes in the text.
            public void insertUpdate(DocumentEvent e) {
            changed();
            }
            public void removeUpdate(DocumentEvent e) {
            changed();
            }
            public void changedUpdate(DocumentEvent e) {
            changed();
            }
        });
    }

    public PlayBall(){
        super("Math Baseball");

        //dBase = db;
        setSize(WIDTH, HEIGHT);
        setBackground(Color.WHITE);
        firstScreen();
        getContentPane().add(panel2);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        allGame.addMouseListener(this);
        allGame.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addGame.addMouseListener(this);
        addGame.setCursor(new Cursor(Cursor.HAND_CURSOR));
        subGame.addMouseListener(this);
        subGame.setCursor(new Cursor(Cursor.HAND_CURSOR));
        placeGame.addMouseListener(this);
        placeGame.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void changed() {
        if (textField1.getText().equals("")){
            qAnswered = false;
        }
        else {
            qAnswered = true;
        }

    }

    public void beginGame(){
        SWINGButton.setText("START");
        formattedTextField1.setText("SCORE: ");
        textPane1.setText("Welcome to Math Baseball! \n" +
                "For each problem click SWING or press ENTER to submit your answer\n"
                + "To begin press the START button below\n"
                + "GOOD LUCK!");
    }
    
    private String buildQuestion(int one, int two){
        SWINGButton.setText("SWING");
        String s;
        if(questionType == 'a'){
          s = ("What is the SUM of " + one + " and " + two +"? " + one + " + " + two + " = ");
        }
        else if(questionType == 's'){
          s = ("What is the DIFFERENCE of " + one + " and " + two +"? " + one + " - " + two + " = ");
        }
        else{
          s = ("What is in the " + placeFinder(two) + " place of " + one +"?");
        }
        tempQuestion = s;
        return s;
    }

    private String placeFinder(int x){
        String s;
        if(x == 1){
            s = "One's";
        }
        else if(x == 2){
            s = "Tenth's";
        }
        else{
            s = "Hundredth's";
        }
        return s;
    }

    public void generatePlayerName(){
        if(score == 1)
            atBatPlayer = "Stinky";
        else if(score % 2 == 0)
            atBatPlayer = "The Duke of Tralee";
        else if(score % 3 == 0)
            atBatPlayer = "Hammerin' Hank";
        else if(score % 4 == 0)
            atBatPlayer = "Old Pete";
    }
    
    public void displayAdditionQuestion(int one, int two){
        firstNum = one;
        secondNum = two;
        questionType = 'a';
        String d = buildQuestion(one, two);
        textPane1.setText("");
        textPane1.setText(d);
    }

    public void displaySubtractionQuestion(int one, int two){
        firstNum = one;
        secondNum = two;
        questionType = 's';
        textPane1.setText("");
        textPane1.setText(buildQuestion(one, two));

    }

    public void displayPlacesQuestion(int one, int two){
        firstNum = one;
        secondNum = two;
        questionType = 'p';
        textPane1.setText("");
        textPane1.setText(buildQuestion(one, two));

    }

    public void displayCorrect(){
        //dBase.correctAnswer(gameID);
        //score = dBase.getScoreForStudent(MainMenu.studentUserName);
        textPane1.setText("");
        generatePlayerName();
        textPane1.setText("GOOD JOB! That was the correct answer.\n"
                + atBatPlayer + " got a hit!\n" +
                "Press Next Question, or hit ENTER, to continue.");
        SWINGButton.setText("Next Question");
        formattedTextField1.setText("");
        formattedTextField1.setText("Score: " + score);
    }

    public void displayWrongAdd(int c){
        //dBase.incorrectAnswer(gameID);
        textPane1.setText("");
        generatePlayerName();
        textPane1.setText("Nice try, but that was INCORRECT.\n" + atBatPlayer + " WHIFFED\n" + "The right answer was " + c);
        SWINGButton.setText("Next Question");
    }

    public void displayWrongSub(int c){
        //dBase.incorrectAnswer(gameID);
        textPane1.setText("");
        generatePlayerName();
        textPane1.setText("Nice try, but that was INCORRECT. \n" + atBatPlayer + " hit a FOUL BALL\n" + "The right answer was " + c);
        SWINGButton.setText("Next Question");
    }

    public void displayWrongPlaces(int c){
        //dBase.incorrectAnswer(gameID);
        textPane1.setText("");
        textPane1.setText("Nice try, but that was INCORRECT. \nThe right answer was " + c);
        SWINGButton.setText("Next Question");
    }

    private void firstScreen(){
        panel2 = new JPanel();
        allGame = new JButton("Random");
        addGame = new JButton("Addition");
        subGame = new JButton("Subtraction");
        placeGame = new JButton("Places");
        panel2.add(allGame);
        panel2.add(addGame);
        panel2.add(subGame);
        panel2.add(placeGame);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textPane1 = new JTextArea();
        textPane1.setFont(new Font("HGMinchoL", textPane1.getFont().getStyle(), 22));
        textPane1.setText("");
        textPane1.setEditable(false);
        panel1.add(textPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Answer:");
        panel1.add(label1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField1 = new JTextField();
        panel1.add(textField1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        SWINGButton = new JButton();
        SWINGButton.setText("SWING");
        panel1.add(SWINGButton, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        formattedTextField1 = new JFormattedTextField();
        formattedTextField1.setEditable(false);
        formattedTextField1.setFont(new Font("HGMinchol", formattedTextField1.getFont().getStyle(), 22));
        panel1.add(formattedTextField1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }


    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();

        if(source == addGame)
            MathBaseball.makeGui('a', dBase);
        if(source == subGame)
            MathBaseball.makeGui('s', dBase);
        if(source == allGame)
            MathBaseball.makeGui('r', dBase);
        if(source == placeGame){
            MathBaseball.makeGui('p', dBase);
        }
    }


    public void mousePressed(MouseEvent e) {

    }


    public void mouseReleased(MouseEvent e) {

    }


    public void mouseEntered(MouseEvent e) {

    }


    public void mouseExited(MouseEvent e) {

    }
}
