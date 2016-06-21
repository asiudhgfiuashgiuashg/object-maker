import javafx.scene.shape.Line;
import java.awt.Point;
import javafx.scene.paint.Color;

public class SelectableLine extends Line {
    private boolean selected;
    private final int LINE_THICKNESS = 4;

    public SelectableLine() {
        super();
        this.setStrokeWidth(LINE_THICKNESS);
        selected = false;
    }

    public SelectableLine(Point start, Point end) {
        this();
        this.setStartX(start.getX());
        this.setStartY(start.getY());

        this.setEndX(end.getX());
        this.setEndY(end.getY());
    }

    protected void setSelected(boolean selected) {
        if (selected) {
            this.setStroke(Color.RED);
        }
        this.selected = selected;
    }

    protected boolean isSelected() {
        return selected;
    }
}