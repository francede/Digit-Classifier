package neumeroverkko;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;


public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {		
		NeuroCanvas canvas = new NeuroCanvas(280, 280, Color.WHITE, Color.BLACK, 10);
		ControllerImpl controller = new ControllerImpl(this);
		HBox hBox = new HBox();
		Group root = new Group();
		BorderPane borderPane = new BorderPane();		
		canvas.draw();				
		borderPane.setCenter(canvas);		
		setButtons(hBox, canvas, controller);
		borderPane.setBottom(hBox);
		root.getChildren().add(borderPane);
		primaryStage.setTitle("NeuroCanvas");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void setButtons(HBox hBox, NeuroCanvas canvas, ControllerImpl controller) {
		Button clear = new Button("Clear");
		Button submit = new Button("Submit");
		Button train = new Button("Train neural network");
		clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	canvas.clearScreen();
            }
        });
		submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	controller.makePrediction(canvas.getPixels());
            }
        });
		hBox.getChildren().addAll(clear, submit, train);

	}
}
