package Insomnia.GUI;

import Insomnia.Model.RequestModel;
import Insomnia.Model.ResponseModel;
import Insomnia.Utils.Method;
import Insomnia.commandLine.RequestSender;
import com.github.weisj.darklaf.components.text.NumberedTextComponent;
import org.apache.commons.validator.routines.UrlValidator;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

/**
 * request seting panel class
 */
public class RequestSettingPanel extends EmptyPanel {
    private RequestModel model;
    private JPanel responsePanel;
    private BodyPanel body;
    private HeaderPanel header;
    private QueryPanel query;

    
    public RequestSettingPanel(RequestModel model, JPanel responsePanel) {
        this.model = model;
        this.responsePanel = responsePanel;
        configUpPanel();
        configCenterPanel();
        loadModel();
    }

    private void loadModel() {
        this.header.setHeaders(model.getHeaders(), ";", ":");
        this.query.setQueries(model.getQuery(), "&", "=");
        this.body.loadBody(model.getMessageBody());
    }

    private void configUpPanel() {
        upPanel.setLayout(new BorderLayout());
        SendRequestPanel up = new SendRequestPanel();
        upPanel.add(up, BorderLayout.CENTER);
    }

    private void configCenterPanel() {
        centerPanel.setLayout(new BorderLayout());
        JTabbedPane centerTabs = new JTabbedPane();
        body = new BodyPanel();
        header = new HeaderPanel(
                new FocusAdapter() {
                    /**
                     * Invoked when a component loses the keyboard focus.
                     *
                     * @param e
                     */
                    @Override
                    public void focusLost(FocusEvent e) {
                        super.focusLost(e);
                        String toSet = toForm(":", ";", ((HeaderPanel) header).getDetails().split(" "));
                        model.setHeaders(toSet);
                    }
                });
        JPanel proxy = new ProxyPanel();
        query = new QueryPanel();
        centerTabs.add(body, "Body");
        centerTabs.add(query, "Query");
        centerTabs.add(header, "Header");
        centerTabs.add(proxy, "Proxy");
        centerPanel.add(centerTabs, BorderLayout.CENTER);
    }

    private String toForm(String delimiter1, String delimiter2, String... data) {
        int i = 0;
        StringBuilder toReturn = new StringBuilder("\"");
        for (String current : data) {
            toReturn.append(current);
            if (i++ % 2 == 0)
                toReturn.append(delimiter1);
            else
                toReturn.append(delimiter2);
        }
        return toReturn.deleteCharAt(toReturn.length() - 1).append("\"").toString();
    }

    /**
     * this is inner class for send request panel
     */
    private class SendRequestPanel extends JPanel {
        private JComboBox methods;
        private JTextField address;
        private JButton send;

        public SendRequestPanel() {
            this.setLayout(new BorderLayout());
            configMethodsComboBox();
            configUrlField();
            configSendButton();
            this.add(methods, BorderLayout.WEST);
            this.add(address, BorderLayout.CENTER);
            this.add(send, BorderLayout.EAST);
        }

        private void configUrlField() {
            address = new JTextField("Address");
            if (!"".equals(model.getUrl()))
                address.setText(model.getUrl());
            address.addKeyListener(new KeyAdapter() {
                /**
                 * Invoked when a key has been released.
                 *
                 * @param e
                 */
                @Override
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    String url = address.getText().trim();
                    model.setUrl(url);
                    boolean valid = UrlValidator.getInstance().isValid(address.getText().trim());
                    if (valid) {
                        send.setEnabled(true);
                        send.setForeground(new Color(123, 226, 106, 255));
                    } else {
                        send.setEnabled(false);
                        send.setForeground(new Color(226, 88, 76, 255));
                    }
                }
            });
        }

