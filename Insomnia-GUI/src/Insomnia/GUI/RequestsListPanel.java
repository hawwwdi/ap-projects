package Insomnia.GUI;

import javax.swing.*;
import java.awt.*;


/**
 * the request list class
 */
public class RequestsListPanel extends EmptyPanel {
    WorkSpace currentSpace;


    public RequestsListPanel(JButton insomnia) {
        this.upPanel.setLayout(new BorderLayout());
        this.upPanel.add(insomnia, BorderLayout.CENTER);
        this.centerPanel.setLayout(new BorderLayout());
    }

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

    public String getName() {
        return currentSpace.getName();
    }

    public void setName(String name) {
        currentSpace.setName(name);
    }
}
