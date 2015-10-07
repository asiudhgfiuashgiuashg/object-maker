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
import javafx.scene.control.TextArea;

public class HelloWorld extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private double mouseDownX;
    private double mouseDownY;
    private ArrayList<Line> lines;
    private int posOfCurrentLineInList = -1; // so it will be zero when first incremented
    private TextArea textArea;
    private int imageWidth = 300;
    private int imageHeight = 250;
    private double imageHeightScaleForAllocatingSpaceForTextarea;
    @Override
    public void start(Stage primaryStage) {
        lines = new ArrayList<>();
        imageHeightScaleForAllocatingSpaceForTextarea = 1.5;

        primaryStage.setTitle("Draw Shapes");
        
        Pane root = new Pane();
        double sceneHeight = imageHeight * imageHeightScaleForAllocatingSpaceForTextarea;
	    Scene scene = new Scene(root, imageWidth, sceneHeight);
        primaryStage.setScene(scene);
	    scene.getStylesheets().add(HelloWorld.class.getResource("login.css").toExternalForm());
	    primaryStage.show();
	    drawImage(root);

        textArea = new TextArea("Shape output will go here.");
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setLayoutY(imageHeight);
        root.getChildren().add(textArea);

        root.addEventFilter(MouseEvent.MOUSE_PRESSED, 
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            System.out.println("MOUSE_PRESSED: " + "x: " + String.valueOf(event.getSceneX()) + " ,y: " + String.valueOf(event.getSceneY()));
                            mouseDownX = event.getSceneX();
                            mouseDownY = event.getSceneY();
                            
                            if (mouseDownY < imageHeight) {
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
                            if (lines.size() > 0) {
                                Line currentLine = lines.get(posOfCurrentLineInList);
                                currentLine.setEndX(event.getSceneX() >= 0 ? event.getSceneX() : 0);
                                double endY;
                                if (event.getSceneY() < 0) {
                                    endY = 0;
                                } else if (event.getSceneY() > imageHeight) {
                                    endY = imageHeight;
                                } else {
                                    endY = event.getSceneY();
                                }
                                currentLine.setEndY(endY);
                            }
                        }
                    });


        primaryStage.show();
    }

    private void drawImage(Pane root)
    {
	
        /*Image image = new Image("../core/assets/color_wheel.png);
        root.getChildren().add(image);*/

    }
}