# object-maker
create and edit game objects
Game objects will be placed on the map.


## Required Library
[JsonBeans 0.7] (https://github.com/EsotericSoftware/jsonbeans/releases)
## Usage
### Compiling
javac -classpath .:jsonbeans-0.7.jar *.java
### Running
java -classpath .:jsonbeans-0.7.jar ObjectMaker $object_file_name

If $object_file_name is a file that doesn't exist yet, object-maker will create it.

### Editing object image
Click the button above Save to select an image for the object
### Editing object hitbox
Click and drag to create a new line in the hitbox.
Click a line to select it and press delete to remove selected lines.
Press ENTER/RETURN to connect the first and last lines to close off the hitbox.
Press CTRL+A to select all of the lines
### Saving
Click save and the object will be saved to $object_file_name (the first command-line argument)



## Future
As the requirements of the game expand, so will this tool.

## Known Bugs
Sometimes a line cant be placed. Pressing ENTER fixes this.
