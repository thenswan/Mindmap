package mindmap.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class BubbleViewController implements Initializable {

    @FXML
    public Rectangle coverRectangle;

    @FXML
    private
    TextField textField;

    @FXML
    public Label titleLabel;

    @FXML
    Circle top;

    @FXML
    Circle right;

    @FXML
    Circle bottom;

    @FXML
    Circle left;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        titleLabel.toFront();
    }

    @FXML
    private void Typed(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            this.titleLabel.setVisible(true);
            this.textField.setVisible(false);

            textField.setFocusTraversable(false);
            textField.setEditable(false);
            this.coverRectangle.setVisible(true);
        }
    }

    public void toggleSelected(boolean isSelected) {
        if (isSelected) {
//            this.coverRectangle.setEffect(new DropShadow(10, 0, 0, Color.GREEN));
            this.coverRectangle.setStroke(Color.GREEN);
        } else {
            this.coverRectangle.setEffect(null);
            this.coverRectangle.setStroke(Color.BLACK);
        }

    }
}