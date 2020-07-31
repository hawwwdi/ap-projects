package Insomnia.GUI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.Serializable;

/**
 * the empty panel class
 * it is base of  main panels of applicaton
 */
public class EmptyPanel extends JPanel  {
    protected JPanel upPanel;
    protected JPanel centerPanel;

    /**
     * constructor of this class to create new object of this class
     */
    public EmptyPanel() {
        this.setLayout(new BorderLayout());
        configPanels();
}

    /**
     * it use to config panels layout and size
     */
    private void configPanels() {
        upPanel = new JPanel();
        upPanel.setBorder(new LineBorder(Color.black, 1));
        upPanel.setLayout(new GridLayout());
        JLabel tmp = new JLabel(" ");
        tmp.setFont(new Font("Noto Sans Bold", 1, 30));
        upPanel.add(tmp);
        centerPanel = new JPanel();
        centerPanel.setBorder(new LineBorder(Color.black, 1));
        this.setLayout(new BorderLayout());
        this.add(upPanel, BorderLayout.NORTH);
        this.add(centerPanel,BorderLayout.CENTER);

    }
}
