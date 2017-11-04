package mindmap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import mindmap.model.BubbleAreaBean;
import mindmap.utils.OutlineGenerator;
import mindmap.utils.jackson.BubbleAreaBeanDeserializer;
import mindmap.view.Bubble;
import mindmap.view.BubbleArea;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainViewController {

    /**
     * Fields
     */

    @FXML
    private MenuItem openFile;

    @FXML
    private MenuItem saveAsImage;

    @FXML
    private MenuItem saveFile;

    @FXML
    private ToggleButton anchorToggleButton;

    @FXML
    public ColorPicker colorPicker;

    @FXML
    public ComboBox<String> fontCombobox;

    @FXML
    public ComboBox<String> fontSizeCombobox;

    @FXML
    private TreeView<String> treeView;

    @FXML
    private AnchorPane bubbleAreaWrapper;

    /**
     * Utility variables
     */

    private TreeItem treeRoot;

    public Bubble selectedBubble;

    private BubbleArea bubbleArea;

    private ContextMenu contextMenu;

    private double contextMenuX;

    private double contextMenuY;

    private BubbleAreaBean bubbleAreaBean;

    public void initialize() {

        // init bubble area
        bubbleArea = new BubbleArea(this);
        bubbleArea.setPrefHeight(850);
        bubbleArea.setPrefWidth(1450);
        bubbleAreaWrapper.getChildren().addAll(bubbleArea);

        // init model
        bubbleAreaBean = bubbleArea.getBubbleAreaBean();

        // init tree view
        treeRoot = new TreeItem("Root");
        treeView.setRoot(treeRoot);
        treeView.setShowRoot(false);
        generateTreeView(bubbleArea.getBubbleArrayList());

        // init font combobox
        List<String> fontFamilies = Font.getFamilies();
        ObservableList fontFamiliesObservableList = FXCollections.observableArrayList(fontFamilies);
        fontCombobox.setItems(fontFamiliesObservableList);
        fontCombobox.getSelectionModel().select(0);

        // init font size combobox
        for (int i = 8; i < 51; i++){
            fontSizeCombobox.getItems().add(String.valueOf(i));
        }

        anchorToggleButton.setGraphic(new Circle(8, Color.RED));

        addListeners();
    }

    private void addListeners(){

        openFile.setOnAction(e -> {
            openFile();
        });

        saveAsImage.setOnAction(e -> {
            saveAsImage();
        });

        saveFile.setOnAction(e -> {
            saveFile();
        });

        anchorToggleButton.setOnAction(e -> {
            ToggleButton toggleButton = (ToggleButton) e.getSource();
            boolean isSelected = toggleButton.isSelected();
            for (Node bubbleAreaNode : bubbleArea.getChildren()) {
                if (bubbleAreaNode instanceof Bubble) {
                    ((Bubble) bubbleAreaNode).setAnchorsVisible(isSelected);
                }
            }
        });
        colorPicker.setOnAction(e -> {
            if (selectedBubble != null){
                selectedBubble.coverRectangle.setFill(colorPicker.getValue());
            }
        });
        fontCombobox.getSelectionModel().selectedItemProperty().addListener((o, oldVal, newVal) -> {
            if (selectedBubble != null){
                selectedBubble.titleLabel.setFont(Font.font(newVal));
            }
        });
        fontSizeCombobox.getSelectionModel().selectedItemProperty().addListener((o, oldVal, newVal) -> {
            if (selectedBubble != null){
                selectedBubble.titleLabel.setFont(Font.font(selectedBubble.titleLabel.getFont().getName(), Double.parseDouble(newVal)));
            }
        });

        contextMenu = new ContextMenu();
        MenuItem menuItem = new MenuItem("Add bubble");
        contextMenu.getItems().add(menuItem);
        bubbleArea.setPickOnBounds(true);

        bubbleArea.setOnMouseClicked(t -> {
            if (t.getButton() == MouseButton.PRIMARY) {
                if (contextMenu.isShowing()){
                    contextMenu.hide();
                }
            }
            if (t.getButton() == MouseButton.SECONDARY) {
                contextMenuX = t.getX();
                contextMenuY = t.getY();
                contextMenu.show(bubbleArea, t.getScreenX(), t.getScreenY());
            }
        });

        menuItem.setOnAction(t -> {
            try {
                bubbleArea.addBubble(this, contextMenuX, contextMenuY);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        bubbleArea.getBubbleObservableList().addListener((ListChangeListener<Bubble>) change -> generateTreeView(bubbleArea.getBubbleArrayList()));

    }

    public void generateTreeView(final ArrayList<Bubble> Bubbles) {
        try {
            OutlineGenerator.Build(Bubbles, treeView);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void saveAsImage(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));
        File file = fileChooser.showSaveDialog(null);
        if(file != null){
            try {
                WritableImage writableImage = new WritableImage((int)bubbleArea.getWidth() + 20,
                        (int)bubbleArea.getHeight() + 20);
                bubbleArea.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) { ex.printStackTrace(); }
        }
    }

    private void openFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("json files (*.json)", "*.json"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null){
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                SimpleModule module = new SimpleModule();
                module.addDeserializer(BubbleAreaBean.class, new BubbleAreaBeanDeserializer());
                objectMapper.registerModule(module);
                bubbleAreaBean = objectMapper.readValue(file, BubbleAreaBean.class);
                bubbleArea.deserialize(bubbleAreaBean);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("json files (*.json)", "*.json"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null){
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                objectMapper.writeValue(file, bubbleAreaBean);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

}

