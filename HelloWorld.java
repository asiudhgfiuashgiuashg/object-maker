import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.shape.Line;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import javafx.scene.image.Image;

public class HelloWorld extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private double mouseDownX;
    private double mouseDownY;
    private ArrayList<Line> lines;
    private int posOfCurrentLineInList = -1; // so it will be zero when first incremented
    
    @Override
    public void start(Stage primaryStage) {
        lines = new ArrayList<>();
        
        



        primaryStage.setTitle("Draw Shapes");
        
        Pane root = new Pane();

        primaryStage.setScene(new Scene(root, 300, 250));

        root.addEventFilter(MouseEvent.MOUSE_PRESSED, 
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            System.out.println("MOUSE_PRESSED: " + "x: " + String.valueOf(event.getSceneX()) + " ,y: " + String.valueOf(event.getSceneY()));
                            mouseDownX = event.getSceneX();
                            mouseDownY = event.getSceneY();
                            
                            Line line = new Line();
                            lines.add(line);
                            line.setStartX(mouseDownX);
                            line.setStartY(mouseDownY);
                            //line is initially a dot
                            line.setEndX(mouseDownX);
                            line.setEndY(mouseDownY);
                            posOfCurrentLineInList++;
                            root.getChildren().add(line);
                        }
                    });

        root.addEventFilter(MouseEvent.MOUSE_RELEASED, 
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            System.out.println("MOUSE_RELEASED: " + "x: " + String.valueOf(event.getSceneX()) + " ,y: " + String.valueOf(event.getSceneY()));
                        }
                    });
                    
        root.addEventFilter(MouseEvent.MOUSE_DRAGGED, 
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            System.out.println("MOUSE_DRAGGED: " + "x: " + String.valueOf(event.getSceneX()) + " ,y: " + String.valueOf(event.getSceneY()));
                            Line currentLine = lines.get(posOfCurrentLineInList);
                            currentLine.setEndX(event.getSceneX());
                            currentLine.setEndY(event.getSceneY());
                        }
                    });


        primaryStage.show();
    }

    private void drawImage(String name)
    {
        Image image = new Image("../core/assets/" + name);
        

    }
}