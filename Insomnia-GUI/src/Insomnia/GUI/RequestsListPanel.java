package Insomnia.GUI;

import javax.swing.*;
import java.awt.*;


/**
 * the request list class
 * it use to save insomnia button and current work space
 */
public class RequestsListPanel extends EmptyPanel {
    WorkSpace currentSpace;

    /**
     * constructor for this class to create object of this class
     * @param insomnia work space manager button
     */
    public RequestsListPanel(JButton insomnia) {
        this.upPanel.setLayout(new BorderLayout());
        this.upPanel.add(insomnia, BorderLayout.CENTER);
        this.centerPanel.setLayout(new BorderLayout());
    }

    /**
     * it use to change current work space
     * @param newSpace selected work space
     */
    public void setSpace(WorkSpace newSpace) {
        try {
            centerPanel.remove(currentSpace);
        } catch (NullPointerException e) {
            //for first time
        }
        this.centerPanel.add(newSpace, BorderLayout.CENTER);
        currentSpace = newSpace;
        this.updateUI();
    }

    /**
     * getter for current work space name
     * @return current work space name
     */
    public String getName() {
        return currentSpace.getName();
    }

    /**
     * setter for current work space name
     * @param name new name
     */
    public void setName(String name) {
        currentSpace.setName(name);
    }
}
