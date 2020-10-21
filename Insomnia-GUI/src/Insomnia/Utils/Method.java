package Insomnia.Utils;


import Insomnia.GUI.Request;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public enum Method {
    GET("icons\\Get.png"), DELETE("icons\\DELETE.png"), POST("icons\\post.png"), PUT("icons\\put.png"), PATCH("icons\\patch.png");

    private Icon icon;

    private Method(String path) {
        this.icon = new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(30, 15, Image.SCALE_DEFAULT));
    }

    private Icon getIcon() {

        return icon;
    }

    public static Method methodRecognizer(String method) {
        for (Method curr : Method.values())
            if (curr.toString().equals(method))
                return curr;
        return null;
    }

    public static class customTree extends DefaultTreeCellRenderer {
        Icon insomnia;
        Icon folder;

        public customTree() {
            insomnia = new ImageIcon(new ImageIcon("icons\\icon.png").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
            folder = new ImageIcon(new ImageIcon("icons\\folder.png").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
        }

        /**
         * Configures the renderer based on the passed in components.
         * The value is set from messaging the tree with
         * <code>convertValueToText</code>, which ultimately invokes
         * <code>toString</code> on <code>value</code>.
         * The foreground color is set based on the selection and the icon
         * is set based on the <code>leaf</code> and <code>expanded</code>
         * parameters.
         *
         * @param tree
         * @param value
         * @param sel
         * @param expanded
         * @param leaf
         * @param row
         * @param hasFocus
         */
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            Component toReturn = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            Icon toSet = null;
            if (value instanceof Request) {
                toSet = ((Request) value).getType().getIcon();
            } else if (row == 0) {
                toSet = insomnia;
            } else {
                toSet = folder;
            }
            setIcon(toSet);
            return toReturn;
        }
    }
}
