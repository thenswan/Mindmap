package sample;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import sample.model.BubbleAreaBean;
import sample.utils.jackson.BubbleAreaBeanDeserializer;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: Сохранение разработанной модели в файл и загрузка ее в редакторе
// TODO: save connections

// TODO: удаление bubble
// TODO: удаление связей


public class Controller {

    @FXML
    public ColorPicker colorPicker;

    @FXML
    public ComboBox<String> fontCombobox;

    @FXML
    public ComboBox<String> fontSizeCombobox;

    @FXML
    private TreeView<String> Outline;

    @FXML
    private AnchorPane bubbleAreaWrapper;

    @FXML
    private ToggleButton anchorToggleButton;

    @FXML
    private MenuItem openFile;

    @FXML
    private MenuItem saveAsImage;

    @FXML
    private MenuItem saveAsJson;

    private TreeItem TreeRoot;

    Bubble selectedBubble;

    private BubbleArea bubbleArea;

    private ContextMenu contextMenu;

    private double contextMenuX;

    private double contextMenuY;

    BubbleAreaBean bubbleAreaBean;

    public void initialize() {

        // init bubble area
        bubbleArea = new BubbleArea(this);
        bubbleArea.setPrefHeight(850);
        bubbleArea.setPrefWidth(1450);

        bubbleAreaBean = bubbleArea.bubbleAreaBean;

        bubbleAreaWrapper.getChildren().addAll(bubbleArea);

        // init tree view
        TreeRoot = new TreeItem("Root");
        Outline.setRoot(TreeRoot);
        Outline.setShowRoot(false);
        GenerateTree(bubbleArea.bubbleArrayList);

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
            captureAndSaveDisplay();
        });

        saveAsJson.setOnAction(e -> {
            saveFile();
//            ObjectMapper objectMapper = new ObjectMapper();
//            try {
//                new ObjectMapper().writeValue(new File("E:\\Mindmap.json"), bubbleAreaBean);
//                System.out.println(bubbleAreaBean.toString());
//                System.out.println(objectMapper.writeValueAsString(bubbleAreaBean));
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
        });

        colorPicker.setOnAction(e -> {
            if (selectedBubble != null){
                selectedBubble.Cover.setFill(colorPicker.getValue());
            }
        });
        fontCombobox.getSelectionModel().selectedItemProperty().addListener((o, oldVal, newVal) -> {
            if (selectedBubble != null){
                selectedBubble.text.setFont(Font.font(newVal));
            }
        });
        fontSizeCombobox.getSelectionModel().selectedItemProperty().addListener((o, oldVal, newVal) -> {
            if (selectedBubble != null){
                selectedBubble.text.setFont(Font.font(selectedBubble.text.getFont().getName(), Double.parseDouble(newVal)));
            }
        });
        anchorToggleButton.setOnAction(event -> {
            for (Node bubbleAreaNode : bubbleArea.getChildren()) {
                if (bubbleAreaNode instanceof Bubble) {
                    ((Bubble) bubbleAreaNode).changeVisible();
                }
            }
        });

        contextMenu = new ContextMenu();
        contextMenu.autoHideProperty().setValue(true);
        MenuItem menuItem = new MenuItem("Add Bubble");
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
                bubbleArea.AddBubble(this, contextMenuX, contextMenuY);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        bubbleArea.bubbleObservableList.addListener((ListChangeListener<Bubble>) change -> GenerateTree(bubbleArea.bubbleArrayList));

        // TODO
        bubbleAreaWrapper.setOnKeyPressed(e -> {
            System.out.println(e.toString());
            try {
                if (e.getCode() == KeyCode.DELETE) {
                    System.out.println("Removing selected bubble");
                    for (int i = 0; i < bubbleArea.bubbleArrayList.size(); i++) {
                        Bubble bubble = bubbleArea.bubbleArrayList.get(i);
                        if (bubble.isSelected) {
                            System.out.println("Removing selected bubble");
                            bubbleArea.RemoveBubble(bubble);
                        }

                    }

                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public void GenerateTree(final ArrayList<Bubble> Bubbles) {
        try {
//            System.out.println("Generating tree");
            OutlineGenerator.Build(Bubbles, Outline);
        } catch (InterruptedException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void captureAndSaveDisplay(){
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));

        //Prompt user to select a file
        File file = fileChooser.showSaveDialog(null);

        if(file != null){
            try {
                //Pad the capture area
                WritableImage writableImage = new WritableImage((int)bubbleArea.getWidth() + 20,
                        (int)bubbleArea.getHeight() + 20);
                bubbleArea.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                //Write the snapshot to the chosen file
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
//                System.out.println(bubbleAreaBean.toString());
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

//                System.out.println(bubbleAreaBean.toString());
//                System.out.println(objectMapper.writeValueAsString(bubbleAreaBean));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

}

