package mindmap.view;

import javafx.scene.Node;
import javafx.scene.shape.CubicCurve;

public class MyCubicCurve extends CubicCurve {

    private Node parent;

    public MyCubicCurve() {
    }

    public Node getMyParent() {
        return this.parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}
