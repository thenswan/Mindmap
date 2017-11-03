package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class BubbleViewController implements Initializable {

    @FXML
    Rectangle cover;

    @FXML
    TextField textField;

    @FXML
    Label Title;

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
        // TODO
//        this.Title.setFont(Font.font("System Regular", 16));
        Title.toFront();
    }

    @FXML
    private void Typed(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            this.Title.setVisible(true);
            this.textField.setVisible(false);

            textField.setFocusTraversable(false);
            textField.setEditable(false);
            this.cover.setVisible(true);
        }
    }

    void toggleSelected(boolean isSelected) {
        if (isSelected) {
//            this.cover.setEffect(new DropShadow(10, 0, 0, Color.GREEN));
            this.cover.setStroke(Color.GREEN);
        } else {
            this.cover.setEffect(null);
            this.cover.setStroke(Color.BLACK);
        }

    }
}