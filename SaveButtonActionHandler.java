/*
 * json library
 */
import com.esotericsoftware.jsonbeans.*;

import java.io.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.*;
import java.awt.Point;


/**
 * performs the saving of the gameobject
 */
public class SaveButtonActionHandler implements EventHandler<ActionEvent> {
	private GameObject gameObject;
	private List<SelectableLine> lines;

	/**
	 * @param gameObject the object to save
	 * @param lines the lines to convert to gameObject's hitbox points before saving
	 */
	public SaveButtonActionHandler(GameObject gameObject, List<SelectableLine> lines) {
		this.gameObject = gameObject;
		this.lines = lines;
	}

	@Override
	public void handle(ActionEvent e) {
        System.out.println("saving");
        putLinesInGameObject();
        Json json = new Json();
        json.setSerializer(GameObject.class, new GameObjectSerializer());
        FileWriter writer = null;
        try {
        	writer = new FileWriter(new File("test.out"));
        } catch (IOException exception) {
        	System.out.println("issue making the FileWriter");
        	System.exit(-1);
        }
        json.toJson(gameObject, writer);
    }

    /**
     * take the lines on the canvas and convert them into points for the object's hitbox
     * call this before saving
     */
    private void putLinesInGameObject() {
        gameObject.hitboxPoints.clear();
        for (SelectableLine line: lines) {
            gameObject.hitboxPoints.add(new Point((int) line.getStartX(), (int) line.getStartY()));
        }
    }
}