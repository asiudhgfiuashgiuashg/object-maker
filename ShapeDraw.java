import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.shape.Line;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
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

    private String fileName;
    private double mouseDownX;
    private double mouseDownY;
    private List<SelectableLine> lines;
    private TextArea textArea;
    private double imageWidth = 300;
    private double imageHeight = 300;
    private double imageHeightScaleForAllocatingSpaceForTextarea;
    private boolean lineInProgress = true;
    private final Color LIGHTER_GREY = Color.rgb(50, 50, 50);
    private final Color DARKER_GREY = Color.rgb(10, 10, 10);
    private final int LINE_THICKNESS = 4;
    private boolean finishedState = false;

    @Override
    public void start(Stage primaryStage) {
        
        Image image = null;
        try {
           fileName = this.getParameters().getUnnamed().get(0);
           image = new Image(fileName);
        } catch (IllegalArgumentException e) {
            System.out.println("pls use a real file ty");
            System.exit(0);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("pls provide file name ty");
            System.exit(0);
        }

        imageWidth = image.getWidth();
        imageHeight = image.getHeight();

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
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        
       

    	ImageView view = new ImageView();
    	view.setImage(image);
    	imagePane.getChildren().add(view);
	    

	

        textArea = new TextArea("Shape output will go here.");
        textArea.setEditable(true);
        textArea.setWrapText(true);

        textArea.setPrefHeight(sceneHeight - imageHeight);
        System.out.println("imageHeight: " + String.valueOf(imageHeight));
        root.getChildren().add(textArea);
        root.setAlignment(Pos.BOTTOM_CENTER);
        primaryStage.show();
	
	    System.out.println(scene.getWidth());

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
                if (!finishedState) {
                    lineInProgress = true;
    
                    mouseDownX = event.getSceneX();
                    mouseDownY = event.getSceneY();
    
                    if (mouseDownY < imageHeight) {
                        SelectableLine line = new SelectableLine();
                        line.setStroke(LIGHTER_GREY);
    
                        if(lines.isEmpty()) {
                            lines.add(line);
                            line.setStartX(mouseDownX);
                            line.setStartY(mouseDownY);
                        } else {
                            lines.add(line);
                            line.setStartX(lines.get(lines.size() -2).getEndX());
                            line.setStartY(lines.get(lines.size() -2).getEndY());
                        }
                        //line is initially a dot
                        line.setEndX(mouseDownX);
                        line.setEndY(mouseDownY);
    
                        line.setStrokeWidth(LINE_THICKNESS);
    
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
    
                                if (line.isSelected() || lineInProgress) {
                                    line.setStroke(LIGHTER_GREY);
                                    line.setSelected(false);
                                } else {
                                    line.setSelected(true);
                                }
                            }
                        });
    
    
    
                        imagePane.getChildren().add(line);
                    }
                }
            }
        });

        imagePane.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("mouse released");
                if (lineInProgress) {
                    lineInProgress = false;
                    resetLinePoints(textArea);

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
                    List<SelectableLine> linesToRemove = new ArrayList<SelectableLine>();
                    for (SelectableLine line: lines) {
                        if (line.isSelected()) {
                            linesToRemove.add(line);
                        }
                    }
                    for (SelectableLine line: linesToRemove) {
                        lines.remove(line);
                        System.out.println("REMOVED A LIEN");

                        imagePane.getChildren().remove(line);
                        System.out.println("removing line: " + line);

                        resetLinePoints(textArea);
                    }
                }
                if (event.isControlDown() && event.getCode() == KeyCode.A) {
                    for (SelectableLine line: lines) {
                        line.setSelected(true);
                    }
                }
                if (event.getCode() == KeyCode.ENTER) {
                    if (!finishedState) { // if the final line doesn't exist yet
                        SelectableLine line = new SelectableLine();
                        line.setStroke(LIGHTER_GREY);
                        lines.add(line);
                        line.setStartX(lines.get(lines.size() - 2).getEndX());
                        line.setStartY(lines.get(lines.size() - 2).getEndY());
                        line.setEndX(lines.get(0).getStartX());
                        line.setEndY(lines.get(0).getStartY());
                        imagePane.getChildren().add(line);
                        line.setStrokeWidth(LINE_THICKNESS);
                        finishedState = true;
                    } else { // if the final line exists and we want to remove it and keep drawing
                        imagePane.getChildren().remove(lines.get(lines.size() - 1));
                        lines.remove(lines.size() - 1);
                        finishedState = false;
                    }
                    resetLinePoints(textArea);
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
    

    private void resetLinePoints(TextArea text)
    {
	
        text.clear();
        System.out.println(lines.size());
        int numberOfLines = lines.size();
        String points = "";
        for (int x = 0; x < numberOfLines; x++)
        {
            points += lines.get(x).getStartX() + ", " + (imageHeight - lines.get(x).getStartY()) + ", " + lines.get(x).getEndX() + ", " + (imageHeight - lines.get(x).getEndY());

            if (x < numberOfLines - 1)
            {
                points += ", ";
            } 
            
        }
        text.appendText(points);

    }

    private class SelectableLine extends Line {
        private boolean selected;

        private SelectableLine() {
            super();
            selected = false;
        }

        private void setSelected(boolean selected) {
            if (selected) {
                this.setStroke(Color.RED);
            }
            this.selected = selected;
        }

        private boolean isSelected() {
            return selected;
        }
    }
}

