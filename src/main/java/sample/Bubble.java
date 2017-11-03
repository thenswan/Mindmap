package sample;

import javafx.beans.property.*;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@SuppressWarnings("Duplicates")
public class Bubble extends Pane {

    ArrayList<Bubble> children;

    ArrayList<Bubble> DirectParents;

    StringProperty Title;

    ObjectProperty<Paint> BubbleColor;

    ObjectProperty<Font> BubbleFont;

    StringProperty FontSize;

    BooleanProperty IsChild;

    IntegerProperty childCount;

    boolean isSelected;

    Rectangle Cover;

    Label text;

    ArrayList<Anchor> anchors;

    Anchor top;
    Anchor bottom;
    Anchor left;
    Anchor right;

    Controller aThis;
    BubbleArea bubbleArea;

    final int ANCHOR_RADUIS = 8;

    public Bubble(Controller aThis, BubbleArea bubbleArea, double x, double y) throws IOException {
        LoadData ld = new LoadData("/BubbleView.fxml", BubbleViewController.class);
        this.children = new ArrayList<>();
        this.DirectParents = new ArrayList<>();
        this.anchors = new ArrayList<>();

        this.aThis = aThis;
        this.bubbleArea = bubbleArea;
        this.getChildren().add(ld.node);
        BubbleViewController ctrl = (BubbleViewController) ld.Controller;

        this.Cover = ctrl.cover;
        this.text = ctrl.Title;

        this.top = new Anchor(Cover.getWidth() / 2, 0, ANCHOR_RADUIS);
        this.right = new Anchor(Cover.getWidth(), Cover.getHeight() / 2, ANCHOR_RADUIS);
        this.bottom = new Anchor(Cover.getWidth() / 2, Cover.getHeight(), ANCHOR_RADUIS);
        this.left = new Anchor(0, Cover.getHeight() / 2, ANCHOR_RADUIS);

        top.setParent(this);
        bottom.setParent(this);
        left.setParent(this);
        right.setParent(this);

        top.toFront();
        bottom.toFront();
        left.toFront();
        right.toFront();

//        top.setVisible(false);
//        bottom.setVisible(false);
//        left.setVisible(false);
//        right.setVisible(false);

        this.setLayoutX(x);
        this.setLayoutY(y);

//        this.bubbleXProperty().bind(this.layoutXProperty());
//        this.bubbleYProperty().bind(this.layoutYProperty());

        top.helpCenterX.bind(this.layoutXProperty().add(Cover.getWidth() / 2));
        top.helpCenterY.bind(this.layoutYProperty());
        right.helpCenterX.bind(this.layoutXProperty().add(Cover.getWidth()));
        right.helpCenterY.bind(this.layoutYProperty().add(Cover.getHeight() / 2));
        bottom.helpCenterX.bind(this.layoutXProperty().add(Cover.getWidth() / 2));
        bottom.helpCenterY.bind(this.layoutYProperty().add(Cover.getHeight()));
        left.helpCenterX.bind(this.layoutXProperty());
        left.helpCenterY.bind(this.layoutYProperty().add(Cover.getHeight() / 2));

//        top.layoutXProperty().bind(this.layoutXProperty().add(Cover.getWidth() / 2));
//        top.layoutYProperty().bind(this.layoutYProperty());
//        right.layoutXProperty().bind(this.layoutXProperty().add(Cover.getWidth()));
//        right.layoutYProperty().bind(this.layoutYProperty().add(Cover.getHeight() / 2));
//        bottom.layoutXProperty().bind(this.layoutXProperty().add(Cover.getWidth() / 2));
//        bottom.layoutYProperty().bind(this.layoutYProperty().add(Cover.getHeight()));
//        left.layoutXProperty().bind(this.layoutXProperty());
//        left.layoutYProperty().bind(this.layoutYProperty().add(Cover.getHeight() / 2));

        this.getChildren().add(top);
        this.getChildren().add(right);
        this.getChildren().add(bottom);
        this.getChildren().add(left);

        anchors.add(top);
        anchors.add(right);
        anchors.add(bottom);
        anchors.add(left);

        this.Title = new SimpleStringProperty();
        this.BubbleColor = new SimpleObjectProperty<>();
        this.BubbleFont = new SimpleObjectProperty<>();
//        this.Font = new SimpleStringProperty();
        this.FontSize = new SimpleStringProperty();
        this.IsChild = new SimpleBooleanProperty(false);
        this.childCount = new SimpleIntegerProperty();
//        System.out.println(this.pickOnBoundsProperty().getValue());
        addListeners(ctrl);
    }

