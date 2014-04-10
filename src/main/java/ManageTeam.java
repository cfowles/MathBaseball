/**
 * Written by: Chris Fowles
 * The manage team class allows students to pick their team template and name.
 */

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;


public class ManageTeam implements ActionListener {
    private JPanel panel1;
    private JTextField teamNameField;
    private String teamName;
    private JLabel teamNameLabel;
    private JButton closeButton;
    private JButton saveButton;
    private JLabel pickTeamLabel;
    private JButton team2Button;
    private JButton team1Button;
    private JButton team3Button;
    private int teamSelected;
    private JFrame screen;
    private int WIDTH = 800;
    private int HEIGHT = 600;


    public ManageTeam(){
    teamName = MathBaseball.getName();
    screen = new JFrame("Manage Team");
        screen.setSize(WIDTH, HEIGHT);
        screen.setLocationRelativeTo(null);
        screen.setBackground(Color.WHITE);
        setupUI();
        screen.getContentPane().add(panel1);
        screen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        screen.setVisible(true);

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

    //Takes resource name and returns button
    public JButton selectedButton(String name, String toolTip) {

        // load the image
        String imagePath = "./resources/" + name + ".png";
        ImageIcon iconRollover = new ImageIcon(imagePath);
        int w = iconRollover.getIconWidth();
        int h = iconRollover.getIconHeight();

        // get the cursor for this button
        Cursor cursor =
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);


        ImageIcon iconDefault = iconRollover;
        ImageIcon iconPressed = iconRollover;

        // create the button
        JButton button = new JButton();
        //button.addActionListener(this);
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
     * Creates a translucent image for the create button method
     * @param w int width
     * @param h int height
     * @param transparency int transparency
     * @return
     */
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

    public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
        if(source == saveButton){
           if(teamNameField.getText() != "")
            MathBaseball.setName(teamNameField.getText());
           MathBaseball.changeTemplate(BaseballController.username, teamSelected);
           screen.dispose();
        }
        if(source == closeButton){
            screen.dispose();
        }
        if(source == team1Button){
            team1Button = selectedButton("team1Button", "Currently Selected");
            teamSelected = 1;
        }
        if(source == team2Button){
            team1Button = selectedButton("team2Button", "Currently Selected");
            teamSelected = 2;
        }
        if(source == team3Button){
            team1Button = selectedButton("team3Button", "Currently Selected");
            teamSelected = 3;
        }
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

    /**
     * Sets up the ManageTeam UI
     */
    private void setupUI() {
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 5, new Insets(0, 0, 0, 0), -1, -1));
        closeButton = new JButton();
        closeButton.setText("Close");
        panel1.add(closeButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        saveButton = new JButton();
        saveButton.setText("Save");
        panel1.add(saveButton, new GridConstraints(2, 1, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pickTeamLabel = new JLabel();
        pickTeamLabel.setText("Pick Team Color:");
        panel1.add(pickTeamLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        teamNameLabel = new JLabel();
        teamNameLabel.setText("Your Team Name:");
        panel1.add(teamNameLabel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(89, 32), null, 0, false));
        team2Button = createButton("cap1", "Red Team");
        panel1.add(team2Button, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        team1Button = createButton("cap2", "Blue Team");
        panel1.add(team1Button, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        team3Button = createButton("cap3", "Yellow Team");
        panel1.add(team3Button, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        teamNameField = new JTextField();
        panel1.add(teamNameField, new GridConstraints(0, 2, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        teamNameField.setText(teamName);
        saveButton.addActionListener(this);
        closeButton.addActionListener(this);
    }
}
