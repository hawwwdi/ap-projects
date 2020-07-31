package Insomnia.GUI;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.io.Serializable;

/**
 * the empty request setting panel
 * it shows when any request selected
 */
public class EmptyRequestSettingPanel extends EmptyPanel  {
    /**
     * constructor for this class
     * it use to create new object of this class
     */
    public EmptyRequestSettingPanel() {
        configCenterPanel();
    }

    /**
     * it use to config center panel of this class
     */
    private void configCenterPanel() {
        centerPanel.setLayout(new BorderLayout());
        JPanel center = new JPanel(new GridLayout(3, 1));
        JLabel[] labels = new JLabel[3];
        labels[0] = new JLabel("New Request             Ctrl + N", JLabel.CENTER);
        labels[1] = new JLabel("Switch Request             Ctrl + P", JLabel.CENTER);
        labels[2] = new JLabel("Edit  Environment             Ctrl + E", JLabel.CENTER);
        for (int i = 0; i < 3; i++) {
            labels[i].setBorder(new EtchedBorder(EtchedBorder.RAISED));
            center.add(labels[i]);
        }
        centerPanel.add(center, BorderLayout.CENTER);
        centerPanel.add(getCenterBottomPanel(), BorderLayout.SOUTH);
    }

    /**
     * it use to create import and new req button object
     * and put these in a panel
     *
     * @return panel of these buttons
     */
    private JPanel getCenterBottomPanel() {
        JPanel toReturn = new JPanel(new GridLayout(1, 2, 5, 30));
        JButton importButton = new JButton("➕ Import From File");
        JButton newReq = new JButton("➕ New Request");
        //todo add action listener
        toReturn.add(importButton);
        toReturn.add(newReq);
        return toReturn;
    }
}