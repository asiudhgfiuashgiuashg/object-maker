import javafx.scene.shape.Line;
import java.awt.Point;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class SelectableLine extends Line {
    private boolean selected;
    private final int LINE_THICKNESS = 4;
    private static final Color DARKER_GREY = Color.rgb(10, 10, 10);
    protected static final Color LIGHTER_GREY = Color.rgb(50, 50, 50);

    public SelectableLine() {
        super();
        this.setStrokeWidth(LINE_THICKNESS);
        this.setOnMousePressed(new MousePressHandler());
        this.setOnMouseReleased(new MouseReleaseHandler());
        this.setStroke(LIGHTER_GREY);
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

    protected class MousePressHandler implements EventHandler<MouseEvent> {
        private SelectableLine line;
        protected MousePressHandler() {
            this.line = SelectableLine.this;
        }

        //indicate mouse down click
        @Override
        public void handle(MouseEvent event) {
            if (line.isSelected()) {
                line.setStroke(Color.DARKRED);
            } else {
                line.setStroke(DARKER_GREY);
            }
        }
    }

    protected class MouseReleaseHandler implements EventHandler<MouseEvent> {
        private SelectableLine line;
        protected MouseReleaseHandler() {
            this.line = SelectableLine.this;
        }

        @Override
        public void handle(MouseEvent event) {

            if (line.isSelected()) {
                line.setSelected(false);
            } else {
                line.setSelected(true);
            }
        }
    }
}