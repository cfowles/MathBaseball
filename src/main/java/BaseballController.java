/**Main controller for the Math Baseball GUI. This class is the point of entry for the game, and deals with login and main menu events.
 * It connects with the database and shares the DBWrapper object with the gameplay classes.
 * Written by: Chris Fowles
 * 3/22/14
 */

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;


public class BaseballController implements ActionListener {

    private Image bgImage;
    private JButton loginButton;
    private JPanel loginPanel;
    private JPanel panel1;
    private JButton manageTeamButton;
    private JButton viewStatsButton;
    private JButton playBallButton;
    private boolean loggedIn = false;
    protected InputManager inputManager;
    protected GameAction exit;
    private JFrame screen;
    private JTextField uNameField;
    private JTextField pWordField;
    private JRadioButton isTeacher;
    private JButton registerButton;
    static String username;
    static DBWrapper dataBase;

    public BaseballController() {

        //Connect to Database

        try{
            dataBase = new DBWrapper();
        }
        catch (SQLException e){ e.printStackTrace(); }

        screen = new JFrame("Math Baseball");
        screen.setExtendedState(Frame.MAXIMIZED_BOTH);
        screen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        inputManager = new InputManager(screen);
        createGameActions();
        loginPanel = new BackGroundPanel();
        loginPanel.setPreferredSize(screen.getSize());
        loginPanel.setOpaque(false);
        loginPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        uNameField = new JTextField();
        TextPrompt uText = new TextPrompt("Username", uNameField, TextPrompt.Show.FOCUS_LOST);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 1;
        loginPanel.add(uNameField, constraints);


        pWordField = new JPasswordField();
        TextPrompt pText = new TextPrompt("Password", pWordField, TextPrompt.Show.FOCUS_LOST);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 2;
        loginPanel.add(pWordField, constraints);

        isTeacher = new JRadioButton("Teacher");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 3;
        loginPanel.add(isTeacher, constraints);

        loginButton = createButton("loginButton", "To Login");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 4;
        loginPanel.add(loginButton, constraints);

        registerButton = new JButton("Register");
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.anchor = GridBagConstraints.PAGE_END;
        loginPanel.add(registerButton, constraints);

        Container contentPane = screen.getContentPane();
        // make sure the content pane is transparent
        if (contentPane instanceof JComponent) {
            ((JComponent)contentPane).setOpaque(false);
        }
        // add components to the screen's content pane
        contentPane.add(loginPanel);
        screen.setVisible(true);
        screen.validate();
        //screen.repaint();
        // add listeners
        isTeacher.addActionListener(this);
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);
    }

    //Checks for system inputs
    public void update(long elapsedTime) {
        checkSystemInput();
    }

    //Checks which input occurs
    public void checkSystemInput() {
        if (exit.isPressed()) {
            System.exit(0);
        }
    }

    //Paints JPanel in the center of the screen
    private void paintPanel(){
        // center the dialog
        panel1.setLocation(
                (screen.getWidth() - panel1.getWidth()) / 2,
                (screen.getHeight() - panel1.getHeight()) / 2);
        // add the dialog to the "modal dialog" layer of the
        // screen's layered pane.
        Container contentPane = screen.getContentPane();
        contentPane.add(panel1);
    }

    //Maps the escape key to the gameaction escape
    public void createGameActions() {
        exit = new GameAction("exit",
                GameAction.DETECT_INITAL_PRESS_ONLY);
        inputManager.mapToKey(exit, KeyEvent.VK_ESCAPE);

    }

    //Overrides the draw method in Gamecore
    public void draw(Graphics g) {
        if(loggedIn)
            g.drawImage(bgImage, 0, 0, null);
        //g.drawImage()


        // the layered pane contains things like popups (tooltips,
        // popup menus) and the content pane.
        screen.getLayeredPane().paintComponents(g);
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

    private BufferedImage createCompatibleImage(int w, int h,
                                               int transparency)
    {
        Window window = screen;
        if (window != null) {
            GraphicsConfiguration gc =
                    window.getGraphicsConfiguration();
            return gc.createCompatibleImage(w, h, transparency);
        }
        return null;
    }

    protected class BackGroundPanel extends JPanel {
        BufferedImage img;
        {try{
            URL url = this.getClass().getResource("fenway.jpg");
            img = ImageIO.read(url);
        } catch (IOException e){}
        }
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 3, 4, this);
        }
    }


    public ImageIcon loadImage(String fileName) {
        return new ImageIcon(fileName);
    }


    private void setupMainMenuUI() {
        panel1 = new BackGroundPanel();
        panel1.setLayout(new GridLayoutManager(2, 3, new Insets(25, 25, 25, 25), -1, -1));
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null));
        viewStatsButton = createButton("viewScoresButton", "View Scores");
        viewStatsButton.setEnabled(true);
        panel1.add(viewStatsButton, new GridConstraints(0, 2, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        manageTeamButton = createButton("manageTeamButton", "Manage Team");
        manageTeamButton.setEnabled(true);
        panel1.add(manageTeamButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        playBallButton = createButton("playBallButton", "Play Ball");
        playBallButton.setEnabled(true);
        panel1.add(playBallButton, new GridConstraints(0, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK, GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        screen.add(panel1);
    }


    /**
     * Logs a student in given a correct username and password
     * @param u String username
     * @param p char password
     */
    public void logInStudent(String u, String p) {
        String pWord = p;
        if(dataBase.login(u, pWord)){
            setupMainMenuUI();
        }
        else{
            System.out.println("Login Failed.");
        }
    }

    /**
     * Logs the teacher in if they have previously registered for a new account
     * @param u String username
     * @param p char password
     */
    public void logInTeacher(String u, String p) {
        String pWord = p;
        if(dataBase.login(u, pWord)){
            TeacherView t = new TeacherView(dataBase, u);
            screen.dispose();
        }

    }

    /**
     * Event Handler. Handles login & menu events.
     * @param e ActionEvent event
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == registerButton){
            Register r = new Register(dataBase);
        }
        if(source == loginButton){ //CHANGE
            loggedIn = true;
            loginPanel.setVisible(false);
            if(isTeacher.isSelected()){
                logInTeacher(uNameField.getText(), pWordField.getText());
            }
            else{
                logInStudent(uNameField.getText(), pWordField.getText());
            }
        }
        if(source == playBallButton){
            PlayBall pB = new PlayBall();
        }
        if(source == manageTeamButton){
            ManageTeam manageTeam = new ManageTeam();
        }
        if(source == viewStatsButton){
            ViewScore viewScore = new ViewScore();
        }
    }

    //Main Runnable
    public static void main(String[] args) {
        BaseballController bC = new BaseballController();
    }
}


