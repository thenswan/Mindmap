package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import sample.model.BubbleAreaBean;
import sample.model.BubbleBean;
import sample.model.ConnectorBean;

import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings({"unchecked", "Duplicates"})
public class BubbleArea extends Pane {

    public ArrayList<Bubble> bubbleArrayList;

    public ArrayList<Connector> Connectors;

    Controller controller;

    ObservableList<Bubble> bubbleObservableList;

    Anchor startAnchor;

    BubbleAreaBean bubbleAreaBean;

    public ArrayList<BubbleBean> bubbleBeanArrayList;

    public ArrayList<ConnectorBean> connectorBeanArrayList;

    BubbleArea(Controller controller) {
        this.controller = controller;
        Connectors = new ArrayList<>();
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

        bubble.childCount.addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                controller.GenerateTree(bubbleArrayList);
            }
        });

        bubble.IsChild.addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                controller.GenerateTree(bubbleArrayList);
            }
        });

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
                            Connectors.add(connector);
                            updateCanvas();

                            addConnectorToModel(connector.getConnectorBean());
                        }
                    }
                }
            });



        }
    }

    public void AddBubble(Controller controller, double x, double y) throws IOException {
        Bubble bubble = new Bubble(controller, this, x, y);
        addListeners(bubble);
        this.bubbleArrayList.add(bubble);
        this.bubbleObservableList.add(bubble);
        this.getChildren().add(bubble);
        AddBubbleToModel(bubble);
    }

//    public void AddConnector(Bubble bubbleP, Bubble bubbleC, Bubble endAnchor){
//        Connector connector = new Connector(bubbleP, bubbleC, startAnchor, endAnchor);
//        Connectors.add(connector);
//        updateCanvas();
//    }

    public void AddConnector(ConnectorBean connectorBean){
        Connector connector = new Connector(connectorBean);
        Connectors.add(connector);
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

    public void addConnectorToModel(ConnectorBean connectorBean) {
//        System.out.println(connectorBean.toString());
        connectorBeanArrayList.add(connectorBean);
    }

    public void RemoveBubble(Bubble b) throws IOException {
        this.bubbleArrayList.remove(b);
        this.bubbleObservableList.remove(b);
        this.getChildren().remove(b);
    }

    public void updateCanvas() {
        controller.GenerateTree(bubbleArrayList);
        this.getChildren().add(Connectors.get(Connectors.size() - 1));
//        System.out.println("connectors size: " + Connectors.size());
    }

    // TODO: add here back to model
    public void deserialize(BubbleAreaBean bubbleAreaBean) throws IOException {
        this.bubbleBeanArrayList = bubbleAreaBean.getBubbleArrayList();
        this.connectorBeanArrayList = bubbleAreaBean.getConnectorBeanArrayList();

        for (BubbleBean bubbleBean: bubbleBeanArrayList){
            Bubble bubble = new Bubble(controller, this, bubbleBean.getBubbleX(), bubbleBean.getBubbleY());
            bubble.Title.setValue(bubbleBean.getBubbleText());
            bubble.BubbleColor.setValue(bubbleBean.bubbleColorProperty().getValue());
            bubble.BubbleFont.setValue(bubbleBean.bubbleFontProperty().getValue());
            addListeners(bubble);
            this.bubbleArrayList.add(bubble);
            this.bubbleObservableList.add(bubble);
            this.getChildren().add(bubble);
        }

        for (ConnectorBean connectorBean: connectorBeanArrayList){

            Anchor startAnchor = null;
            Anchor endAnchor = null;

            for (Bubble bubble: bubbleArrayList){
                for (Anchor anchorNode : bubble.getAnchors()) {

//                    System.out.println("connectorBean.getStartX() :" + connectorBean.getStartX()
//                            + " anchorNode.getHelpCenterX(): " + anchorNode.getHelpCenterX()
//                            + " connectorBean.getStartY():  " + connectorBean.getStartY()
//                            + " anchorNode.getHelpCenterY(): " + anchorNode.getHelpCenterY());
//
//                    System.out.println("connectorBean.getEndX() :" + connectorBean.getEndX()
//                            + " anchorNode.getHelpCenterX(): " + anchorNode.getHelpCenterX()
//                            + " connectorBean.getEndY():  " + connectorBean.getEndY()
//                            + " anchorNode.getHelpCenterY(): " + anchorNode.getHelpCenterY());

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
                Connectors.add(connector);
                updateCanvas();

//            addConnectorToModel(connector.getConnectorBean());
            }

        }

    }


}
