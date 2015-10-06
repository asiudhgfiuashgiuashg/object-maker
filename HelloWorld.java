import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.shape.Line;
import javafx.scene.input.MouseEvent;

public class HelloWorld extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private boolean mouseDown = false;

    @Override
    public void start(Stage primaryStage) {

        Line line = new Line();
        line.setStartX(0.0f);
        line.setStartY(0.0f);
        line.setEndX(100.0f);
        line.setEndY(100.0f);
        



        primaryStage.setTitle("Draw Shapes");
        
        StackPane root = new StackPane();

        root.getChildren().add(line);
        primaryStage.setScene(new Scene(root, 300, 250));

        root.addEventFilter(MouseEvent.MOUSE_PRESSED, 
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            System.out.println("MOUSE_PRESSED: " + "x: " + String.valueOf(event.getSceneX()) + " ,y: " + String.valueOf(event.getSceneY()));
                            mouseDown = true;
                        }
                    });

        root.addEventFilter(MouseEvent.MOUSE_RELEASED, 
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            System.out.println("MOUSE_RELEASED: " + "x: " + String.valueOf(event.getSceneX()) + " ,y: " + String.valueOf(event.getSceneY()));
                            mouseDown = false;
                        }
                    });

        primaryStage.show();
    }
}