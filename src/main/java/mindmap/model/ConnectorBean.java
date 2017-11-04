package mindmap.model;

import javafx.beans.property.SimpleDoubleProperty;

public class ConnectorBean {

    private SimpleDoubleProperty startX = new SimpleDoubleProperty();

    private SimpleDoubleProperty startY = new SimpleDoubleProperty();

    private SimpleDoubleProperty controlX1 = new SimpleDoubleProperty();

    private SimpleDoubleProperty controlY1 = new SimpleDoubleProperty();

    private SimpleDoubleProperty controlX2 = new SimpleDoubleProperty();

    private SimpleDoubleProperty controlY2 = new SimpleDoubleProperty();

    private SimpleDoubleProperty endX = new SimpleDoubleProperty();

    private SimpleDoubleProperty endY = new SimpleDoubleProperty();

    public double getStartX() {
        return startX.get();
    }

    public SimpleDoubleProperty startXProperty() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX.set(startX);
    }

    public double getStartY() {
        return startY.get();
    }

    public SimpleDoubleProperty startYProperty() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY.set(startY);
    }

    public double getControlX1() {
        return controlX1.get();
    }

    public SimpleDoubleProperty controlX1Property() {
        return controlX1;
    }

    public void setControlX1(double controlX1) {
        this.controlX1.set(controlX1);
    }

    public double getControlY1() {
        return controlY1.get();
    }

    public SimpleDoubleProperty controlY1Property() {
        return controlY1;
    }

    public void setControlY1(double controlY1) {
        this.controlY1.set(controlY1);
    }

    public double getControlX2() {
        return controlX2.get();
    }

    public SimpleDoubleProperty controlX2Property() {
        return controlX2;
    }

    public void setControlX2(double controlX2) {
        this.controlX2.set(controlX2);
    }

    public double getControlY2() {
        return controlY2.get();
    }

    public SimpleDoubleProperty controlY2Property() {
        return controlY2;
    }

    public void setControlY2(double controlY2) {
        this.controlY2.set(controlY2);
    }

    public double getEndX() {
        return endX.get();
    }

    public SimpleDoubleProperty endXProperty() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX.set(endX);
    }

    public double getEndY() {
        return endY.get();
    }

    public SimpleDoubleProperty endYProperty() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY.set(endY);
    }

    @Override
    public String toString() {
        return "ConnectorBean {" + "\n" +
                "startX=" + getStartX() + "\n" +
                "startY=" + getStartY() + "\n" +
                "controlX1=" + getControlX1() + "\n" +
                "controlY1=" + getControlY1() + "\n" +
                "controlX2=" + getControlX2() + "\n" +
                "controlY2=" + getControlY2() + "\n" +
                "endX=" + getEndX() + "\n" +
                "endY=" + getEndY() + "\n" +
                '}';
    }

}
