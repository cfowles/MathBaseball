import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Chris Fowles on 4/9/14.
 */
public class ViewScore {
    private JTextPane scorePane;
    private BackGroundPanel bgPanel;
    private JPanel panel1;
    private JFrame screen;
    private int WIDTH = 800;
    private int HEIGHT = 600;

    public ViewScore(){
        screen = new JFrame("View Score");
        screen.setSize(WIDTH, HEIGHT);
        screen.setLocationRelativeTo(null);
        bgPanel = new BackGroundPanel();
        setupUI();
        bgPanel.setLayout(new GridBagLayout());
        bgPanel.add(panel1, new GridBagConstraints());
        screen.getContentPane().add(bgPanel);
        screen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        screen.setVisible(true);
    }

    protected class BackGroundPanel extends JPanel {
        BufferedImage img;
        {try{
            URL url = this.getClass().getResource("score.jpg");
            img = ImageIO.read(url);
        } catch (IOException e){}
        }
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 3, 4, this);
        }
    }

    private void setScorePane(int s){

    }

    /**
     */
    private void setupUI() {
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        scorePane = new JTextPane();
        scorePane.setEditable(false);
        scorePane.setFont(new Font("Rockwell Extra Bold", Font.PLAIN, 72));
        scorePane.setText("0");
        panel1.add(scorePane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(250, 75), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
