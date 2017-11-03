package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

class LoadData<T>{
    public T Controller;
    public Node node;
    public LoadData (String loc, Class<T> type) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        this.node = (Node) loader.load(FxmlLoader.class.getResourceAsStream(loc));
        this.Controller = type.cast(loader.getController());
    }
}