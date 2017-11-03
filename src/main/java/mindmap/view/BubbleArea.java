package mindmap.view;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import mindmap.controller.MainViewController;
import mindmap.model.BubbleAreaBean;
import mindmap.model.BubbleBean;
import mindmap.model.ConnectorBean;

import java.io.IOException;
import java.util.ArrayList;

public class BubbleArea extends Pane {

    public ArrayList<Bubble> bubbleArrayList;

    private ArrayList<Connector> connectorArrayList;

    private MainViewController mainViewController;

    public ObservableList<Bubble> bubbleObservableList;

    private Anchor startAnchor;

    public BubbleAreaBean bubbleAreaBean;

    private ArrayList<BubbleBean> bubbleBeanArrayList;

    private ArrayList<ConnectorBean> connectorBeanArrayList;

    public BubbleArea(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
        connectorArrayList = new ArrayList<>();
        bubbleArrayList = new ArrayList<>();
        this.bubbleObservableList = FXCollections.observableArrayList(bubbleArrayList);

        bubbleAreaBean = new BubbleAreaBean();
        bubbleBeanArrayList = new ArrayList<>();
        connectorBeanArrayList = new ArrayList<>();
        bubbleAreaBean.setBubbleArrayList(bubbleBeanArrayList);
        bubbleAreaBean.setConnectorBeanArrayList(connectorBeanArrayList);
    }

    private void addListeners(final Bubble bubble) {
        final MyCubicCurve cubicCurveDraggable = new MyCubicCurve();
        this.getChildren().add(cubicCurveDraggable);
        cubicCurveDraggable.setVisible(false);
        bubble.isPickOnBounds();

        cubicCurveDraggable.setStroke(Color.ORANGE);
        cubicCurveDraggable.setStrokeWidth(5);
        cubicCurveDraggable.setFill(null);

        bubble.childCount.addListener((ov, t, t1) -> mainViewController.GenerateTree(bubbleArrayList));

        bubble.IsChild.addListener((ov, t, t1) -> mainViewController.GenerateTree(bubbleArrayList));

        // get anchors
        for (Anchor anchorNode : bubble.getAnchors()) {

            SimpleDoubleProperty mousePositionX = new SimpleDoubleProperty();
            SimpleDoubleProperty mousePositionY = new SimpleDoubleProperty();

            SimpleDoubleProperty mousePositionX1 = new SimpleDoubleProperty();
            SimpleDoubleProperty mousePositionY1 = new SimpleDoubleProperty();

            anchorNode.setOnMousePressed(e -> {
//                System.out.println(e.toString());

                cubicCurveDraggable.setVisible(true);

                startAnchor = (Anchor) e.getSource();
                cubicCurveDraggable.setParent(startAnchor);
                cubicCurveDraggable.startXProperty().bind(startAnchor.helpCenterXProperty());
                cubicCurveDraggable.startYProperty().bind(startAnchor.helpCenterYProperty());
                cubicCurveDraggable.controlX1Property().bind(startAnchor.helpCenterXProperty());
                cubicCurveDraggable.controlY1Property().bind(startAnchor.helpCenterYProperty());

                mousePositionX.set(e.getSceneX());
                mousePositionY.set(e.getSceneY());
                mousePositionX1.set(startAnchor.getHelpCenterX());
                mousePositionY1.set(startAnchor.getHelpCenterY());

                cubicCurveDraggable.controlX2Property().bind(mousePositionX1);
                cubicCurveDraggable.controlY2Property().bind(mousePositionY1);
                cubicCurveDraggable.endXProperty().bind(mousePositionX1);
                cubicCurveDraggable.endYProperty().bind(mousePositionY1);
            });

            anchorNode.setOnMouseDragged(e -> {
                double deltaX = e.getSceneX() - mousePositionX.get();
                double deltaY = e.getSceneY() - mousePositionY.get();
                mousePositionX1.set(startAnchor.getHelpCenterX() + deltaX);
                mousePositionY1.set(startAnchor.getHelpCenterY() + deltaY);
                double valueX = 0;
                if (startAnchor.getHelpCenterX() + deltaX > startAnchor.getHelpCenterX()) {
                    valueX = startAnchor.getHelpCenterX() + deltaX - startAnchor.getHelpCenterX();
                } else {
                    valueX = startAnchor.getHelpCenterX() - startAnchor.getHelpCenterX() + deltaX;
                }
                cubicCurveDraggable.controlX1Property().bind(startAnchor.helpCenterXProperty().add(valueX));
                cubicCurveDraggable.controlY1Property().bind(startAnchor.helpCenterYProperty());
                cubicCurveDraggable.controlX2Property().bind(mousePositionX1);
                cubicCurveDraggable.controlY2Property().bind(mousePositionY1);
            });

            anchorNode.setOnMouseReleased(e -> {
                cubicCurveDraggable.setVisible(false);

                Node picked = e.getPickResult().getIntersectedNode();
//                System.out.println(picked.toString());
                if (picked instanceof Anchor) {
                    Anchor endAnchor = (Anchor) e.getPickResult().getIntersectedNode();
                    if (!startAnchor.equals(endAnchor) && !startAnchor.equalsByParent(endAnchor)) {
                        Bubble bubbleP = (Bubble) startAnchor.getMyParent();
                        Bubble bubbleC = (Bubble) endAnchor.getMyParent();
                        if (!bubbleC.children.contains(bubble) && !bubble.children.contains(bubbleC)) {

                            Connector connector = new Connector(bubbleP, bubbleC, startAnchor, endAnchor);
                            connectorArrayList.add(connector);
                            updateCanvas();

                            addConnectorToModel(connector.getConnectorBean());
                        }
                    }
                }
            });
        }
    }

