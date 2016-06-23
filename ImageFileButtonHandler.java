/*
 * json library
 */
import com.esotericsoftware.jsonbeans.*;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.image.*;


/**
 * performs the saving of the gameobject
 */
public class ImageFileButtonHandler implements EventHandler<ActionEvent> {

    /**
     * need this to open a file chooser
     */
    private Stage stage;

    /**
     * set the content of thihs image view to the image file thhat was chosen
     */
    private ImageView imageView;

    /**
     * need a reference to gameObject to update what image it uses
     */
    private GameObject gameObject;

	public ImageFileButtonHandler(Stage stage, ImageView imageView, GameObject gameObject) {
        this.stage = stage;
        this.imageView = imageView;
        this.gameObject = gameObject;
	}

	@Override
	public void handle(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an image for this object");
        File imgFile = fileChooser.showOpenDialog(stage);
        Image img = new Image(imgFile.getName());
        imageView.setImage(img);
        imageView.setFitWidth(img.getWidth());
        imageView.setFitHeight(img.getHeight());
        System.out.println("fit width: " + imageView.getFitWidth());
        System.out.println("fit height: " + imageView.getFitHeight());
        gameObject.imageFile = imgFile;
    }


}