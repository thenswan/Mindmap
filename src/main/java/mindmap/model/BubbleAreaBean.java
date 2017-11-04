package mindmap.model;

import java.util.ArrayList;

public class BubbleAreaBean {

    private ArrayList<BubbleBean> bubbleBeanArrayList;

    private ArrayList<ConnectorBean> connectorBeanArrayList;

    public ArrayList<BubbleBean> getBubbleBeanArrayList() {
        return bubbleBeanArrayList;
    }

    public void setBubbleBeanArrayList(ArrayList<BubbleBean> bubbleBeanArrayList) {
        this.bubbleBeanArrayList = bubbleBeanArrayList;
    }

    public ArrayList<ConnectorBean> getConnectorBeanArrayList() {
        return connectorBeanArrayList;
    }

    public void setConnectorBeanArrayList(ArrayList<ConnectorBean> connectorBeanArrayList) {
        this.connectorBeanArrayList = connectorBeanArrayList;
    }

    @Override
    public String toString() {
        return "BubbleAreaBean{" + "\n" +
                "bubbleBeanArrayList=" + bubbleBeanArrayList +
                "\n connectorBeanArrayList=" + connectorBeanArrayList +
                "\n}";
    }

}
