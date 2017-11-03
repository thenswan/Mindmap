package mindmap.view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import mindmap.model.ConnectorBean;

public class Connector extends Group {

//    Bubble Parent;
//    Bubble Child;

    Anchor startAnchor;
    Anchor endAnchor;

//    Line line;

    MyCubicCurve cubicCurveDraggable;

    public Connector(Bubble p, Bubble c, Anchor startAnchor, Anchor endAnchor){
//        this.Child = c;
//        this.Parent = p;

        this.startAnchor = startAnchor;
        this.endAnchor = endAnchor;

//        line = new Line();
//        line.startXProperty().bind(c.layoutXProperty());
//        line.startYProperty().bind(c.layoutYProperty().add(c.Cover.heightProperty().divide(2)));
//        line.endXProperty().bind(p.layoutXProperty().add(p.Cover.widthProperty()));
//        line.endYProperty().bind(p.layoutYProperty().add(p.Cover.heightProperty().divide(2)));

        cubicCurveDraggable = new MyCubicCurve();

//        cubicCurveDraggable.startXProperty().bind(startAnchor.centerXProperty());
//        cubicCurveDraggable.startYProperty().bind(startAnchor.centerYProperty());
//
//        cubicCurveDraggable.controlX1Property().bind(startAnchor.centerXProperty());
//        cubicCurveDraggable.controlY1Property().bind(startAnchor.centerYProperty());
//        cubicCurveDraggable.controlX2Property().bind(endAnchor.centerXProperty());
//        cubicCurveDraggable.controlY2Property().bind(endAnchor.centerYProperty());
//        cubicCurveDraggable.endXProperty().bind(endAnchor.centerXProperty());
//        cubicCurveDraggable.endYProperty().bind(endAnchor.centerYProperty());


        cubicCurveDraggable.startXProperty().bind(startAnchor.helpCenterXProperty());
        cubicCurveDraggable.startYProperty().bind(startAnchor.helpCenterYProperty());

        cubicCurveDraggable.controlX1Property().bind(startAnchor.helpCenterXProperty());
        cubicCurveDraggable.controlY1Property().bind(startAnchor.helpCenterYProperty());
        cubicCurveDraggable.controlX2Property().bind(endAnchor.helpCenterXProperty());
        cubicCurveDraggable.controlY2Property().bind(endAnchor.helpCenterYProperty());
        cubicCurveDraggable.endXProperty().bind(endAnchor.helpCenterXProperty());
        cubicCurveDraggable.endYProperty().bind(endAnchor.helpCenterYProperty());

        cubicCurveDraggable.setStroke(Color.ORANGE);
        cubicCurveDraggable.setStrokeWidth(5);
        cubicCurveDraggable.setFill(null);

        p.children.add(c);
        c.IsChild.set(true);
        p.childCount.add(1);
//        c.kids.setIsFilled(true);
//        p.parent.setIsFilled(true);
        c.DirectParents.add(p);

        this.getChildren().add(cubicCurveDraggable);
    }

    public Connector(ConnectorBean connectorBean){
        cubicCurveDraggable = new MyCubicCurve();

        cubicCurveDraggable.startXProperty().bind(connectorBean.startXProperty());
        cubicCurveDraggable.startYProperty().bind(connectorBean.startYProperty());

        cubicCurveDraggable.controlX1Property().bind(connectorBean.controlX1Property());
        cubicCurveDraggable.controlY1Property().bind(connectorBean.controlY1Property());
        cubicCurveDraggable.controlX2Property().bind(connectorBean.controlX2Property());
        cubicCurveDraggable.controlY2Property().bind(connectorBean.controlY2Property());
        cubicCurveDraggable.endXProperty().bind(connectorBean.endXProperty());
        cubicCurveDraggable.endYProperty().bind(connectorBean.endYProperty());

        cubicCurveDraggable.setStroke(Color.ORANGE);
        cubicCurveDraggable.setStrokeWidth(5);
        cubicCurveDraggable.setFill(null);

        this.getChildren().add(cubicCurveDraggable);
    }

    public ConnectorBean getConnectorBean(){
        ConnectorBean connectorBean = new ConnectorBean();
        connectorBean.startXProperty().bind(startAnchor.helpCenterXProperty());
        connectorBean.startYProperty().bind(startAnchor.helpCenterYProperty());
        connectorBean.controlX1Property().bind(startAnchor.helpCenterXProperty());
        connectorBean.controlY1Property().bind(startAnchor.helpCenterYProperty());
        connectorBean.controlX2Property().bind(endAnchor.helpCenterXProperty());
        connectorBean.controlY2Property().bind(endAnchor.helpCenterYProperty());
        connectorBean.endXProperty().bind(endAnchor.helpCenterXProperty());
        connectorBean.endYProperty().bind(endAnchor.helpCenterYProperty());

        return connectorBean;
    }

}
