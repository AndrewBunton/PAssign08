package keypad;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * File: PAssign08.java
 * Package: keypad
 * @author Andrew Bunton
 * Created on: Nov 12, 2024
 * Last Modified:  Nov 14, 2024
 * Description: Class that makes a remote control and a TV screen. 
 * Link to GitHub: https://github.com/AndrewBunton/hello-world/tree/main
 */

public class PAssign08 extends Application{
	// Placing the Stage in instance data may work
	private Stage tvStage = new Stage();
	private Circle circle = new Circle(10);
	
	@Override
	public void start(Stage primaryStage) {
		// Begin with the remote
		FlowPane fpRemote = new FlowPane();
		RemotePadPane remotePadPane = new RemotePadPane();
		GridPane gpRemote = new GridPane();
		
		fpRemote.setAlignment(Pos.CENTER);
		fpRemote.setVgap(10);
		
		gpRemote.setAlignment(Pos.CENTER);
		gpRemote.setVgap(5);
		gpRemote.setHgap(5);
		
		remotePadPane.setAlignment(Pos.CENTER);
		
		// Add the buttons for OK and directional arrows to the GridPane
		Button btOK = new Button("OK");
		Button btUp = new Button("     ");
		Button btDown = new Button("     ");
		gpRemote.add(btOK, 1, 1);
		gpRemote.add(btUp, 1, 0);
		gpRemote.add(btDown, 1, 2);
		
		fpRemote.getChildren().add(remotePadPane);
		fpRemote.getChildren().add(gpRemote);
		fpRemote.setOrientation(Orientation.VERTICAL);
		btUp.setAlignment(Pos.CENTER);
		
		primaryStage.setTitle("Remote");
		Scene remoteScene = new Scene(fpRemote, 100, 250);
		primaryStage.setScene(remoteScene);
		primaryStage.setResizable(false);
		primaryStage.setX(250);
		primaryStage.setY(250);
		
		// Now make the TV Screen
		// This will be a StackPane to allow for a background
		StackPane spTV = new StackPane();
		spTV.setAlignment(Pos.CENTER);
		
		// Rectangle to act as the background
		Rectangle bgRect = new Rectangle();
		bgRect.widthProperty().bind(spTV.widthProperty());
		bgRect.heightProperty().bind(spTV.heightProperty());
		bgRect.setFill(Color.CADETBLUE);
		
		circle.setFill(Color.BLUE);
		spTV.getChildren().addAll(bgRect, circle);
		
		// Now add functionality to the buttons on the remote
		for (Button bt : remotePadPane.copyListButtons) {
			bt.setOnAction(e -> remotePadPane.resize(bt, circle));
		}
		
		btUp.setOnAction(e -> {
			circle.setRadius(circle.getRadius() + 10);
		});
		
		btDown.setOnAction(e -> {
			circle.setRadius(circle.getRadius() - 10);
		});
		
		// New Scene and Stage
		Scene tvScene = new Scene(spTV, 500, 500);
		tvStage.setScene(tvScene);
		tvStage.setTitle("TV Screen");
		tvStage.show();
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}

/**
 * File: PAssign08.java
 * Package: keypad
 * @author Andrew Bunton
 * Created on: Nov 14, 2024
 * Last Modified: Nov 14, 2024
 * Description: Class specifically for the keypad on a remote control
 */

class RemotePadPane extends KeyPadPane {
	// Default constructor
	public RemotePadPane() {
		super(true);
	}
	
	// Method to resize the circle on the TV "screen"
	public void resize(Button bt, Circle circle) {
		String btText = bt.getText();
		
		// Error catching to make sure the button has a number. If not, then size the circle accordingly
		try {
			Double btVal = Double.parseDouble(btText);
			circle.setRadius(btVal * 10);
		} catch (NumberFormatException e) {
			// Check to see which symbol is caught
			// "*" doubles the circle's radius, "#" halves the circle's radius
			if (btText.equals("*")) {
				circle.setRadius(circle.getRadius() * 2);
			} else {
				circle.setRadius(circle.getRadius() / 2);
			}
		}
	}
}

class KeyPadPane extends GridPane {
	// private Buttons 
	protected Button btn1 = new Button("1");
	protected Button btn2 = new Button("2");
	protected Button btn3 = new Button("3");
	protected Button btn4 = new Button("4");
	protected Button btn5 = new Button("5");
	protected Button btn6 = new Button("6");
	protected Button btn7 = new Button("7");
	protected Button btn8 = new Button("8");
	protected Button btn9 = new Button("9");
	protected Button btnBlank1 = new Button("  ");
	protected Button btn0 = new Button("0");
	protected Button btnBlank2 = new Button("  ");
	protected Button btnAsterisk = new Button("*");
	protected Button btnPound = new Button("#");

	// collect all buttons
	private Button[] arrButtons = { btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnBlank1, btn0, btnBlank2};
	protected ArrayList<Button> listButtons = new ArrayList<>(Arrays.asList(arrButtons));
	protected ArrayList<Button> copyListButtons;

	/** 
	 * Create a default KeyPadPane with numbers in order, includes 2 blanks around 0
	 */
	public KeyPadPane() {
		// counter for ArrayList elements
		int counter = 0;

		// place all buttons in 1-9, blank, 0, blank order, 3 per row
		for (int i = 0; i < listButtons.size() / 3; i++) {
			for (int j = 0; j < 3; j++) {
				this.add(listButtons.get(counter++), j, i);
			}
		}

		// call registerEventHandlers() so buttons all work
		registerEventHandlers();
	}

	/** 
	 * Create a KeyPadPane with phone order buttons
	 * @param phoneOrder specify that the user wants phone keypad layout of buttons, 
	 * 		if they use true here, use default layout
	 */
	public KeyPadPane(boolean phoneOrder) {
		// default to default layout for someone choosing false
		this();

		// now check if they want a phone layout
		if (phoneOrder) {
			// get rid of default layout
			this.getChildren().clear();

			// clone list and replace blanks
			copyListButtons = (ArrayList<Button>)listButtons.clone();
			copyListButtons.set(copyListButtons.size() - 3, btnAsterisk);
			copyListButtons.set(copyListButtons.size() - 1, btnPound);

			// counter start at last numeric button index
			int counter = copyListButtons.size() - 4;

			// place all buttons in 9-1, 3 per row
			for (int i = 0; i < (copyListButtons.size() - 3) / 3; i++) {
				for (int j = 0; j < 3; j++) {
					this.add(copyListButtons.get(counter--), j, i);
				}
			}

			// reset counter
			counter = copyListButtons.size() - 1;
			// manually add the blank, 0, blank order
			for (int i = 2; i >= 0; i--) {
				this.add(copyListButtons.get(counter--), i, 3);
			}
			
			// re-register so the phone's different (#, *) buttons get event handlers
			registerEventHandlers();
		} 
	}
	
	/**
	 * Register a default (basic) action for all Buttons
	 * Extend this class and override this method if you desire 
	 * different functionality 
	 */
	protected void registerEventHandlers() {
		// check if we are using phone layout
		ArrayList<Button> currList = new ArrayList<Button>();
		if (copyListButtons != null) {
			currList = copyListButtons;
		} else {
			currList = listButtons;
		}
		
		// set event handlers for all Buttons
		for (int i = 0; i < currList.size(); i++) {
			currList.get(i).setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					System.out.println("Button was clicked: " + ((Button)e.getSource()).getText());
				}
			});
		}
		
		// free up memory
		currList = null;
	}
}

