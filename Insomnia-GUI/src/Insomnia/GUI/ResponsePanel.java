package Insomnia.GUI;


import Insomnia.Model.ResponseModel;
import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.Theme;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static org.fife.ui.rsyntaxtextarea.SyntaxConstants.*;


/**
 * response panel class
 */
public class ResponsePanel extends EmptyPanel {
    private ResponseModel model;
    private boolean ok;

    public ResponsePanel(ResponseModel model) {
        this.model = model;
        ok = !"null".equals(model.getStatus());
        configUpPanel();
        configCenterPanel();
    }

    private void configUpPanel() {
        upPanel.setLayout(new BorderLayout());
        JPanel upPanelBar = new UpPanelBar();
        upPanel.add(upPanelBar, BorderLayout.CENTER);
    }

    private class UpPanelBar extends JPanel {
        public UpPanelBar() {
            this.setLayout(new FlowLayout(FlowLayout.LEFT));
            int status;
            try {
                status = Integer.parseInt(model.getStatus().substring(0, 3));
            } catch (Exception e) {
                status = 504;
            }
            JButton[] labels = new JButton[3];
            labels[0] = new JButton(model.getStatus());
            labels[0].setOpaque(true);
            if (status / 100 == 2)
                labels[0].setBackground(new Color(96, 195, 103));
            else if (status / 100 == 4)
                labels[0].setBackground(new Color(195, 74, 68));
            else
                labels[0].setBackground(new Color(214, 208, 98));
            labels[1] = new JButton(model.getTime());
            labels[2] = new JButton(model.getDataTransferred().substring(0, 5) + " KB");
            for (int i = 0; i < 3; i++) {
                labels[i].setEnabled(false);
                labels[i].setMargin(new Insets(3, 2, 3, 2));
                this.add(labels[i]);
            }
        }
    }

    private void configCenterPanel() {
        centerPanel.setLayout(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();
        JPanel messageBody = new MessageBodyPanel();
        JPanel header = new Header();
        tabs.add(messageBody, "Message Body");
        tabs.add(header, "Header");
        centerPanel.add(tabs, BorderLayout.CENTER);

    }

    private class MessageBodyPanel extends JPanel {
        private JScrollPane preview;
        private RSyntaxTextArea rawBody;
        private RSyntaxTextArea json;

        public MessageBodyPanel() {
            this.setLayout(new BorderLayout());
            initViewPane();
            initJsonPane();
            initRawPane();
            JTabbedPane types = new JTabbedPane();
            types.setTabPlacement(JTabbedPane.RIGHT);
            types.addTab("View", preview);
            types.addTab("Raw", new RTextScrollPane(rawBody));
            types.addTab("Json", new RTextScrollPane(json));
            types.addMouseListener(new MouseAdapter() {
                /**
                 * {@inheritDoc}
                 *
                 * @param e
                 */
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    changeColor();
                }
            });
            this.add(types, BorderLayout.CENTER);
        }

        private void initViewPane() {
            JTextPane pane = new JTextPane();
            pane.setEditable(false);
            if (ok)
                if (model.getContentType().contains("image")) {
                    ImageIcon image = new ImageIcon(model.getBody());
                    pane.setAlignmentX(JTextPane.CENTER_ALIGNMENT);
                    pane.insertIcon(image);
                } else {
                    pane.setContentType("text/html");
                    pane.setText(model.getBodyAsString());
                }
            preview = new JScrollPane(pane);
        }

        private void initJsonPane() {
            json = new RSyntaxTextArea();
            json.setSyntaxEditingStyle(SYNTAX_STYLE_JSON);
            json.setShowMatchedBracketPopup(true);
            json.setEditable(false);
            json.setCodeFoldingEnabled(true);
            if (ok)
                if (model.getContentType().contains("json")) {
                    try {
                        json.setText(new JSONObject(model.getBodyAsString()).toString(4));
                    } catch (JSONException e) {
                        json.setText(model.getBodyAsString());
                    }
                } else
                    json.setText("json not found!!!");
        }

        private void initRawPane() {
            rawBody = new RSyntaxTextArea();
            rawBody.setSyntaxEditingStyle(SYNTAX_STYLE_HTML);
            rawBody.setShowMatchedBracketPopup(true);
            rawBody.setEditable(false);
            rawBody.setCodeFoldingEnabled(true);
            if (ok)
                try {
                    rawBody.setText(new JSONObject(model.getBodyAsString()).toString(4));
                } catch (JSONException e) {
                    rawBody.setText(model.getBodyAsString());
                }
            //rawBody.setText(model.getBodyAsString());
        }

        private void changeColor() {
            ArrayList<Theme> themes = new ArrayList<Theme>(Arrays.asList(LafManager.getRegisteredThemes()));
            int index = themes.indexOf(LafManager.getTheme());
            String themePath = "/org/fife/ui/rsyntaxtextarea/themes/dark.xml";
            if (index < 2 || index == 5)
                themePath = "/org/fife/ui/rsyntaxtextarea/themes/eclipse.xml";
            try {
                org.fife.ui.rsyntaxtextarea.Theme theme = org.fife.ui.rsyntaxtextarea.Theme
                        .load(getClass().getResourceAsStream(themePath));
                theme.apply(rawBody);
                theme.apply(json);
                rawBody.revalidate();
                json.revalidate();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

    }

    /**
     * inner class for header panel
     */
    private class Header extends JPanel {
        JTable headers;
        JButton copy;

        public Header() {
            headers = new JTable(toTableModel(model.getHeaders()));
            headers.setDefaultEditor(Object.class, null);
            configCopyButton();
            JScrollPane tableScroller = new JScrollPane(headers);
            this.setLayout(new BorderLayout());
            this.add(tableScroller, BorderLayout.CENTER);
            this.add(copy, BorderLayout.SOUTH);
        }

        private TableModel toTableModel(Map<?, ?> map) {
            DefaultTableModel model = new DefaultTableModel(
                    new Object[]{"Key", "Value"}, 0
            );
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                model.addRow(new Object[]{entry.getKey(), entry.getValue()});
            }
            return model;
        }

        private void configCopyButton() {
            copy = new JButton("Copy To ClipBoard");
            copy.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    StringSelection stringSelection = new StringSelection(dataToString());
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, null);
                }
            });
        }

        private String dataToString() {
            StringBuilder toReturn = new StringBuilder("\"");
            model.getHeaders().forEach((k, v) ->
                    toReturn.append(k).append(":")
                            .append(v).append(";")
            );
            toReturn.deleteCharAt(toReturn.length() - 1).append("\"");
            return toReturn.toString();
        }
    }

}
