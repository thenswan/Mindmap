package sample.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class BubbleBean {

    SimpleDoubleProperty bubbleX = new SimpleDoubleProperty();

    SimpleDoubleProperty bubbleY = new SimpleDoubleProperty();

    SimpleStringProperty bubbleText = new SimpleStringProperty();

    ObjectProperty<Paint> bubbleColor = new SimpleObjectProperty<>();

    ObjectProperty<Font> bubbleFont = new SimpleObjectProperty<>();

    public double getBubbleX() {
        return bubbleX.get();
    }

    public SimpleDoubleProperty bubbleXProperty() {
        return bubbleX;
    }

    public void setBubbleX(double bubbleX) {
        this.bubbleX.set(bubbleX);
    }

    public double getBubbleY() {
        return bubbleY.get();
    }

    public SimpleDoubleProperty bubbleYProperty() {
        return bubbleY;
    }

    public void setBubbleY(double bubbleY) {
        this.bubbleY.set(bubbleY);
    }

    public String getBubbleText() {
        return bubbleText.get();
    }

    public SimpleStringProperty bubbleTextProperty() {
        return bubbleText;
    }

    public void setBubbleText(String bubbleText) {
        this.bubbleText.set(bubbleText);
    }

    public Paint getBubbleColor() {
        return bubbleColor.get();
    }

    public ObjectProperty<Paint> bubbleColorProperty() {
        return bubbleColor;
    }

    public void setBubbleColor(Paint bubbleColor) {
        this.bubbleColor.set(bubbleColor);
    }

    public Font getBubbleFont() {
        return bubbleFont.get();
    }

    public ObjectProperty<Font> bubbleFontProperty() {
        return bubbleFont;
    }

    public void setBubbleFont(Font bubbleFont) {
        this.bubbleFont.set(bubbleFont);
    }

    @Override
    public String toString() {
        return "BubbleBean {" + "\n" +
                "bubbleX: " + getBubbleX() + "\n" +
                "bubbleY: " + getBubbleY() + "\n" +
                "bubbleText: " + getBubbleText() + "\n" +
                "bubbleColor: " + getBubbleColor() + "\n" +
                "bubbleFont: " + getBubbleFont() + "\n" +
                "}";
    }
}
