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
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.concurrent.Task;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

public class ShapeDraw extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private double mouseDownX;
    private double mouseDownY;
    private List<SelectableLine> lines;
    private TextArea textArea;
    private int imageWidth = 300;
    private int imageHeight = 250;
    private double imageHeightScaleForAllocatingSpaceForTextarea;
    private boolean lineInProgress = true;
    private final Color LIGHTER_GREY = Color.rgb(50, 50, 50);
    private final Color DARKER_GREY = Color.rgb(10, 10, 10);

    @Override
    public void start(Stage primaryStage) {
        lines = new ArrayList<>();
        imageHeightScaleForAllocatingSpaceForTextarea = 1.5;

        primaryStage.setTitle("Draw Shapes");
        
        VBox root = new VBox();
        Pane imagePane = new Pane();
        imagePane.setPrefSize(imageWidth, imageHeight);
        double sceneHeight = imageHeight * imageHeightScaleForAllocatingSpaceForTextarea;
        root.getChildren().add(imagePane);
	    Scene scene = new Scene(root, imageWidth, sceneHeight);
        primaryStage.setScene(scene);

	
        Image image = new Image("color_wheel.png");
	ImageView view = new ImageView();
	view.setImage(image);
	root.getChildren().add(view);

        /*primaryStage.setResizable(false);
	    scene.getStylesheets().add(ShapeDraw.class.getResource("login.css").toExternalForm());
	    drawImage(root);*/

        textArea = new TextArea("Shape output will go here.");
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setLayoutY(imageHeight);
        root.getChildren().add(textArea);
        root.setAlignment(Pos.BOTTOM_CENTER);
        primaryStage.show();

        imagePane.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("mouse entered imagePane - focusing image Pane");
                imagePane.requestFocus();
            }
        });


        imagePane.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("dragdetected");
                //System.out.println("MOUSE_DRAGGED: " + "x: " + String.valueOf(event.getSceneX()) + " ,y: " + String.valueOf(event.getSceneY()));
                lineInProgress = true;

                mouseDownX = event.getSceneX();
                mouseDownY = event.getSceneY();

            
                if (mouseDownY < imageHeight) {
                    SelectableLine line = new SelectableLine();
                    line.setStroke(LIGHTER_GREY);
                    lines.add(line);
                    line.setStartX(mouseDownX);
                    line.setStartY(mouseDownY);
                    //line is initially a dot
                    line.setEndX(mouseDownX);
                    line.setEndY(mouseDownY);

                    line.setStrokeWidth(4);

                    //for selecting lines
                    line.setOnMousePressed(new EventHandler<MouseEvent>() {
                        //indicate mouse down click
                        @Override
                        public void handle(MouseEvent event) {
                            if (line.isSelected()) {
                                line.setStroke(Color.DARKRED);
                            } else {
                                line.setStroke(DARKER_GREY);
                            }
                        }
                    });

                    line.setOnMouseReleased(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (line.isSelected()) {
                                line.setStroke(LIGHTER_GREY);
                                line.setSelected(false);
                            } else {
                                line.setStroke(Color.RED);
                                line.setSelected(true);
                            }
                        }
                    });



                    imagePane.getChildren().add(line);
                }
            }
        });

        imagePane.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("mouse released");
                if (lineInProgress) {
                    lineInProgress = false;
                }   
            }
        });
        imagePane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("MOUSE_DRAGGED: " + "x: " + String.valueOf(event.getSceneX()) + " ,y: " + String.valueOf(event.getSceneY()));
                if (lines.size() > 0 && lineInProgress) {
                    Line currentLine = lines.get(lines.size() - 1);
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


        // handle keypresses here:
        imagePane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                System.out.println(event);
                if (event.getCode() == KeyCode.DELETE) {
                    for (SelectableLine line: lines) {
                        if (line.isSelected()) {
                            System.out.println(root.getChildren().size());
                            root.getChildren().removeAll(line);
                            System.out.println(root.getChildren().size());

                            // ui thread shouldn't modify lines list apparently WHY???
                            Task<Void> removeLine = new Task<Void>() {
                                @Override
                                protected Void call() {
                                    lines.remove(line);
                                    
                                    return null;
                                }
                            };

                            imagePane.getChildren().remove(line);
                            System.out.println("removing line: " + line);
                        }
                    }
                }
            }
        });

        textArea.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("mouse entered textArea - focusing text area");
                imagePane.requestFocus();
            }
        });

        primaryStage.show();
    }
    

    private void drawImage(Pane root)
    {
	
        Image image = new Image("../core/assets/color_wheel.png");
	ImageView view = new ImageView();
	view.setImage(image);
        

    }

    private class SelectableLine extends Line {
        private boolean selected;

        private SelectableLine() {
            super();
            selected = false;
        }

        private void setSelected(boolean selected) {
            this.selected = selected;
        }

        private boolean isSelected() {
            return selected;
        }
    }
}

