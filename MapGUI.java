import java.io.File;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.GridPane;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.util.Optional;
import java.util.Scanner;


public class MapGUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

	private	int tileSizeX;
	private int tileSizeY;
	private int tileCols;
	private int tileRows;
	private String defTilePath;
    
    @Override
    public void start(Stage primaryStage) {
    	primaryStage.setTitle("MapGUI");
    	Scene scene = new Scene(new VBox(), 400, 250);

    	// Scroll pane and grid pane for tiles
    	final ScrollPane sp = new ScrollPane();
    	final GridPane gp = new GridPane();
    	sp.setContent(gp);
    	
    	//----------------------------------------------------------------------//
    	
    	//////////////////////
    	// New - dialog box //
    	//////////////////////
    	Dialog<String[]> newMapDialog = new Dialog<>();
    	newMapDialog.setTitle("Create New Map");
    	newMapDialog.setHeaderText(null);
    	
    	ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
    	newMapDialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
    	
    	GridPane grid = new GridPane();
    	grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(20, 150, 10, 10));

    	TextField tileNumX = new TextField("10");
    	TextField tileNumY = new TextField("10");

    	grid.add(new Label("# of tiles X:"), 0, 0);
    	grid.add(tileNumX, 1, 0);
    	grid.add(new Label("# of tiles Y:"), 0, 1);
    	grid.add(tileNumY, 1, 1);
    	
    	Text defTileText = new Text();
    	defTileText.setWrappingWidth(200);
    	defTileText.setTextAlignment(TextAlignment.JUSTIFY);
    	defTileText.setText("no selection made");
    	
    	FileChooser defTileChooser = new FileChooser();
    	defTileChooser.setTitle("Choose default tile");
    	defTileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
    	FileChooser.ExtensionFilter imgFilter = new FileChooser.ExtensionFilter("Images (*.png, *.jpg)", "*.png", "*.jpg");
    	defTileChooser.getExtensionFilters().add(imgFilter);
    	
    	Button defTileChooserBtn = new Button("Open");
    	defTileChooserBtn.setOnAction(new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent e) {
    			File defTileFile = defTileChooser.showOpenDialog(primaryStage);
    			if (defTileFile != null) {
    				// Convert absolute image location to relative location
    				String workingDir = System.getProperty("user.dir");  				
    				String relativePath = new File(workingDir).toURI().relativize(defTileFile.toURI()).getPath();
    				
    				defTileText.setText(relativePath);
    			}
    		}
    	});
    	    	    	
    	grid.add(new Label("Choose default tile"), 0, 2);
    	grid.add(defTileText, 1, 2);
    	grid.add(defTileChooserBtn, 2, 2);
    	
    	newMapDialog.getDialogPane().setContent(grid);
    	
    	newMapDialog.setResultConverter(dialogButton -> {
    	    if (dialogButton == okButtonType) {
    	    	// Return input data
    	    	String[] returnStr = new String[3];
    	    	returnStr[0] = tileNumX.getText();
    	    	returnStr[1] = tileNumY.getText();
    	    	returnStr[2] = defTileText.getText();
    	    	return returnStr;
    	    }
    	    return null;
    	});
    	
    	//////////////////////
    	// Open - dialog box //
    	//////////////////////
    	FileChooser openMapChooser = new FileChooser();
    	openMapChooser.setTitle("Open Map");
    	openMapChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
    	FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
    	openMapChooser.getExtensionFilters().add(txtFilter);
    	
    	//////////////////////
    	// Save - dialog box //
    	//////////////////////
    	
    	
    	//----------------------------------------------------------------------//
    	
    	// File Menu    	
    	Menu menuFile = new Menu("File");
    	
    	///////////////////////
    	// New - menu button //
    	///////////////////////
    	MenuItem newBtn = new MenuItem("New");
    	newBtn.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent t) {
    			Optional<String[]> newMapResult = newMapDialog.showAndWait();
    			if (newMapResult.isPresent()) {
    				// Clear grid
    				gp.getChildren().clear();
    				
    				// Get inputs from the create map dialog box
    				tileCols = Integer.parseInt(newMapResult.get()[0]);
    				tileRows = Integer.parseInt(newMapResult.get()[1]);
    				defTilePath = newMapResult.get()[2];
    				 
    				// Get image and fill grid with default tile
    				Image defTileImage = null;
    				try {
    					defTileImage = new Image(defTilePath);
    				} catch (IllegalArgumentException e) {
    					System.out.println("Default tile file not found or file out of map-maker directory");
    					System.exit(0);
    				}
    				for(int i=0; i<tileRows; i++){
    					for(int j=0; j<tileCols; j++){
    						ImageView tempImageView = new ImageView();
    						tempImageView.setImage(defTileImage);
    						gp.add(tempImageView, i, j);
    					}
    				}
    			}
    		}
    	});
    	
    	////////////////////////
    	// Open - menu button //
    	////////////////////////
    	MenuItem openBtn = new MenuItem("Open");
    	openBtn.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent t) {
    			// load in tiles
    			File openMapFile = openMapChooser.showOpenDialog(primaryStage);
    			Scanner mapFileScanner = new Scanner(openMapFile);
    			mapTitle = mapFileScanner.nextLine();
    	        inputRows = Integer.parseInt(mapFileScanner.nextLine());
    	        inputCols = Integer.parseInt(mapFileScanner.nextLine());
    	        
    		}
    	});
    	
    	////////////////////////
    	// Save - menu button //
    	////////////////////////
    	MenuItem saveBtn = new MenuItem("Save");
    	saveBtn.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent t) {
    			// saveMapDialog.showAndWait();
    		}
    	});
    	
    	//----------------------------------------------------------------------//
    	
    	// Add buttons to File dropdown
    	menuFile.getItems().addAll(newBtn, openBtn, saveBtn);

    	// Menu Bar    	
    	MenuBar menuBar = new MenuBar();
    	menuBar.getMenus().add(menuFile);
    	
    	// Add elements to scene
    	((VBox) scene.getRoot()).getChildren().addAll(menuBar, sp);
    	
    	// Show GUI
    	primaryStage.setScene(scene);
    	primaryStage.show();
    }
}