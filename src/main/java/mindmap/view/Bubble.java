package mindmap.view;

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
import mindmap.controller.BubbleViewController;
import mindmap.controller.MainViewController;
import mindmap.utils.LoadData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@SuppressWarnings("Duplicates")
public class Bubble extends Pane {

    ArrayList<Bubble> childrenBubblesArrayList;

    ArrayList<Bubble> parentBubblesArrayList;

    StringProperty Title;

    ObjectProperty<Paint> BubbleColor;

    ObjectProperty<Font> BubbleFont;

    public BooleanProperty isChild;

    IntegerProperty childCount;

    private boolean isSelected;

    public Rectangle coverRectangle;

    public Label titleLabel;

    private ArrayList<Anchor> anchorArrayList;

    private Anchor topAnchor;

    private Anchor bottomAnchor;

    private Anchor leftAnchor;

    private Anchor rightAnchor;

    private MainViewController mainViewController;

    private BubbleArea bubbleArea;

    private final int ANCHOR_RADUIS = 8;

    public Bubble(MainViewController mainViewController, BubbleArea bubbleArea, double x, double y) throws IOException {
        LoadData loadData = new LoadData("/fxml/BubbleView.fxml", BubbleViewController.class);
        this.childrenBubblesArrayList = new ArrayList<>();
        this.parentBubblesArrayList = new ArrayList<>();
        this.anchorArrayList = new ArrayList<>();
        this.mainViewController = mainViewController;
        this.bubbleArea = bubbleArea;
        this.getChildren().add(loadData.node);

        BubbleViewController bubbleViewController = (BubbleViewController) loadData.controller;

        this.coverRectangle = bubbleViewController.coverRectangle;
        this.titleLabel = bubbleViewController.titleLabel;

        this.topAnchor = new Anchor(coverRectangle.getWidth() / 2, 0, ANCHOR_RADUIS);
        this.rightAnchor = new Anchor(coverRectangle.getWidth(), coverRectangle.getHeight() / 2, ANCHOR_RADUIS);
        this.bottomAnchor = new Anchor(coverRectangle.getWidth() / 2, coverRectangle.getHeight(), ANCHOR_RADUIS);
        this.leftAnchor = new Anchor(0, coverRectangle.getHeight() / 2, ANCHOR_RADUIS);

        topAnchor.setParent(this);
        bottomAnchor.setParent(this);
        leftAnchor.setParent(this);
        rightAnchor.setParent(this);

        topAnchor.toFront();
        bottomAnchor.toFront();
        leftAnchor.toFront();
        rightAnchor.toFront();

        topAnchor.setVisible(false);
        bottomAnchor.setVisible(false);
        leftAnchor.setVisible(false);
        rightAnchor.setVisible(false);

        this.setLayoutX(x);
        this.setLayoutY(y);

        topAnchor.helpCenterX.bind(this.layoutXProperty().add(coverRectangle.getWidth() / 2));
        topAnchor.helpCenterY.bind(this.layoutYProperty());
        rightAnchor.helpCenterX.bind(this.layoutXProperty().add(coverRectangle.getWidth()));
        rightAnchor.helpCenterY.bind(this.layoutYProperty().add(coverRectangle.getHeight() / 2));
        bottomAnchor.helpCenterX.bind(this.layoutXProperty().add(coverRectangle.getWidth() / 2));
        bottomAnchor.helpCenterY.bind(this.layoutYProperty().add(coverRectangle.getHeight()));
        leftAnchor.helpCenterX.bind(this.layoutXProperty());
        leftAnchor.helpCenterY.bind(this.layoutYProperty().add(coverRectangle.getHeight() / 2));

        this.getChildren().add(topAnchor);
        this.getChildren().add(rightAnchor);
        this.getChildren().add(bottomAnchor);
        this.getChildren().add(leftAnchor);

        anchorArrayList.add(topAnchor);
        anchorArrayList.add(rightAnchor);
        anchorArrayList.add(bottomAnchor);
        anchorArrayList.add(leftAnchor);

        this.Title = new SimpleStringProperty();
        this.BubbleColor = new SimpleObjectProperty<>();
        this.BubbleFont = new SimpleObjectProperty<>();
        this.isChild = new SimpleBooleanProperty();
        this.childCount = new SimpleIntegerProperty();

        addListeners(bubbleViewController);
    }

