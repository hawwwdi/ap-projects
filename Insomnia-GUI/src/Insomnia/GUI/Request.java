package Insomnia.GUI;

import Insomnia.Model.RequestModel;
import Insomnia.Utils.Method;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;


public class Request extends DefaultMutableTreeNode {
    private RequestModel model;
    private JPanel settingPanel;
    private JPanel responsePanel;
    private JSplitPane split;
    private String name;

    public Request(String name, Method type, JSplitPane split) {
        super(name);
        model = new RequestModel(Application.FOLLOW_REDIRECT);
        model.setShowResponseHeaders(true);
        this.split = split;
        this.name = name;
        this.model.setMethod(type);
        responsePanel = new EmptyResponsePanel();
        settingPanel = new RequestSettingPanel(model, responsePanel);
    }

    public Request(String name, RequestModel model, JSplitPane split) {
        super(name);
        this.name = name;
        this.model = model;
        this.split = split;
        responsePanel = new EmptyResponsePanel();
        settingPanel = new RequestSettingPanel(model, responsePanel);
    }

    public void select() {
        split.setRightComponent(responsePanel);
        split.setLeftComponent(settingPanel);
    }

    public void setType(Method type) {
        this.model.setMethod(type);
    }

    public Method getType() {
        return this.model.getMethod();
    }

    public RequestModel getModel() {
        return model;
    }

    @Override
    public String toString() {
        return name;
    }
}
