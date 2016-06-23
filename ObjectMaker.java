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
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.concurrent.Task;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import java.io.*;
import java.awt.Point;
import javafx.beans.value.*;

/*
 * json library
 */
import com.esotericsoftware.jsonbeans.*;

public class ObjectMaker extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private String fileName;
    private double mouseDownX;
    private double mouseDownY;
    private List<SelectableLine> lines;

    private boolean lineInProgress = false;
    private final Color DARKER_GREY = Color.rgb(10, 10, 10);
    private boolean finishedState = false;
    private GameObject gameObject = null;
    private Pane imagePane;

    private ImageView imageView;
    /**
     * the file that the game object is loaded from or saved to
     */
    private File objectFile;

    @Override
    public void start(Stage primaryStage) {
        
        Image image = null;
        try {
            fileName = this.getParameters().getUnnamed().get(0);
            objectFile = new File(fileName);
            if (objectFile.exists()) {
                gameObject = getGameObjectFromFile(objectFile);
                

            } else { //user-specified gameobject file doesnt exist
                /*
                 * make a gameobject without a hitbox or image
                 */
                gameObject = new GameObject();
                gameObject.imageFile = new File("default_image.png"); //placeholder
            }
            image = new Image(gameObject.imageFile.getName());
            
        } catch (IllegalArgumentException e) {
            System.out.println("pls use a real file ty");
            System.exit(0);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("pls provide file name ty");
            System.exit(0);
        }

        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();

        lines = new ArrayList<>();


        primaryStage.setTitle("Game Object File: " + objectFile.getName());
        
        VBox root = new VBox();
        imagePane = new Pane();
        imagePane.setPrefSize(imageWidth, imageHeight);

        
        
        root.getChildren().add(imagePane);

        
        
        imageView = new ImageView();
        imageView.setImage(image);
        imagePane.getChildren().add(imageView);
        
        

        /*
         * add button which is used to select the object's image
         */
        Button imageFileButton = new Button(gameObject.imageFile.getName());
        imageFileButton.setPrefHeight(30);
        imageFileButton.setOnAction(new ImageFileButtonHandler(primaryStage, imageView, gameObject));
        root.getChildren().add(imageFileButton);

        /*
         * create button which is used to save
         */
        Button saveButton = new Button("SAVE");
        saveButton.setOnAction(new SaveButtonActionHandler(gameObject, lines, objectFile));
        saveButton.setPrefHeight(30);
        root.getChildren().add(saveButton);

        /*
         * the following listeners support the automatic resizing of the application upon selecting a new image for the object
         */
        imageView.fitHeightProperty().addListener(new ChangeListener<Number>() {

                @Override
                public void changed(ObservableValue<? extends Number> observable,
                        Number oldValue, Number newValue) {
                    imagePane.setPrefHeight(newValue.doubleValue());
                    primaryStage.setHeight(imageView.getImage().getHeight() + saveButton.getHeight() + imageFileButton.getHeight());
            }
        });

        imageView.fitWidthProperty().addListener(new ChangeListener<Number>() {

                @Override
                public void changed(ObservableValue<? extends Number> observable,
                        Number oldValue, Number newValue) {
                    imagePane.setPrefWidth(newValue.doubleValue());
                    primaryStage.setWidth(imageView.getImage().getWidth());
            }
        });

        double sceneHeight = imageHeight + saveButton.getPrefHeight() + imageFileButton.getPrefHeight();


        
        


	    Scene scene = new Scene(root, imageWidth, sceneHeight);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        
        

    	

	
        System.out.println("imageHeight: " + String.valueOf(imageHeight));
        root.setAlignment(Pos.BOTTOM_CENTER);
        primaryStage.show();
	
	    populateLines();

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
    
                    if (mouseDownY < imageView.getImage().getHeight()) {
                        SelectableLine line = new SelectableLine();
                        line.setStroke(SelectableLine.LIGHTER_GREY);
    
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
                    } else if (event.getSceneY() > imageView.getImage().getHeight()) {
                        endY = imageView.getImage().getHeight();
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
                        line.setStroke(SelectableLine.LIGHTER_GREY);
                        lines.add(line);
                        line.setStartX(lines.get(lines.size() - 2).getEndX());
                        line.setStartY(lines.get(lines.size() - 2).getEndY());
                        line.setEndX(lines.get(0).getStartX());
                        line.setEndY(lines.get(0).getStartY());
                        imagePane.getChildren().add(line);
                        finishedState = true;
                    } else { // if the final line exists and we want to remove it and keep drawing
                        imagePane.getChildren().remove(lines.get(lines.size() - 1));
                        lines.remove(lines.size() - 1);
                        finishedState = false;
                    }
                }
            }
        });


        primaryStage.show();
    }
    
    /**
     * take the points of the object's hitbox and make them into lines on the canvas
     */
    private void populateLines() {
        for (int i = 0; i < gameObject.hitboxPoints.size(); i += 1) {
            int beginIndex = i - 1 >= 0 ? i - 1 : gameObject.hitboxPoints.size() + i - 1;
            int endIndex = i;
            SelectableLine line = new SelectableLine(gameObject.hitboxPoints.get(beginIndex), gameObject.hitboxPoints.get(endIndex));
            imagePane.getChildren().add(line);
            lines.add(line);
        }
    }

    

    /**
     * @param fileName the name of the object file in the same directory
     * @return the jsonvalue stored in this file
     */
    private GameObject getGameObjectFromFile(File file) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            System.out.println("game object file: " + file.getName() + " not found");
            System.exit(-1);
        }
        
        GameObjectSerializer serializer = new GameObjectSerializer();
        Json json = new Json();
        json.setSerializer(GameObject.class, new GameObjectSerializer());
        return json.fromJson(GameObject.class, fileReader);
    }

}

