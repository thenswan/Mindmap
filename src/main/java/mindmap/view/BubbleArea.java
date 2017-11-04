package mindmap.view;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import mindmap.controller.MainViewController;
import mindmap.model.BubbleAreaBean;
import mindmap.model.BubbleBean;
import mindmap.model.ConnectorBean;

import java.io.IOException;
import java.util.ArrayList;

public class BubbleArea extends Pane {

    private ArrayList<Bubble> bubbleArrayList;

    private ObservableList<Bubble> bubbleObservableList;

    private ArrayList<Connector> connectorArrayList;

    private BubbleAreaBean bubbleAreaBean;

    private ArrayList<BubbleBean> bubbleBeanArrayList;

    private ArrayList<ConnectorBean> connectorBeanArrayList;

    private MainViewController mainViewController;

    private Anchor startAnchor;

    public BubbleArea(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
        bubbleArrayList = new ArrayList<>();
        this.bubbleObservableList = FXCollections.observableArrayList(bubbleArrayList);
        connectorArrayList = new ArrayList<>();
        bubbleAreaBean = new BubbleAreaBean();
        bubbleBeanArrayList = new ArrayList<>();
        connectorBeanArrayList = new ArrayList<>();

        bubbleAreaBean.setBubbleBeanArrayList(bubbleBeanArrayList);
        bubbleAreaBean.setConnectorBeanArrayList(connectorBeanArrayList);
    }

    private void addListeners(final Bubble bubble) {
        final CubicCurve cubicCurve = new CubicCurve();
        this.getChildren().add(cubicCurve);
        cubicCurve.setVisible(false);
        bubble.isPickOnBounds();

        cubicCurve.setStroke(Color.ORANGE);
        cubicCurve.setStrokeWidth(5);
        cubicCurve.setFill(null);

        bubble.childCount.addListener((ov, t, t1) -> mainViewController.generateTreeView(bubbleArrayList));

        bubble.isChild.addListener((ov, t, t1) -> mainViewController.generateTreeView(bubbleArrayList));

        for (Anchor anchor : bubble.getAnchorArrayList()) {

            SimpleDoubleProperty mousePositionXScene = new SimpleDoubleProperty();
            SimpleDoubleProperty mousePositionYScene = new SimpleDoubleProperty();

            SimpleDoubleProperty mousePositionXRelative = new SimpleDoubleProperty();
            SimpleDoubleProperty mousePositionYRelative = new SimpleDoubleProperty();

            anchor.setOnMousePressed(e -> {
                cubicCurve.setVisible(true);

                startAnchor = (Anchor) e.getSource();
                cubicCurve.startXProperty().bind(startAnchor.helpCenterXProperty());
                cubicCurve.startYProperty().bind(startAnchor.helpCenterYProperty());
                cubicCurve.controlX1Property().bind(startAnchor.helpCenterXProperty());
                cubicCurve.controlY1Property().bind(startAnchor.helpCenterYProperty());

                mousePositionXScene.set(e.getSceneX());
                mousePositionYScene.set(e.getSceneY());
                mousePositionXRelative.set(startAnchor.getHelpCenterX());
                mousePositionYRelative.set(startAnchor.getHelpCenterY());

                cubicCurve.controlX2Property().bind(mousePositionXRelative);
                cubicCurve.controlY2Property().bind(mousePositionYRelative);
                cubicCurve.endXProperty().bind(mousePositionXRelative);
                cubicCurve.endYProperty().bind(mousePositionYRelative);
            });

            anchor.setOnMouseDragged(e -> {
                double deltaX = e.getSceneX() - mousePositionXScene.get();
                double deltaY = e.getSceneY() - mousePositionYScene.get();
                mousePositionXRelative.set(startAnchor.getHelpCenterX() + deltaX);
                mousePositionYRelative.set(startAnchor.getHelpCenterY() + deltaY);
                double valueX;
                if (startAnchor.getHelpCenterX() + deltaX > startAnchor.getHelpCenterX()) {
                    valueX = startAnchor.getHelpCenterX() + deltaX - startAnchor.getHelpCenterX();
                } else {
                    valueX = startAnchor.getHelpCenterX() - startAnchor.getHelpCenterX() + deltaX;
                }
                cubicCurve.controlX1Property().bind(startAnchor.helpCenterXProperty().add(valueX));
                cubicCurve.controlY1Property().bind(startAnchor.helpCenterYProperty());
                cubicCurve.controlX2Property().bind(mousePositionXRelative);
                cubicCurve.controlY2Property().bind(mousePositionYRelative);
            });

            anchor.setOnMouseReleased(e -> {
                cubicCurve.setVisible(false);
                Node pickedNode = e.getPickResult().getIntersectedNode();

                if (pickedNode instanceof Anchor) {
                    Anchor endAnchor = (Anchor) pickedNode;

                    if (!startAnchor.equals(endAnchor) && !startAnchor.equalsByParent(endAnchor)) {
                        Bubble bubbleParent = (Bubble) startAnchor.getMyParent();
                        Bubble bubbleChild = (Bubble) endAnchor.getMyParent();

                        if (!bubbleChild.childrenBubblesArrayList.contains(bubble) && !bubble.childrenBubblesArrayList.contains(bubbleChild)) {
                            Connector connector = new Connector(bubbleParent, bubbleChild, startAnchor, endAnchor);
                            connectorArrayList.add(connector);
                            updateCanvas();
                            addConnectorToModel(connector.getConnectorBean());
                        }
                    }
                }
            });

        }
    }

