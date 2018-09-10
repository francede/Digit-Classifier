package neumeroverkko;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		NeuroCanvas canvas = new NeuroCanvas(280, 280, Color.BLACK, Color.WHITE, 5);
		canvas.draw();
		Group root = new Group();
		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(canvas);
		HBox hBox = new HBox(8);
		setButtons(hBox, canvas);
		borderPane.setBottom(hBox);
		root.getChildren().add(borderPane);
		primaryStage.setTitle("NeuroCanvas");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	private void setButtons(HBox hBox, final NeuroCanvas canvas) {
		Button clear = new Button("Clear");
		Button submit = new Button("Submit");
		clear.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	canvas.clearScreen();
            }
        });
		submit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	
            }
        });
		hBox.getChildren().addAll(clear, submit);		
	}
}
