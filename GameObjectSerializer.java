/*
 * json library
 */
import com.esotericsoftware.jsonbeans.*;

import java.awt.Point;
import java.util.*;
import java.io.*;


/**
 * used to construct the object-maker's representation of a game object
 */
public class GameObjectSerializer implements JsonSerializer<GameObject> {
	
	@Override
	public void write(Json json, GameObject gameObject, Class type) {
		json.writeObjectStart();
		json.writeValue("hitbox", gameObject.hitboxPoints);
		json.writeValue("image", gameObject.imageFile.getName());
		json.writeObjectEnd();
	}

	@Override
	public GameObject read(Json json, JsonValue jsonValue, Class type) {
		/*
		 * get the filename of the image for this object
		 */
		String imgStr = jsonValue.get("image").asString();
		File imgFile = new File(imgStr);
		/*
		 * get the list of points of the hitbox of this object in order
		 */
		JsonValue pointArray = jsonValue.get("hitbox");
		List<Point> hitboxPoints = new ArrayList<>();
		for (JsonValue jsonPoint: pointArray) {
			Point point = new Point(jsonPoint.get("x").asInt(), jsonPoint.get("y").asInt());
			hitboxPoints.add(point);
		}
		/*
		 * construct the object
		 */
		return new GameObject(imgFile, hitboxPoints);

	}
}