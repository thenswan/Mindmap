package mindmap.view;

import javafx.concurrent.Task;
import javafx.scene.control.TreeItem;

public class OutlineItem extends TreeItem {

    public OutlineItem(Bubble bubble) {
        this.valueProperty().bind(bubble.Title);

        if (bubble.childrenBubblesArrayList.size() > 0) {
            for (final Bubble bubbleChild : bubble.childrenBubblesArrayList) {
                Task t = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        OutlineItem.this.getChildren().add(new OutlineItem(bubbleChild));
                        return null;
                    }
                };
                new Thread(t).start();
            }
        }
    }

}
