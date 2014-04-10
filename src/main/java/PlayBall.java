import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

/**
 * Created by Febo on 2/17/14.
 */
public class PlayBall implements ActionListener {
    private JPanel panel1;
    private JTextArea textPane1;
    private JTextField textField1;
    private JButton SWINGButton;
    private JFormattedTextField formattedTextField1;
    private int firstNum;
    private int secondNum;
    private BufferedImage bgImage;
    private String answer;
    private String tempQuestion;
    private boolean qAnswered;
    private char questionType; //addition(a) subtraction(s) places(p)
    static final int WIDTH = 1024;
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
    private JFrame screen;

    public PlayBall(){

        screen = new JFrame("Math Baseball");
        screen.setSize(WIDTH, HEIGHT);
        screen.setLocationRelativeTo(null);
        screen.setBackground(Color.WHITE);
        firstScreen();  //Sets up initial screen where student picks game mode
        screen.getContentPane().add(panel2);
        screen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        screen.setVisible(true);

    }

    public void startGui() {

        $$$setupUI$$$();
        screen.getContentPane().add(panel1);
        screen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        screen.setVisible(true);
        screen.getRootPane().setDefaultButton(SWINGButton);
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

    private void answerQuestion(){
        answer = textField1.getText();
        if(qAnswered){
            MathBaseball.answerReceived(Integer.parseInt(answer), questionType);
            textField1.setText("");
        } else {
            MathBaseball.answerReceived();
        }

    }

    /**Main display method for the gameplay UI
     * Called to display text to the student in the main text pane
     * @param str String student text
     */
    public void displayToScreen(String str){
        textPane1.setText(str);
    }

    /**Setst the text of the Start / Swing / Next Question Button
     * depending on the state of the game
     * @param str String button text
     */
    public void setButtonText(String str){
        SWINGButton.setText(str);
    }

    /**Sets the score that is visible to the student
     * @param str String score
     */
    public void setScore(String str){
        formattedTextField1.setText(str);
    }

    /**Clears the textfield after the student has answered the question
     */
    public void clearAnswer(){
        textField1.setText("");
    }

    /**Removes the initial panel to get the screen ready for the game
     */
    public void setScreenForGame(){
        screen.removeAll();
        screen.repaint();
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
        panel2 = new BackGroundPanel();
        allGame = createButton("randomButton", "Random");
        addGame = createButton("additionButton", "Addition");
        subGame = createButton("subtractionButton", "Subtraction");
        placeGame = createButton("placesButton", "Places");
        panel2.add(allGame);
        panel2.add(addGame);
        panel2.add(subGame);
        panel2.add(placeGame);

    }



    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        //$$$setupUI$$$();
    }

    /**
     * Sets up the gameplay UI
     */
    private void $$$setupUI$$$() {
        panel1 = new BackGroundPanel();
        panel1.setOpaque(false);
        panel1.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textPane1 = new JTextArea();
        textPane1.setFont(new Font("HGMinchoL", textPane1.getFont().getStyle(), 22));
        textPane1.setText("");
        textPane1.setEditable(false);
        textPane1.setOpaque(false);
        panel1.add(textPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Answer:");
        panel1.add(label1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField1 = new JTextField();
        panel1.add(textField1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        SWINGButton = createButton("bat", "SWING!");
        //SWINGButton.setText("SWING");
        panel1.add(SWINGButton, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        formattedTextField1 = new JFormattedTextField();
        formattedTextField1.setEditable(false);
        formattedTextField1.setFont(new Font("HGMinchol", formattedTextField1.getFont().getStyle(), 22));
        panel1.add(formattedTextField1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

    public Image loadImage(String fileName) {
        return new ImageIcon(fileName).getImage();
    }


    /**Starts te game
     *
     * @param e ActionEvent event
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == addGame)
            MathBaseball.playGame('a', BaseballController.username, this);
        if(source == subGame)
            MathBaseball.playGame('s', BaseballController.username, this);
        if(source == allGame)
            MathBaseball.playGame('r', BaseballController.username, this);
        if(source == placeGame)
            MathBaseball.playGame('p', BaseballController.username, this);
        if(source == SWINGButton)
            answerQuestion();
    }

    //Creates the background image as a JPanel
    protected class BackGroundPanel extends JPanel {
        BufferedImage img;
        BufferedImage bases;
        {try{
               URL url = this.getClass().getResource("gameplay.png");
               //URL bUrl = this.getClass().getResource(Team.getRunnersOn());
               //URL cUrl = this.getClass().getResource(MathBaseball.getTeamTemplate());
               img = ImageIO.read(url);
            } catch (IOException e){}
        }
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 3, 4, this);
        }
    }

    //Takes resource name and returns button
    public JButton createButton(String name, String toolTip) {

        // load the image
        String imagePath = "./resources/" + name + ".png";
        ImageIcon iconRollover = new ImageIcon(imagePath);
        int w = iconRollover.getIconWidth();
        int h = iconRollover.getIconHeight();

        // get the cursor for this button
        Cursor cursor =
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

        // make translucent default image
        Image image = createCompatibleImage(w, h,
                Transparency.TRANSLUCENT);
        Graphics2D g = (Graphics2D)image.getGraphics();
        Composite alpha = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, .5f);
        g.setComposite(alpha);
        g.drawImage(iconRollover.getImage(), 0, 0, null);
        g.dispose();
        ImageIcon iconDefault = new ImageIcon(image);

        // make a pressed image
        image = createCompatibleImage(w, h,
                Transparency.TRANSLUCENT);
        g = (Graphics2D)image.getGraphics();
        g.drawImage(iconRollover.getImage(), 2, 2, null);
        g.dispose();
        ImageIcon iconPressed = new ImageIcon(image);

        // create the button
        JButton button = new JButton();
        button.addActionListener(this);
        button.setIgnoreRepaint(true);
        button.setFocusable(false);
        button.setToolTipText(toolTip);
        button.setBorder(null);
        button.setContentAreaFilled(false);
        button.setCursor(cursor);
        button.setIcon(iconDefault);
        button.setRolloverIcon(iconRollover);
        button.setPressedIcon(iconPressed);

        return button;
    }

    /**
     * Creates translucent image for create button
     * @param w int width
     * @param h int height
     * @param transparency int level of translucence
     * @return
     */
    private BufferedImage createCompatibleImage(int w, int h,
                                                int transparency)
    {
            GraphicsConfiguration gc =
                    screen.getGraphicsConfiguration();
            return gc.createCompatibleImage(w, h, transparency);
    }


}