    private void addListeners(final BubbleViewController bubbleViewController) {
        this.Title.bindBidirectional(bubbleViewController.titleLabel.textProperty());
        this.BubbleColor.bindBidirectional(bubbleViewController.coverRectangle.fillProperty());
        this.BubbleFont.bindBidirectional(bubbleViewController.titleLabel.fontProperty());

        ObjectProperty<Point2D> mousePosition = new SimpleObjectProperty<>();

        // Bubble selection event
        bubbleViewController.coverRectangle.setOnMouseClicked(t -> {

            // Just need it to be here
            double titleFontSize = bubbleViewController.titleLabel.getFont().getSize();

            if (t.getButton() == MouseButton.PRIMARY) {
                t.consume();
                // only one can be selected at time
                for (int i = 0; i < bubbleArea.getBubbleArrayList().size(); i++) {
                    Bubble bubble = bubbleArea.getBubbleArrayList().get(i);
                    // if ANOTHER bubble isSELECTED
                    if(!this.myEquals(bubble)){
                        if (bubble.isSelected)
                            return;
                    }
                }

                if (Bubble.this.isSelected) {
                    Bubble.this.isSelected = false;
                    mainViewController.selectedBubble = null;
                    mainViewController.colorPicker.setValue(Color.WHITE);
                    mainViewController.fontCombobox.getSelectionModel().selectFirst();
                    mainViewController.fontSizeCombobox.getSelectionModel().selectFirst();
                } else {
                    Bubble.this.isSelected = true;
                    mainViewController.selectedBubble = this;
                    mainViewController.colorPicker.setValue(Color.web(String.valueOf(coverRectangle.getFill())));
                    mainViewController.fontCombobox.getSelectionModel().select(titleLabel.getFont().getName());
                    mainViewController.fontSizeCombobox.getSelectionModel().select(String.valueOf(titleFontSize));
                }
                bubbleViewController.toggleSelected(Bubble.this.isSelected);


            }
        });

        bubbleViewController.coverRectangle.setOnMouseEntered(event -> {
            Bubble.this.setCursor(Cursor.HAND);
        });

        bubbleViewController.coverRectangle.setOnMouseExited(event -> {
            Bubble.this.setCursor(Cursor.DEFAULT);
        });
        // Dragging bubble events
        bubbleViewController.coverRectangle.setOnMousePressed(e -> {
            mousePosition.set(new Point2D(e.getSceneX(), e.getSceneY()));
        });
        bubbleViewController.coverRectangle.setOnMouseDragged(t -> {
            Bubble.this.setCursor(Cursor.MOVE);

            double deltaX = t.getSceneX() - mousePosition.get().getX();
            double deltaY = t.getSceneY() - mousePosition.get().getY();

            Bubble.this.setLayoutX(Bubble.this.getLayoutX() + deltaX);
            Bubble.this.setLayoutY(Bubble.this.getLayoutY() + deltaY);
            mousePosition.set(new Point2D(t.getSceneX(), t.getSceneY()));
        });
        bubbleViewController.coverRectangle.setOnMouseReleased(t -> {
            Bubble.this.setCursor(Cursor.DEFAULT);
        });

        // Change bubble title event
        bubbleViewController.titleLabel.setOnMouseClicked(mouseEvent -> {
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
                                    bubbleViewController.titleLabel.setText(result.get().substring(0, 15) + "\n" + result.get().substring(15, 30) + "\n" + result.get().substring(30));
                                } else {
                                    bubbleViewController.titleLabel.setText(result.get().substring(0, 15) + "\n" + result.get().substring(15));
                                }
                            } else if (result.get().length() < 15) {
                                bubbleViewController.titleLabel.setText(result.get());
                            }
                        }
                    }
                }
            }
        });

    }

    public void setAnchorsVisible(boolean isVisible){
        bottomAnchor.setVisible(isVisible);
        topAnchor.setVisible(isVisible);
        leftAnchor.setVisible(isVisible);
        rightAnchor.setVisible(isVisible);
    }

    public ArrayList<Anchor> getAnchorArrayList() {
        return anchorArrayList;
    }

    public boolean myEquals(Object obj) {
        Bubble bubble = (Bubble) obj;
        return this.getLayoutX() == bubble.getLayoutX() && this.getLayoutX() == bubble.getLayoutX();
    }
}
