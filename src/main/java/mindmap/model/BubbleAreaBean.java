package mindmap.model;

import java.util.ArrayList;

public class BubbleAreaBean {

    private ArrayList<BubbleBean> bubbleArrayList;

    private ArrayList<ConnectorBean> connectorBeanArrayList;

    public ArrayList<BubbleBean> getBubbleArrayList() {
        return bubbleArrayList;
    }

    public void setBubbleArrayList(ArrayList<BubbleBean> bubbleArrayList) {
        this.bubbleArrayList = bubbleArrayList;
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
                "bubbleArrayList=" + bubbleArrayList +
                "\n connectorBeanArrayList=" + connectorBeanArrayList +
                "\n}";
    }

}