    public void AddBubble(MainViewController mainViewController, double x, double y) throws IOException {
        Bubble bubble = new Bubble(mainViewController, this, x, y);
        addListeners(bubble);
        this.bubbleArrayList.add(bubble);
        this.bubbleObservableList.add(bubble);
        this.getChildren().add(bubble);
        AddBubbleToModel(bubble);
    }

//    public void AddConnector(Bubble bubbleP, Bubble bubbleC, Bubble endAnchor){
//        Connector connector = new Connector(bubbleP, bubbleC, startAnchor, endAnchor);
//        connectorArrayList.add(connector);
//        updateCanvas();
//    }

    public void AddConnector(ConnectorBean connectorBean){
        Connector connector = new Connector(connectorBean);
        connectorArrayList.add(connector);
        updateCanvas();
    }

    public void AddBubbleToModel(Bubble bubble){
        BubbleBean bubbleBean = new BubbleBean();
        // x, y coordinates
        bubbleBean.bubbleXProperty().bind(bubble.layoutXProperty());
        bubbleBean.bubbleYProperty().bind(bubble.layoutYProperty());
        // save text
        bubbleBean.bubbleTextProperty().bind(bubble.Title);
        // save color
        bubbleBean.bubbleColorProperty().bind(bubble.BubbleColor);
        // save font
        bubbleBean.bubbleFontProperty().bind(bubble.BubbleFont);
        // save connections
        bubbleBeanArrayList.add(bubbleBean);
    }

    private void addConnectorToModel(ConnectorBean connectorBean) {
//        System.out.println(connectorBean.toString());
        connectorBeanArrayList.add(connectorBean);
    }

    public void removeBubble(Bubble bubble) {
        this.bubbleArrayList.remove(bubble);
        this.bubbleObservableList.remove(bubble);
        this.getChildren().remove(bubble);
    }

    private void clearCanvas() {
        this.bubbleObservableList.clear();
        this.bubbleArrayList.clear();
        this.connectorArrayList.clear();
        this.getChildren().clear();
    }

    private void updateCanvas() {
        this.mainViewController.GenerateTree(bubbleArrayList);
        this.getChildren().add(connectorArrayList.get(connectorArrayList.size() - 1));
    }

    // TODO: add here back to model
    public void deserialize(BubbleAreaBean bubbleAreaBean) throws IOException {
        this.bubbleBeanArrayList = bubbleAreaBean.getBubbleArrayList();
        this.connectorBeanArrayList = bubbleAreaBean.getConnectorBeanArrayList();

        clearCanvas();

        // add bubbles
        for (BubbleBean bubbleBean: bubbleBeanArrayList){
            Bubble bubble = new Bubble(mainViewController, this, bubbleBean.getBubbleX(), bubbleBean.getBubbleY());
            bubble.Title.setValue(bubbleBean.getBubbleText());
            bubble.BubbleColor.setValue(bubbleBean.bubbleColorProperty().getValue());
            bubble.BubbleFont.setValue(bubbleBean.bubbleFontProperty().getValue());
            addListeners(bubble);
            this.bubbleArrayList.add(bubble);
            this.bubbleObservableList.add(bubble);
            this.getChildren().add(bubble);
        }

        // add connectors
        for (ConnectorBean connectorBean: connectorBeanArrayList){

            Anchor startAnchor = null;
            Anchor endAnchor = null;

            for (Bubble bubble: bubbleArrayList){
                for (Anchor anchorNode : bubble.getAnchors()) {
                    if (connectorBean.getStartX() == anchorNode.getHelpCenterX()
                            && connectorBean.getStartY() == anchorNode.getHelpCenterY()){
                        startAnchor = anchorNode;
                    }
                    if (connectorBean.getEndX() == anchorNode.getHelpCenterX()
                            && connectorBean.getEndY() == anchorNode.getHelpCenterY()){
                        endAnchor = anchorNode;
                    }
                }
            }

            if (startAnchor != null && endAnchor != null) {
                Bubble bubbleP = (Bubble) startAnchor.getMyParent();
                Bubble bubbleC = (Bubble) endAnchor.getMyParent();

                Connector connector = new Connector(bubbleP, bubbleC, startAnchor, endAnchor);
                connectorArrayList.add(connector);
                updateCanvas();

//            addConnectorToModel(connector.getConnectorBean());
            }

        }

    }


}
