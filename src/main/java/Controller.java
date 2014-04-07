import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;


public class Controller extends GameCore implements ActionListener, MouseListener {

    public static void main(String[] args) {
        new Controller().run();
    }


    private Image bgImage;
    private JButton loginButton;
    private JPanel loginPanel;
    private JPanel gamePanel;
    private JPanel scoresPanel;
    private JPanel managePanel;
    private JPanel panel1;
    private JButton manageTeamButton;
    private JButton viewStatsButton;
    private JLabel playBallLabel;
    private JTextArea textPane1;
    private JFormattedTextField formattedTextField1;
    private JTextField textField1;
    private JButton SWINGButton;
    private boolean loggedIn = false;
    private JButton quitButton;
    private JButton playButton;
    private JButton pauseButton;
    private JButton manageButton;
    private JButton scoreButton;
    private JPanel playButtonSpace;
    protected InputManager inputManager;
    protected GameAction configAction;
    protected GameAction scoreAction;
    protected GameAction exit;
    DBWrapper dBase;
    //private JTextField uNameField;
    //private JTextField pWordField;
    //private JRadioButton isTeacher;

    public void init() {
        //Connect to Database
        /*
        try{
            dBase = new DBWrapper();
        }
        catch (SQLException e){ }*/
        //Initialize GameCore()
        super.init();

        Window window = screen.getFullScreenWindow();
        inputManager = new InputManager(window);
        NullRepaintManager.install();
        createBG();
        createGameActions();
        loginPanel = new JPanel();
        loginPanel.setLayout(new BorderLayout());
        loginPanel.setPreferredSize(screen.getFullScreenWindow().getSize());
        loginPanel.setOpaque(false);
        loginButton = createButton("loginButton", "To Login");
        loginPanel.add(loginButton, BorderLayout.SOUTH);


        JFrame frame = (JFrame)screen.getFullScreenWindow();
        Container contentPane = frame.getContentPane();

        // make sure the content pane is transparent
        if (contentPane instanceof JComponent) {
            ((JComponent)contentPane).setOpaque(false);
        }

        // add components to the screen's content pane
        contentPane.add(loginPanel);
        frame.validate();
        screen.getFullScreenWindow().add(loginPanel);

        // add listeners
        loginButton.addActionListener(this);

    }

    public void update(long elapsedTime) {
        checkSystemInput();
    }

    public void checkSystemInput() {
        if (exit.isPressed()) {
            stop();
        }
    }

    private void paintPanel(){
        // center the dialog
        panel1.setLocation(
                (screen.getWidth() - panel1.getWidth()) / 2,
                (screen.getHeight() - panel1.getHeight()) / 2);
        // add the dialog to the "modal dialog" layer of the
        // screen's layered pane.
        JFrame frame = (JFrame)super.screen.getFullScreenWindow();
        Container contentPane = frame.getContentPane();
        contentPane.add(panel1);
    }

    public void createGameActions() {
        exit = new GameAction("exit",
                GameAction.DETECT_INITAL_PRESS_ONLY);
        inputManager.mapToKey(exit, KeyEvent.VK_ESCAPE);

    }

    public void draw(Graphics2D g) {
        if(loggedIn)
            g.drawImage(bgImage, 0, 0, null);
        //g.drawImage()
        JFrame frame = (JFrame)super.screen.getFullScreenWindow();

        // the layered pane contains things like popups (tooltips,
        // popup menus) and the content pane.
        frame.getLayeredPane().paintComponents(g);
    }


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
        Image image = screen.createCompatibleImage(w, h,
                Transparency.TRANSLUCENT);
        Graphics2D g = (Graphics2D)image.getGraphics();
        Composite alpha = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, .5f);
        g.setComposite(alpha);
        g.drawImage(iconRollover.getImage(), 0, 0, null);
        g.dispose();
        ImageIcon iconDefault = new ImageIcon(image);

        // make a pressed image
        image = screen.createCompatibleImage(w, h,
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
     Load Background Image
     */
    private void createBG() {
        if(!loggedIn)
            bgImage = loadImage("./resources/loginImage.jpg");
        else
            bgImage = loadImage("./resources/fenway.jpg");
    }

    private void setupMainMenu(){
        // create an additional GameAction for "config"
        configAction = new GameAction("config");
        scoreAction = new GameAction("score");

        // create buttons
        quitButton = createButton("quit", "Quit");
        playButton = createButton("play", "Continue");
        pauseButton = createButton("pause", "Pause");
        manageButton = createButton("menu", "Manage Team");
        scoreButton = createButton("score", "View Scores");

        // create the space where the play/pause buttons go.
        playButtonSpace = new JPanel();
        playButtonSpace.setOpaque(false);
        playButtonSpace.add(pauseButton);

        JFrame frame = (JFrame)super.screen.getFullScreenWindow();
        Container contentPane = frame.getContentPane();

        // make sure the content pane is transparent
        if (contentPane instanceof JComponent) {
            ((JComponent)contentPane).setOpaque(false);
        }

        // add components to the screen's content pane
        contentPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        contentPane.add(playButtonSpace);
        contentPane.add(manageButton);
        contentPane.add(scoreButton);
        contentPane.add(quitButton);

        // explicitly lay out components (needed on some systems)
        frame.validate();
    }

    private void setupMainMenuUI() {
        panel1 = new TransparentPanel();
        panel1.setOpaque(false);
        panel1.setLayout(new GridLayoutManager(2, 3, new Insets(25, 25, 25, 25), -1, -1));
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null));
        viewStatsButton = new JButton();
        viewStatsButton.setEnabled(true);
        viewStatsButton.setText("View Scores");
        panel1.add(viewStatsButton, new GridConstraints(0, 2, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        manageTeamButton = new JButton();
        manageTeamButton.setEnabled(true);
        manageTeamButton.setText("Manage Team");
        panel1.add(manageTeamButton, new GridConstraints(0, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        playBallLabel = new JLabel();
        playBallLabel.setFocusable(true);
        playBallLabel.setIcon(new ImageIcon("./resources/playBallButton.png"));
        playBallLabel.setOpaque(false);
        playBallLabel.setText("");
        playBallLabel.setVisible(true);
        playBallLabel.addMouseListener(this);
        playBallLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewStatsButton.addMouseListener(this);
        viewStatsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        manageTeamButton.addMouseListener(this);
        manageTeamButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel1.add(playBallLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    private void setupPlayBallUI() {
        panel1 = new TransparentPanel();
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

    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();

        if(source == playBallLabel){
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    PlayBall p = new PlayBall();
                    return null;
                }
            }.execute();

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

    /*public JComponent $$$getRootComponent$$$() {
        return panel1;
    }*/

    private class TransparentPanel extends JPanel {
        {
            setOpaque(false);
        }
        public void paintComponent(Graphics g) {
            g.setColor(getBackground());
            Rectangle r = g.getClipBounds();
            g.fillRect(r.x, r.y, r.width, r.height);
            super.paintComponent(g);
        }
    }

    public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
        if(source == loginButton){
            loggedIn = true;
            loginButton.setVisible(false);
            createBG();
            //setupMainMenu();
            setupPlayBallUI();
            paintPanel();
            Graphics2D g = screen.getGraphics();
            draw(g);
        }
    }
}