        private void configMethodsComboBox() {
            methods = new JComboBox<>(Method.values());
            methods.setSelectedItem(model.getMethod());
            methods.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    Method selected = ((Method) e.getItem());
                    model.setMethod(selected);
                }
            });
            methods.setPreferredSize(new Dimension(90, 40));
        }

        private void configSendButton() {
            send = new JButton("Send  ➤");
            send.setEnabled(UrlValidator.getInstance().isValid(model.getUrl()));
            send.setOpaque(true);
            send.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    model.setFollowRedirect(Application.FOLLOW_REDIRECT);
                    System.out.println(model.toString());
                    new SwingWorker<ResponseModel, Object>() {
                        @Override
                        protected ResponseModel doInBackground() throws Exception {
                            responsePanel.removeAll();
                            responsePanel.setLayout(new BorderLayout());
                            ImageIcon loading = new ImageIcon("icons\\loading.gif");
                            responsePanel.add(new JLabel(loading), BorderLayout.CENTER);
                            responsePanel.updateUI();
                            return new RequestSender(model).send();
                        }

                        /**
                         * Executed on the <i>Event Dispatch Thread</i> after the {@code doInBackground}
                         * method is finished. The default
                         * implementation does nothing. Subclasses may override this method to
                         * perform completion actions on the <i>Event Dispatch Thread</i>. Note
                         * that you can query status inside the implementation of this method to
                         * determine the result of this task or whether this task has been cancelled.
                         *
                         * @see #doInBackground
                         * @see #isCancelled()
                         * @see #get
                         */
                        @Override
                        protected void done() {
                            super.done();
                            responsePanel.removeAll();
                            responsePanel.revalidate();
                            responsePanel.setLayout(new BorderLayout());
                            try {
                                responsePanel.add(new ResponsePanel(get()));
                            } catch (InterruptedException | ExecutionException ex) {
                                System.out.println("exception handled");
                                ex.printStackTrace();
                            }
                            responsePanel.updateUI();
                        }
                    }.execute();
                }
            });
        }

    }

    /**
     * inner class for body panel
     */
    private class BodyPanel extends JPanel {
        private HeaderPanel formData;
        private NumberedTextComponent editor;
        private NumberedTextComponent jsonPanel;
        private SelectFilePane binary;

        public BodyPanel() {
            this.setLayout(new BorderLayout());
            initPanels();
            JTabbedPane types = new JTabbedPane();
            types.setTabPlacement(JTabbedPane.RIGHT);
            types.addTab("Text", editor);
            types.addTab("Form", formData);
            types.addTab("Json", jsonPanel);
            types.addTab("File", binary);
            this.add(types, BorderLayout.CENTER);
        }

        private void initPanels() {
            editor = new NumberedTextComponent(new JTextPane());
            formData = new HeaderPanel(new FocusAdapter() {
                /**
                 * Invoked when a component loses the keyboard focus.
                 *
                 * @param e
                 */
                @Override
                public void focusLost(FocusEvent e) {
                    super.focusLost(e);
                    String toSet = toForm("=", "&", formData.getDetails().split(" "));
                    model.setMessageBody(toSet);
                }
            });
            jsonPanel = new NumberedTextComponent(new JTextPane());
            binary = new SelectFilePane();
            editor.getTextComponent().addKeyListener(new KeyAdapter() {
                /**
                 * Invoked when a key has been typed.
                 * This event occurs when a key press is followed by a key release.
                 *
                 * @param e
                 */
                @Override
                public void keyTyped(KeyEvent e) {
                    super.keyTyped(e);
                    String data = toForm("=", "&", editor.getTextComponent().getText().replaceAll("", " "));
                    model.setMessageBody(data);
                }
            });
            jsonPanel.getTextComponent().addKeyListener(new KeyAdapter() {
                /**
                 * Invoked when a key has been typed.
                 * This event occurs when a key press is followed by a key release.
                 *
                 * @param e
                 */
                @Override
                public void keyTyped(KeyEvent e) {
                    super.keyTyped(e);
                    String toSet = toForm("=", "&", "Json", jsonPanel.getTextComponent().getText().replaceAll(" ", ""));
                    model.setMessageBody(toSet);
                }
            });
        }

        public void loadBody(String body) {
            if (body.contains("file"))
                binary.setPath(body.trim().substring(5));
            else if (body.contains("Json"))
                jsonPanel.getTextComponent().setText(body.trim().substring(5));
            else if (Pattern.matches("(\\w+[=]\\w+&?)+", body))
                formData.setHeaders(body.replaceAll("\"", ""), "&", "=");
            else
                editor.getTextComponent().setText(body);

        }

        class SelectFilePane extends JPanel {
            private JLabel path;
            private JFileChooser fc;

            public SelectFilePane() {
                fc = new JFileChooser(".");
                fc.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return true;
                    }

                    @Override
                    public String getDescription() {
                        return "select a file";
                    }
                });
                path = new JLabel();
                path.setBorder(new TitledBorder(new LineBorder(Color.black, 1), "path"));
                path.setEnabled(false);
                this.setLayout(new BorderLayout());
                this.add(path, BorderLayout.NORTH);
                this.add(getCenterPane(), BorderLayout.CENTER);
            }

            public void setPath(String path) {
                this.path.setText(path);
            }

            private JPanel getCenterPane() {
                JPanel center = new JPanel(new BorderLayout());
                JButton choose = new JButton("Choose File");
                choose.setMargin(new Insets(0, 0, 10, 10));
                choose.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int i = fc.showOpenDialog(RequestSettingPanel.this);
                        if (i == JFileChooser.APPROVE_OPTION) {
                            File f = fc.getSelectedFile();
                            path.setText(f.getAbsolutePath());
                            model.setMessageBody(toForm("=", "&", "file", f.getAbsolutePath()));
                        }
                    }
                });
                center.add(choose, BorderLayout.NORTH);
                return center;
            }
        }

    }

    /**
     * inner class for header panel
     */
    private class HeaderPanel extends JPanel {
        private JPanel headersPanel;
        private LinkedList<Header> headers;
        private int lastHeaderIndex;
        private FocusAdapter action;

        public HeaderPanel(FocusAdapter action) {
            this.action = action;
            headersPanel = new JPanel();
            headersPanel.setLayout(new BoxLayout(headersPanel, BoxLayout.Y_AXIS));
            headers = new LinkedList<>();
            lastHeaderIndex = 0;
            Header tmp = new Header(action);
            tmp.setVisible(true);
            headers.add(tmp);
            headersPanel.add(tmp);
            for (int i = 0; i < 9; i++) {
                tmp = new Header(action);
                headers.add(tmp);
                headersPanel.add(tmp);
            }
            JScrollPane scroller = new JScrollPane(headersPanel);
            this.setLayout(new BorderLayout());
            this.add(scroller, BorderLayout.CENTER);
        }

        public void setHeaders(String headers, String delimiter1, String delimiter2) {
            String[] toSet = headers.split(delimiter1);
            for (int i = 0; i < toSet.length; i++) {
                if (toSet[i].contains(delimiter2)) {
                    this.headers.get(i).setDetails(toSet[i].split(delimiter2));
                    addHeader();
                }
            }
        }

        private void addHeader() {
            if (lastHeaderIndex < 9) {
                headers.get(lastHeaderIndex + 1).setVisible(true);
            } else {
                Header tmp = new Header(action);
                tmp.setVisible(true);
                headers.add(tmp);
                headersPanel.add(tmp);
            }
            lastHeaderIndex++;
            updateUI();
        }

        private void removeHeader(Header toRemove) {
            if (lastHeaderIndex != 0) {
                headers.remove(toRemove);
                headersPanel.remove(toRemove);
                updateUI();
                lastHeaderIndex--;
                int index = headers.size();
                while (index < 11) {
                    Header tmp = new Header(action);
                    headers.add(tmp);
                    headersPanel.add(tmp);
                    updateUI();
                    index++;
                }
            }
        }

        public String getDetails() {
            StringBuilder toReturn = new StringBuilder("");
            for (Header current : headers)
                if (current.isEnable()) {
                    toReturn.append(current.getDetails());
                }
            return toReturn.toString();
        }

        private class Header extends JPanel {
            private JTextField header;
            private JTextField value;
            private JCheckBox checkBox;
            private JButton delete;
            private boolean enable;

            public Header(FocusAdapter action) {
                this.setLayout(new GridBagLayout());
                header = new JTextField(1);
                value = new JTextField(1);
                value.setText("");
                header.addFocusListener(action);
                value.addFocusListener(action);
                checkBox = new JCheckBox();
                checkBox.setSelected(true);
                checkBox.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        enable = e.getStateChange() == 1;
                        header.setEnabled(enable);
                        value.setEnabled(enable);
                    }
                });
                delete = new JButton("×");
                setVisible(false);
                configActions();
                GridBagConstraints c = new GridBagConstraints();
                c.weightx = 1;
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = 1;
                c.gridwidth = 5;
                c.ipady = 20;
                c.ipadx = 10;
                c.gridx = 0;
                c.gridy = 0;
                this.add(header, c);
                c.gridx = 5;
                this.add(value, c);
                value.setHorizontalAlignment(JTextField.LEFT);
                c.ipady = 0;
                c.ipadx = 10;
                c.gridwidth = 1;
                c.gridx = 11;
                this.add(checkBox);
                c.gridx = 11;
                this.add(delete);
                delete.setMargin(new Insets(8, 5, 7, 5));
                // this.isEmpty = true;
                this.enable = true;
            }

            public void setVisible(boolean visible) {

                if (visible) {
                    header.setVisible(true);
                    value.setVisible(true);
                    checkBox.setVisible(true);
                    delete.setVisible(true);
                } else {
                    header.setVisible(false);
                    value.setVisible(false);
                    checkBox.setVisible(false);
                    delete.setVisible(false);
                }
                HeaderPanel.this.updateUI();
            }

            private void configActions() {
                Header current = this;
                delete.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        delete.setOpaque(true);
                        if (e.getClickCount() == 1)
                            delete.setBackground(Color.RED);
                        else if (e.getClickCount() == 2)
                            removeHeader(current);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        super.mouseExited(e);
                        delete.setBackground(Color.LIGHT_GRAY);
                    }
                });
                class Actions extends FocusAdapter {
                    /**
                     * Invoked when a component gains the keyboard focus.
                     *
                     * @param e
                     */
                    @Override
                    public void focusGained(FocusEvent e) {
                        super.focusGained(e);
                        if (headers.indexOf(current) == lastHeaderIndex)
                            addHeader();
                    }
                }
                header.addFocusListener(new Actions());
                value.addFocusListener(new Actions());
            }

            public boolean isEnable() {
                return enable && !"".equals(header.getText());
            }

            public String getDetails() {
                return header.getText() + " " + value.getText() + " ";
            }

            public void setDetails(String[] details) {
                header.setText(details[0]);
                value.setText(details[1]);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Header header1 = (Header) o;
                return //isEmpty == header1.isEmpty &&
                        Objects.equals(header, header1.header) &&
                                Objects.equals(value, header1.value) &&
                                Objects.equals(checkBox, header1.checkBox) &&
                                Objects.equals(delete, header1.delete);
            }

        }
    }

    /**
     * inner class for query panel
     */
    private class QueryPanel extends JPanel {
        private HeaderPanel headerPanel;
        private JTextField urlReview;

        public QueryPanel() {
            this.setLayout(new BorderLayout());
            headerPanel = new HeaderPanel(new FocusAdapter() {
                /**
                 * Invoked when a component loses the keyboard focus.
                 *
                 * @param e
                 */
                @Override
                public void focusLost(FocusEvent e) {
                    super.focusLost(e);
                    String data = toForm("=", "&", headerPanel.getDetails().trim().split(" "));
                    model.setQuery(data.replaceAll("\"", ""));
                    updateUrlReview(data.replaceAll("\"", ""));
                }
            });
            urlReview = new JTextField("  ...");
            urlReview.setEditable(false);
            urlReview.setBorder(new TitledBorder(new LineBorder(Color.black, 1), "URL PREVIEW"));
            this.add(urlReview, BorderLayout.NORTH);
            this.add(headerPanel, BorderLayout.CENTER);
        }

        private void updateUrlReview(String query) {
            model.setQuery(query);
            urlReview.setText(model.getUrlWithQuery());
        }

        public void setQueries(String queries, String delimiter1, String delimiter2) {
            headerPanel.setHeaders(queries, delimiter1, delimiter2);
        }
    }

    /**
     * inner class for proxy panel
     */
    private class ProxyPanel extends JPanel {
        private JTextField ip;
        private JTextField port;
        private JCheckBox enable;
        private final String IP_REGEX = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

        public ProxyPanel() {
            this.setLayout(new BorderLayout());
            enable = new JCheckBox();
            ip = new JTextField();
            port = new JTextField();
            ip.setText(model.getServerIp());
            port.setText(String.valueOf(model.getPort()));
            boolean sendToServer = model.isSendToServer();
            enable.setSelected(sendToServer);
            ip.setEnabled(sendToServer);
            port.setEnabled(sendToServer);
            ip.setOpaque(true);
            port.setOpaque(true);
            ip.addKeyListener(new KeyAdapter() {
                /**
                 * Invoked when a key has been released.
                 *
                 * @param e
                 */
                @Override
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    String address = ip.getText();
                    model.setServerIp(address.trim());
                    if ((Pattern.matches(IP_REGEX, address) || "localhost".equals(address))) {
                        port.setForeground(new Color(123, 226, 106, 255));
                    } else
                        port.setForeground(new Color(226, 88, 76, 255));
                }
            });
            port.addKeyListener(new KeyAdapter() {
                /**
                 * Invoked when a key has been released.
                 *
                 * @param e
                 */
                @Override
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    String number = port.getText();
                    if (Pattern.matches("\\d+", number)) {
                        model.setPort(Integer.parseInt(number));
                        ip.setForeground(new Color(123, 226, 106, 255));
                    } else
                        ip.setForeground(new Color(226, 88, 76, 255));
                }
            });
            enable.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    boolean isSelected = enable.isSelected();
                    ip.setEnabled(isSelected);
                    port.setEnabled(isSelected);
                    model.setSendToServer(isSelected);
                }
            });
            this.add(getUpPanel(), BorderLayout.NORTH);
        }

        private JPanel getUpPanel() {
            JPanel toReturn = new JPanel(new GridLayout(3, 1, 20, 2));
            toReturn.add(getRowPanel(ip, " IP Address   "));
            toReturn.add(getRowPanel(port, " Port Number "));
            toReturn.add(getRowPanel(enable, " Enable "));
            return toReturn;
        }

        private JPanel getRowPanel(Component toAdd, String waterMark) {
            JPanel toReturn = new JPanel(new BorderLayout());
            toReturn.add(new JLabel(waterMark), BorderLayout.WEST);
            toReturn.add(toAdd, BorderLayout.CENTER);
            return toReturn;
        }
    }

}
