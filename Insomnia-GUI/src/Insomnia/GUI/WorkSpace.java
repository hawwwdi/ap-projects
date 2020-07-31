package Insomnia.GUI;

import Insomnia.Model.RequestModel;
import Insomnia.Utils.FileUtils;
import Insomnia.Utils.Method;
import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.components.text.SearchEvent;
import com.github.weisj.darklaf.components.text.SearchListener;
import com.github.weisj.darklaf.components.text.SearchTextField;
import com.github.weisj.darklaf.theme.IntelliJTheme;
import com.github.weisj.darklaf.theme.OneDarkTheme;
import com.github.weisj.darklaf.theme.Theme;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * the work space class
 * its extends JPanal
 * its hold a JTree of request and folders
 */
public class WorkSpace extends JPanel implements TreeSelectionListener {
    private String name;
    private JTree tree;
    private DefaultTreeModel rootModel;
    private ArrayList<DefaultMutableTreeNode> requests;
    private JPanel treePanel;
    private JSplitPane split;

    /**
     * constructor for this class
     *
     * @param name      work space name
     * @param showSpace split to show details of work spece requests
     */
    public WorkSpace(String name, JSplitPane showSpace) {
        this.name = name;
        this.split = showSpace;
        Folder root = new Folder(name);
        rootModel = new DefaultTreeModel(root);
        tree = new JTree(rootModel);
        tree.setCellRenderer(new Method.customTree());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        tree.addTreeSelectionListener(this);
        treePanel = new JPanel(new BorderLayout());
        treePanel.add(tree, BorderLayout.CENTER);
        JScrollPane scroller = new JScrollPane(treePanel);
        requests = new ArrayList<>();
        this.setLayout(new BorderLayout());
        this.add(new SearchBar(root), BorderLayout.NORTH);
        this.add(scroller, BorderLayout.CENTER);
    }

    /**
     * this is an overLoad for this class constructor
     * it use to load work spaces from save files
     * @param workSpace save file object
     * @param showSpace split pane to show
     */
    public WorkSpace(File workSpace, JSplitPane showSpace) {
        this.name = workSpace.getName();
        //todo check name
        this.split = showSpace;
        requests = new ArrayList<>();
        Folder root = new Folder(name);
        rootModel = new DefaultTreeModel(root);
        tree = new JTree(rootModel);
        tree.setCellRenderer(new Method.customTree());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        tree.addTreeSelectionListener(this);
        treePanel = new JPanel(new BorderLayout());
        treePanel.add(tree, BorderLayout.CENTER);
        JScrollPane scroller = new JScrollPane(treePanel);
        loadRoot(workSpace, root);
        this.setLayout(new BorderLayout());
        this.add(new SearchBar(root), BorderLayout.NORTH);
        this.add(scroller, BorderLayout.CENTER);
    }

    /**
     * it use to load JTree root from saved files
     * @param workSpace saved file folder
     * @param root JTree root node
     */
    private void loadRoot(File workSpace, Folder root) {
        for (File curr : workSpace.listFiles()) {
            if (curr.isDirectory()) {
                Folder folder = new Folder(curr.getName());
                root.addReq(folder);
                HashMap<String, RequestModel> map = FileUtils.loadRequests(workSpace.getName(), curr.getName());
                map.forEach((name, req) -> folder.addReq(new Request(name.split("\\.")[0], req, split)));
            } else if (curr.getParentFile().getName().equals(name)) {
                RequestModel model = new RequestModel(false);
                FileUtils.loadReq(model, curr);
                root.addReq(new Request(curr.getName().split("\\.")[0], model, split));
            }
        }
    }

    /**
     * the save method
     * it use to save work spaces details in a folder
     * it use the file utils class
     */
    public void save() {
        for (DefaultMutableTreeNode folder : requests) {
            if (folder instanceof Folder) {
                for (int i = 0; i < folder.getChildCount(); i++) {
                    Request request = (Request) (folder.getChildAt(i));
                    FileUtils.saveRequest(request.getModel(), name, ((Folder) folder).name, request.toString());
                }
            } else {
                Request request = (Request) folder;
                FileUtils.saveRequest(request.getModel(), name, "", request.toString());
            }
        }
    }

    /**
     * setter for work spacce name
     *
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
        ((Folder) rootModel.getRoot()).setName(name);
    }

    /**
     * getter for work space name
     *
     * @return work space name
     */
    public String getName() {
        return name;
    }

    /**
     * the method compares two work paces with their names
     *
     * @param name second work space name
     * @return compare result
     */
    public boolean equalsByName(String name) {
        return this.name.equals(name);
    }

