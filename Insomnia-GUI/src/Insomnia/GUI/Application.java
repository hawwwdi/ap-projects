package Insomnia.GUI;

import Insomnia.Utils.FileUtils;
import com.github.weisj.darklaf.*;
import com.github.weisj.darklaf.theme.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * the application class
 *
 * @author hadi
 * @version 1.0
 */
public class Application extends JFrame {
    private InsomniaButton insomnia;
    private RequestsListPanel current;
    private JSplitPane rightSplit;
    private JSplitPane leftSplit;
    private JPanel framePanel;
    private ArrayList<WorkSpace> workSpaces;
    private CloseOperation closeOperation;
    public static boolean FOLLOW_REDIRECT;


    public Application() {
        changeLookAndFeel();
        framePanel = new JPanel(new BorderLayout());
        workSpaces = new ArrayList<>();
        closeOperation = new CloseOperation();
        rightSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new EmptyRequestSettingPanel(), new EmptyPanel());
        rightSplit.setDividerSize(4);
        rightSplit.setResizeWeight(0.5f);
        insomnia = new InsomniaButton();
        leftSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, current, rightSplit);
        leftSplit.setDividerSize(4);
        leftSplit.setResizeWeight(0.33f);
        framePanel.add(leftSplit, BorderLayout.CENTER);
        this.addWindowListener(closeOperation);
        this.setJMenuBar(getToolBar());
        this.setLayout(new BorderLayout());
        this.add(framePanel, BorderLayout.CENTER);
        this.setTitle("Insomnia");
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("icons\\icon.png"));
        this.setSize(1100, 550);
        this.setVisible(true);
        rightSplit.setDividerLocation(rightSplit.getWidth() / 2);
        leftSplit.setDividerLocation(leftSplit.getWidth() / 3);
        configShutDownHook();
    }

    private void configShutDownHook() {
        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(new Thread() {

            @Override
            public void run() {
                super.run();
                System.out.println("saving...");
                for (WorkSpace curr : workSpaces)
                    curr.save();
                int index = Arrays.asList(LafManager.getRegisteredThemes()).indexOf(LafManager.getTheme());
                FileUtils.saveObject(FOLLOW_REDIRECT + " "
                        + closeOperation.exitOnClose + " "
                        + index, "option");
                System.out.println("saved!");
            }
        });
    }


    private JMenuBar getToolBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu Application = getApplicationMenu();
        JMenu View = getViewMenu();
        JMenu Help = getHelpMenu();
        menuBar.add(Application);
        menuBar.add(View);
        menuBar.add(Help);
        Font f = new Font("ITCEDSCR", 0, 11);
        UIManager.put("Menu.font", f);
        menuBar.setPreferredSize(new Dimension(100, 25));
        return menuBar;
    }


    private JMenu getApplicationMenu() {
        JMenu Application = new JMenu("Application");
        JMenuItem Exit = new JMenuItem("Exit", 'e');
        Exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_MASK));
        Exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeOperation.doAction();
            }
        });
        JMenuItem Option = getOptionItem();
        Application.add(Option);
        Application.add(Exit);
        Application.setMnemonic(KeyEvent.VK_A);
        return Application;
    }

    
    private JMenuItem getOptionItem() {
        String[] options = new String[0];
        try {
            options = FileUtils.loadObject("option").split(" ");
        } catch (FileNotFoundException e) {
            options = "false true 1".split(" ");
        }
        FOLLOW_REDIRECT = Boolean.parseBoolean(options[0]);
        closeOperation.changeCloseOperation(Boolean.parseBoolean(options[1]));
        JMenuItem toReturn = new JMenuItem("Option", 'o');
        toReturn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
        JDialog option = new JDialog(this, "Option", true);
        option.setLayout(new BorderLayout());
        JCheckBox followRedirectButton = new JCheckBox("follow redirect");
        followRedirectButton.setSelected(FOLLOW_REDIRECT);
        JButton ok = new JButton("Ok");
        JPanel themeSelector = getThemeSelector(ok);
        option.getRootPane().setDefaultButton(ok);
        JCheckBox exitState = new JCheckBox("Exit on close");
        exitState.setSelected(Boolean.parseBoolean(options[1]));
        exitState.setSelected(true);
        JPanel southPane = new JPanel(new BorderLayout());
        southPane.add(exitState, BorderLayout.WEST);
        southPane.add(followRedirectButton, BorderLayout.CENTER);
        southPane.add(ok, BorderLayout.EAST);
        JPanel toSet = new JPanel(new BorderLayout());
        toSet.add(themeSelector, BorderLayout.CENTER);
        toSet.add(southPane, BorderLayout.SOUTH);
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FOLLOW_REDIRECT = followRedirectButton.isSelected();
                closeOperation.changeCloseOperation(exitState.isSelected());
                option.setVisible(false);
            }
        });
        toReturn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                option.setVisible(true);
            }
        });
        option.add(toSet, BorderLayout.CENTER);
        option.setSize(500, 200);
        option.setLocation(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2);
        return toReturn;
    }


    private JPanel getThemeSelector(JButton okButton) {
        JPanel themeSelector = new JPanel(new GridLayout(2, 4));
        themeSelector.setBorder(new TitledBorder(new LineBorder(Color.black, 2), "Theme"));
        Theme[] themes = LafManager.getRegisteredThemes();
        JRadioButton[] buttons = new JRadioButton[7];
        ButtonGroup bg = new ButtonGroup();
        for (int i = 0; i < 7; i++) {
            buttons[i] = new JRadioButton(themes[i].getName());
            bg.add(buttons[i]);
            themeSelector.add(buttons[i]);
        }
        buttons[1].setSelected(true);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 7; i++) {
                    if (buttons[i].isSelected()) {
                        LafManager.install(themes[i]);
                        for (WorkSpace current : workSpaces)
                            current.updateTheme();
                    }
                }
            }
        });
        return themeSelector;
    }

    
    private JMenu getViewMenu() {
        JMenu view = new JMenu("View");
        JMenuItem fullScreen = new JMenuItem("Toggle Full Screen");
        fullScreen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK));
        fullScreen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Application.this.getExtendedState() == JFrame.MAXIMIZED_BOTH)
                    setExtendedState(JFrame.NORMAL);
                else
                    setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });
        JMenuItem sideBar = new JCheckBoxMenuItem("Toggle Side Bar");
        sideBar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
        sideBar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sideBar.isSelected()) {
                    framePanel.remove(leftSplit);
                    leftSplit.remove(rightSplit);
                    framePanel.add(rightSplit, BorderLayout.CENTER);
                } else {
                    framePanel.remove(rightSplit);
                    leftSplit.setRightComponent(rightSplit);
                    framePanel.add(leftSplit, BorderLayout.CENTER);
                }
                framePanel.updateUI();
            }
        });
        view.add(fullScreen);
        view.add(sideBar);
        view.setMnemonic(KeyEvent.VK_V);
        return view;
    }


    private JMenu getHelpMenu() {
        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_MASK));
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String details = "Author: hadi\nContact us: hawwwdi@gmail.com";
                JOptionPane.showMessageDialog(Application.this, details, "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        JMenuItem Help = new JMenuItem("Help");
        Help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_MASK));
        Help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //todo change details
                String details = "Hello World :)";
                JOptionPane.showMessageDialog(Application.this, details, "Help", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        help.add(about);
        help.add(Help);
        help.setMnemonic(KeyEvent.VK_H);
        return help;
    }

    
    private void changeLookAndFeel() {
        String[] options = new String[0];
        try {
            options = FileUtils.loadObject("option").split(" ");
        } catch (FileNotFoundException e) {
            options = "false true 1".split(" ");
        }
        LafManager.install(LafManager.getRegisteredThemes()[Integer.parseInt(options[2])]);
    }

    /**
     * inner class to create insomnia button
     */
    private class InsomniaButton extends JButton {
        private JLabel workSpaceName;
        private JPopupMenu insomniaMenu;
        private JMenu workSpaceSelector;
        private ArrayList<JMenuItem> menuItems;


        public InsomniaButton() {
            this.setOpaque(true);
            this.setBackground(new Color(186, 76, 255));
            current = new RequestsListPanel(this);
            workSpaceName = new JLabel();
            workSpaceSelector = new JMenu("Work Spaces");
            menuItems = new ArrayList<>();
            this.setLayout(new BorderLayout());
            this.setPreferredSize(new Dimension(200, 40));
            this.setHorizontalAlignment(JButton.RIGHT);
            this.setText("▼  ");
            this.add(workSpaceName, BorderLayout.WEST);
            this.insomniaMenu = getInsomniaMenu();
            this.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    insomniaMenu.show(insomnia, insomnia.getX(), insomnia.getY() + insomnia.getHeight());
                }
            });
            if (!load())
                this.addWorkSpace(new WorkSpace("Insomnia", rightSplit));
        }

        private boolean load() {
            if (FileUtils.getGuiWorkSpaces().length == 0)
                return false;
            workSpaces.clear();
            workSpaceSelector.removeAll();
            menuItems.clear();
            for (File curr : FileUtils.getGuiWorkSpaces()) {
                WorkSpace toLoad = new WorkSpace(curr, rightSplit);
                addWorkSpace(toLoad);
            }
            return true;
        }

        
        private JPopupMenu getInsomniaMenu() {
            JPopupMenu toReturn = new JPopupMenu();
            JMenuItem WorkSpaceSetting = new JMenuItem("Change WorkSpace Name");
            JMenuItem NewWorkSpace = new JMenuItem("New Work Space     ➕");
            NewWorkSpace.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newWorkSpaceName = JOptionPane.showInputDialog(Application.this, "Enter New WorkSpace Name");
                    if (Pattern.matches("\\w[\\s\\w]*", newWorkSpaceName)) {
                        WorkSpace newSpace = new WorkSpace(newWorkSpaceName, rightSplit);
                        addWorkSpace(newSpace);
                    } else
                        JOptionPane.showMessageDialog(Application.this, "enter valid name!", "Alert", JOptionPane.WARNING_MESSAGE);
                }
            });
            WorkSpaceSetting.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newName = JOptionPane.showInputDialog(Application.this, "Enter New Name");
                    if (Pattern.matches("\\w[\\s\\w]*", newName)) {
                        for (JMenuItem tmp : menuItems)
                            if (current.getName().equals(tmp.getText()))
                                tmp.setText(newName);
                        current.setName(newName);
                        updateButton();
                    } else
                        JOptionPane.showMessageDialog(Application.this, "enter valid name!", "Alert", JOptionPane.WARNING_MESSAGE);
                }
            });

            toReturn.add(WorkSpaceSetting);
            toReturn.add(NewWorkSpace);
            toReturn.add(workSpaceSelector);
            return toReturn;
        }


        private void updateButton() {
            this.workSpaceName.setText(current.getName());
        }

        
        private void setCurrentWorkSpace(WorkSpace current) {
            Application.this.current.setSpace(current);
            updateButton();
            this.setBackground(new Color(186, 76, 255));
        }

        
        private void addWorkSpace(WorkSpace newWorkSpace) {
            workSpaces.add(newWorkSpace);
            setCurrentWorkSpace(newWorkSpace);
            JMenuItem item = new JMenuItem(newWorkSpace.getName());
            menuItems.add(item);
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String name = ((JMenuItem) e.getSource()).getText();
                    for (WorkSpace tmp : workSpaces) {
                        if (tmp.equalsByName(name))
                            setCurrentWorkSpace(tmp);
                    }
                }
            });
            workSpaceSelector.add(item);
        }
    }

    
    private class CloseOperation extends WindowAdapter {
        private SystemTray systemTray;
        private TrayIcon trayIcon;
        private PopupMenu trayMenu;
        private boolean exitOnClose;

        public CloseOperation() {
            this.systemTray = SystemTray.getSystemTray();
            configTrayMenu();
            configTrayIcon();
            exitOnClose = true;
        }

        private void configTrayMenu() {
            trayMenu = new PopupMenu();
            MenuItem exit = new MenuItem("EXIT");
            MenuItem show = new MenuItem("SHOW");
            exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Application.this.dispose();
                    System.exit(0);
                }
            });
            show.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Application.this.setVisible(true);
                    systemTray.remove(trayIcon);
                    Application.this.setExtendedState(JFrame.NORMAL);
                }
            });
            trayMenu.add(show);
            trayMenu.add(exit);
        }

        private void configTrayIcon() {
            Image icon = Toolkit.getDefaultToolkit().getImage("icons\\Icon.png");
            trayIcon = new TrayIcon(icon, "Insomnia", trayMenu);
            trayIcon.setImageAutoSize(true);
        }

        public void changeCloseOperation(boolean exitOnClose) {
            this.exitOnClose = exitOnClose;
        }

        public void doAction() {
            if (!exitOnClose) {
                try {
                    Application.this.setVisible(false);
                    systemTray.add(trayIcon);
                } catch (AWTException awtException) {
                    JOptionPane.showMessageDialog(Application.this, awtException.getMessage(), "Warning!", JOptionPane.WARNING_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(Application.this, e.getMessage(), "Warning!", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                Application.this.dispose();
                System.exit(0);
            }
        }

        @Override
        public void windowClosing(WindowEvent e) {
            super.windowClosing(e);
            this.doAction();
        }
    }


}
