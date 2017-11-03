package mindmap.view;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Anchor extends Circle {

    private Node parent;

    public SimpleDoubleProperty helpCenterX = new SimpleDoubleProperty();

    public SimpleDoubleProperty helpCenterY = new SimpleDoubleProperty();

    public Anchor() {
    }

    public Anchor(double centerX, double centerY, double radius) {
        super(radius);
        super.setFill(Color.RED);
//        this.setCenterX(centerX);
//        this.setCenterY(centerY);
        this.setLayoutY(centerY);
        this.setLayoutX(centerX);
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getMyParent(){
        return this.parent;
    }

    public double getHelpCenterX() {
        return helpCenterX.get();
    }

    public SimpleDoubleProperty helpCenterXProperty() {
        return helpCenterX;
    }

    public double getHelpCenterY() {
        return helpCenterY.get();
    }

    public SimpleDoubleProperty helpCenterYProperty() {
        return helpCenterY;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Circle){
            Circle circle = (Circle) obj;
            return this.getLayoutX() == circle.getLayoutX() && this.getLayoutY() == circle.getLayoutY();
        }
        return false;
    }

    public boolean equalsByParent(Object obj){
        if (obj instanceof Anchor){
            Anchor anchor = (Anchor) obj;
            return this.parent == anchor.parent;
        }

        return false;
    }

}