    /**
     * this is event handler method for tree object
     *
     * @param e input event
     */
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        split.removeAll();
        if (selected instanceof Request) {
            ((Request) selected).select();
        } else {
            split.setLeftComponent(new EmptyRequestSettingPanel());
            split.setRightComponent(new EmptyPanel());
        }
        split.updateUI();
    }

    /**
     * the folder class
     * it use to groupingg requests
     */
    private class Folder extends DefaultMutableTreeNode {
        private String name;

        public Folder(String name) {
            super(name);
            this.name = name;
        }

        public void addReq(DefaultMutableTreeNode node) {
            requests.add(node);
            rootModel.insertNodeInto(node, this, this.getChildCount());
            tree.scrollPathToVisible(new TreePath(node.getPath()));
        }


        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * it use to change theme for all requests in this work space
     */
    public void updateTheme() {
        TreePath leaf = tree.getSelectionPath();
        Theme current = LafManager.getTheme();
        Theme second;
        if (current.equals(new IntelliJTheme()))
            second = new OneDarkTheme();
        else
            second = new IntelliJTheme();
        for (DefaultMutableTreeNode tmp : requests)
            if (tmp instanceof Request) {
                TreePath path = new TreePath(tmp.getPath());
                tree.setSelectionPath(path);
                LafManager.install(second);
                LafManager.install(current);
            }
        tree.setSelectionPath(leaf);
    }

    /**
     * the search bar class
     * it is search bar of requests list panel
     */
    private class SearchBar extends JPanel {
        private JComboBox folders;
        private JComboBox methods;
        private Folder root;
        private SearchTextField filter;

        /**
         * constructor for search bar class
         *
         * @param root root node of requests tree's
         */
        public SearchBar(Folder root) {
            this.setLayout(new BorderLayout());
            folders = new JComboBox();
            methods = new JComboBox(Method.values());
            this.root = root;
            folders.addItem(root);
            filter = getFilter();
            filter.setToolTipText("filter");
            JButton addReq = getAddReqButton();
            this.add(filter, BorderLayout.CENTER);
            this.add(addReq, BorderLayout.EAST);
        }

        /**
         * it use to config search text field and actions
         *
         * @return object of search text field
         */
        private SearchTextField getFilter() {
            SearchTextField toReturn = new SearchTextField("filter");
            toReturn.addSearchListener(new SearchListener() {
                @Override
                public void searchPerformed(SearchEvent searchEvent) {
                    String input = filter.getText();
                    if (Pattern.matches("\\w[\\s\\w]*", input))
                        showResult(input);
                }
            });
            return toReturn;
        }

        /**
         * it use to find result of searched string
         *
         * @param searchString user input string
         */
        private void showResult(String searchString) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            DefaultMutableTreeNode foundNode = null;
            int bookmark = -1;

            if (currentNode != null) {
                bookmark = requests.indexOf(currentNode);
            }

            for (int index = bookmark + 1; index < requests.size(); index++) {
                if (requests.get(index).toString().toLowerCase().contains(searchString.toLowerCase())) {
                    foundNode = requests.get(index);
                    break;
                }
            }

            if (foundNode == null) {
                for (int index = 0; index <= bookmark; index++) {
                    if (requests.get(index).toString().toLowerCase().contains(searchString.toLowerCase())) {
                        foundNode = requests.get(index);
                        break;
                    }
                }
            }
            if (foundNode != null) {
                TreePath path = new TreePath(foundNode.getPath());
                tree.setSelectionPath(path);
                tree.scrollPathToVisible(path);
            }
        }

        /**
         * it use to config add request button and add action to this button
         *
         * @return add req button object
         */
        private JButton getAddReqButton() {
            JButton add = new JButton("➕");
            add.setMargin(new Insets(0, 0, 2, 0));
            JPopupMenu Req = new JPopupMenu();
            JDialog addReqDialog = getAddReqDialog();
            add.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Req.show(WorkSpace.this, add.getX(), add.getY() + add.getHeight());
                }
            });
            JMenuItem addFolder = new JMenuItem("➕ New Folder");
            addFolder.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String folderName = JOptionPane.showInputDialog(WorkSpace.this, "Enter New Folder Name");
                    if (folderName != null) {
                        if (Pattern.matches("\\w[\\s\\w]*", folderName)) {
                            addFolder(folderName);
                        } else
                            JOptionPane.showMessageDialog(WorkSpace.this, "enter valid name!", "Alert", JOptionPane.WARNING_MESSAGE);

                    }
                }
            });
            JMenuItem addRequest = new JMenuItem("➕ New Request", 'n');
            addRequest.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK));
            addRequest.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addReqDialog.setVisible(true);
                }
            });
            Req.add(addFolder);
            Req.add(addRequest);
            return add;
        }

        /**
         * its use to create dialog object for add req button
         *
         * @return dialog object for add req button
         */
        private JDialog getAddReqDialog() {
            JDialog toReturn = new JDialog();
            JTextField reqName = new JTextField();
            JButton add = new JButton("add");
            JPanel center = new JPanel(new BorderLayout());
            toReturn.getRootPane().setDefaultButton(add);
            toReturn.setLayout(new BorderLayout());
            toReturn.setSize(400, 100);
            center.add(folders, BorderLayout.WEST);
            center.add(reqName, BorderLayout.CENTER);
            center.add(methods, BorderLayout.EAST);
            toReturn.add(center, BorderLayout.CENTER);
            toReturn.add(add, BorderLayout.EAST);
            add.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String name = reqName.getText();
                    if (Pattern.matches("\\w[\\s\\w]*", name)) {
                        Folder selected = (Folder) folders.getSelectedItem();
                        Method method = (Method) methods.getSelectedItem();
                        addReq(name, selected, method);
                        toReturn.setVisible(false);
                        reqName.setText("");
                    } else
                        JOptionPane.showMessageDialog(WorkSpace.this, "enter valid name!", "Alert", JOptionPane.WARNING_MESSAGE);
                }
            });
            return toReturn;
        }

        /**
         * ot use to add req to specific folder in requests tree's
         *
         * @param name   new req name
         * @param folder new req folder
         * @param method new req method type
         */
        private void addReq(String name, Folder folder, Method method) {
            folder.addReq(new Request(name, method, split));
        }

        /**
         * it use to add new folder to requests tree
         *
         * @param name new folder name
         */
        private void addFolder(String name) {
            Folder tmp = new Folder(name);
            rootModel.insertNodeInto(tmp, root, root.getChildCount());
            tree.scrollPathToVisible(new TreePath(tmp.getPath()));
            folders.addItem(tmp);
            requests.add(tmp);
        }

    }
}