    public void addBubble(MainViewController mainViewController, double x, double y) throws IOException {
        Bubble bubble = new Bubble(mainViewController, this, x, y);
        this.addListeners(bubble);
        this.bubbleArrayList.add(bubble);
        this.bubbleObservableList.add(bubble);
        this.getChildren().add(bubble);
        addBubbleToModel(bubble);
    }

    private void addBubbleToModel(Bubble bubble){
        BubbleBean bubbleBean = new BubbleBean();
        // x, y coordinates
        bubbleBean.bubbleXProperty().bind(bubble.layoutXProperty());
        bubbleBean.bubbleYProperty().bind(bubble.layoutYProperty());
        // save titleLabel
        bubbleBean.bubbleTextProperty().bind(bubble.Title);
        // save color
        bubbleBean.bubbleColorProperty().bind(bubble.BubbleColor);
        // save font
        bubbleBean.bubbleFontProperty().bind(bubble.BubbleFont);
        // save connections
        bubbleBeanArrayList.add(bubbleBean);
    }

    private void addConnectorToModel(ConnectorBean connectorBean) {
        connectorBeanArrayList.add(connectorBean);
    }

    private void updateCanvas() {
        this.mainViewController.generateTreeView(bubbleArrayList);
        this.getChildren().add(connectorArrayList.get(connectorArrayList.size() - 1));
    }

    private void clearCanvas() {
        this.bubbleObservableList.clear();
        this.bubbleArrayList.clear();
        this.connectorArrayList.clear();
        this.getChildren().clear();
    }

    public void deserialize(BubbleAreaBean bubbleAreaBean) throws IOException {
        clearCanvas();

        this.bubbleBeanArrayList = bubbleAreaBean.getBubbleBeanArrayList();
        this.connectorBeanArrayList = bubbleAreaBean.getConnectorBeanArrayList();

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
                for (Anchor anchorNode : bubble.getAnchorArrayList()) {
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
                Bubble bubbleParent = (Bubble) startAnchor.getMyParent();
                Bubble bubbleChild = (Bubble) endAnchor.getMyParent();
                Connector connector = new Connector(bubbleParent, bubbleChild, startAnchor, endAnchor);
                connectorArrayList.add(connector);
                updateCanvas();
            }
        }

    }

    public ArrayList<Bubble> getBubbleArrayList() {
        return bubbleArrayList;
    }

    public ObservableList<Bubble> getBubbleObservableList() {
        return bubbleObservableList;
    }

    public BubbleAreaBean getBubbleAreaBean() {
        return bubbleAreaBean;
    }

}
