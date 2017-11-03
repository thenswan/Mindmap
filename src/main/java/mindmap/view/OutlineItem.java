package mindmap.view;

import javafx.concurrent.Task;
import javafx.scene.control.TreeItem;

public class OutlineItem extends TreeItem{

    public Bubble bubble;

    public OutlineItem(Bubble b) {
        this.bubble = b;
        this.valueProperty().bind(b.Title);
        if(b.children.size()>0){
            for(final Bubble c : b.children){
                Task t = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        OutlineItem.this.getChildren().add(new OutlineItem(c));
                        return null;
                    }
                };
                new Thread(t).start();
            }
        }
    }

}