import java.io.*;
import java.util.*;
import java.awt.Point;

/**
 * represents an object
 */
public class GameObject {
	/*
	 * the file that will be displayed for this gameObject
	 */
	protected File imageFile;
	/*
	 * list of points making up the hitbox of the Gameobject
	 */
	protected List<Point> hitboxPoints;

	public GameObject() {
		this.hitboxPoints = new ArrayList<>();
	}

	public GameObject(File imageFile, List<Point> hitboxPoints) {
		this.imageFile = imageFile;
		this.hitboxPoints = hitboxPoints;
	}
}