    private void addListeners(final BubbleViewController ctrl) {
        this.Title.bindBidirectional(ctrl.Title.textProperty());
        this.BubbleColor.bindBidirectional(ctrl.cover.fillProperty()); // setValue(ctrl.cover.getFill().toString());
        this.BubbleFont.bindBidirectional(ctrl.Title.fontProperty());
//        this.Font.setValue(String.valueOf(ctrl.Title.getFont().getName()));
//        this.FontSize.setValue(String.valueOf(ctrl.Title.getFont().getSize()));
//        this.Color.bind(ctrl.cover.prop styleProperty());
        ObjectProperty<Point2D> mousePosition = new SimpleObjectProperty<>();

        // Bubble selection event
        ctrl.cover.setOnMouseClicked(t -> {

            // Just need it to be here
            double titleFontSize = ctrl.Title.getFont().getSize();

            if (t.getButton() == MouseButton.PRIMARY) {
                t.consume();
                // only one can be selected at time
                for (int i = 0; i < bubbleArea.bubbleArrayList.size(); i++) {
                    Bubble bubble = bubbleArea.bubbleArrayList.get(i);
                    // if ANOTHER bubble isSELECTED
                    if(!this.myEquals(bubble)){
                        if (bubble.isSelected)
                            return;
                    }
                }

                if (Bubble.this.isSelected) {
                    Bubble.this.isSelected = false;
                    aThis.selectedBubble = null;
                    aThis.colorPicker.setValue(Color.WHITE);
                    aThis.fontCombobox.getSelectionModel().selectFirst();
                    aThis.fontSizeCombobox.getSelectionModel().selectFirst();
                } else {
                    Bubble.this.isSelected = true;
                    aThis.selectedBubble = this;
                    aThis.colorPicker.setValue(Color.web(String.valueOf(Cover.getFill())));
                    aThis.fontCombobox.getSelectionModel().select(text.getFont().getName());
                    aThis.fontSizeCombobox.getSelectionModel().select(String.valueOf(titleFontSize));
                }
                ctrl.toggleSelected(Bubble.this.isSelected);


            }
        });

        ctrl.cover.setOnMouseEntered(event -> {
            Bubble.this.setCursor(Cursor.HAND);
        });

        ctrl.cover.setOnMouseExited(event -> {
            Bubble.this.setCursor(Cursor.DEFAULT);
        });
        // Dragging bubble events
        ctrl.cover.setOnMousePressed(e -> {
            mousePosition.set(new Point2D(e.getSceneX(), e.getSceneY()));
//            System.out.println("Mouse clicked point :" +  this.getLayoutX() + " " +this.getLayoutY());
//            System.out.println("ctrl.top.getCenterX() Y : " + ctrl.top.getCenterX() + " " + ctrl.top.getCenterY());
//            System.out.println("this.top.getCenterX() Y : " + this.top.getCenterX() + " " + this.top.getCenterY());
//            System.out.println("ctrl.top.getLayoutX() Y : " + ctrl.top.getLayoutX() + " " + ctrl.top.getLayoutY());
//            System.out.println("this.top.getLayoutX() Y : " + this.top.getLayoutX() + " " + this.top.getLayoutY());
        });
        ctrl.cover.setOnMouseDragged(t -> {
            Bubble.this.setCursor(Cursor.MOVE);

//            System.out.println("this.top.getCenterX() Y : " + this.top.getLayoutX() + " " + this.top.getLayoutY());

            double deltaX = t.getSceneX() - mousePosition.get().getX();
            double deltaY = t.getSceneY() - mousePosition.get().getY();

            Bubble.this.setLayoutX(Bubble.this.getLayoutX() + deltaX);
            Bubble.this.setLayoutY(Bubble.this.getLayoutY() + deltaY);
            mousePosition.set(new Point2D(t.getSceneX(), t.getSceneY()));
        });
        ctrl.cover.setOnMouseReleased(t -> {
            Bubble.this.setCursor(Cursor.DEFAULT);
        });

        // Change bubble title event
        ctrl.Title.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                if(mouseEvent.getClickCount() == 2){
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Введите текст");
                    dialog.setHeaderText("Введите текст элемента");
                    Optional<String> result = dialog.showAndWait();
                    if (!result.toString().equals("Optional.empty")) {
                        if (result.get().length() < 45) {
                            if (result.get().length() > 15) {
                                if (result.get().length() >= 30) {
                                    ctrl.Title.setText(result.get().substring(0, 15) + "\n" + result.get().substring(15, 30) + "\n" + result.get().substring(30));
                                } else {
                                    ctrl.Title.setText(result.get().substring(0, 15) + "\n" + result.get().substring(15));
                                }
                            } else if (result.get().length() < 15) {
                                ctrl.Title.setText(result.get());
                            }
                        }
                    }
                }
            }
        });

    }

    public void changeVisible() {
        bottom.setVisible(!bottom.isVisible());
        top.setVisible(!top.isVisible());
        left.setVisible(!left.isVisible());
        right.setVisible(!right.isVisible());
    }

    public ArrayList<Anchor> getAnchors() {
        return anchors;
    }

    public boolean myEquals(Object obj) {
        Bubble bubble = (Bubble) obj;
        return this.getLayoutX() == bubble.getLayoutX() && this.getLayoutX() == bubble.getLayoutX();
    }
}
