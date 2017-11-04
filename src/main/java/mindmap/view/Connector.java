package mindmap.view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import mindmap.model.ConnectorBean;

public class Connector extends Group {

    private Anchor startAnchor;

    private Anchor endAnchor;

    private CubicCurve cubicCurve;

    public Connector(Bubble p, Bubble c, Anchor startAnchor, Anchor endAnchor){
        this.startAnchor = startAnchor;
        this.endAnchor = endAnchor;

        cubicCurve = new CubicCurve();
        cubicCurve.startXProperty().bind(startAnchor.helpCenterXProperty());
        cubicCurve.startYProperty().bind(startAnchor.helpCenterYProperty());
        cubicCurve.controlX1Property().bind(startAnchor.helpCenterXProperty());
        cubicCurve.controlY1Property().bind(startAnchor.helpCenterYProperty());
        cubicCurve.controlX2Property().bind(endAnchor.helpCenterXProperty());
        cubicCurve.controlY2Property().bind(endAnchor.helpCenterYProperty());
        cubicCurve.endXProperty().bind(endAnchor.helpCenterXProperty());
        cubicCurve.endYProperty().bind(endAnchor.helpCenterYProperty());

        cubicCurve.setStroke(Color.ORANGE);
        cubicCurve.setStrokeWidth(5);
        cubicCurve.setFill(null);

        p.childrenBubblesArrayList.add(c);
        c.isChild.set(true);
        p.childCount.add(1);
        c.parentBubblesArrayList.add(p);

        this.getChildren().add(cubicCurve);
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
