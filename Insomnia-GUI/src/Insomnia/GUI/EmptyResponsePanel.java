package Insomnia.GUI;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class EmptyResponsePanel extends EmptyPanel {
    public EmptyResponsePanel(){
        configCenterPanel();
    }
    private void configCenterPanel(){
        centerPanel.setLayout(new BorderLayout());
        JPanel center= new JPanel(new GridLayout(4, 1));
        center.setBackground(Color.LIGHT_GRAY);
        JLabel[] labels= new JLabel[4];
        labels[0]= new JLabel("Send Request            Ctrl + Enter", JLabel.CENTER);
        labels[1]= new JLabel("Focus Url Bar           Ctrl + L", JLabel.CENTER);
        labels[2]= new JLabel("Manage Cookies          Ctrl + K", JLabel.CENTER);
        labels[3]= new JLabel("Edit  Environment       Ctrl + E", JLabel.CENTER);
        for (int i = 0; i <4 ; i++) {
            labels[i].setBorder(new EtchedBorder(EtchedBorder.RAISED));
            center.add(labels[i]);
        }
        centerPanel.add(center, BorderLayout.CENTER);
    }
}
