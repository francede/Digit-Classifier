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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


public class Gui extends Application {

	private ControllerImpl controller;
	private NeuroCanvas canvas;
	private HBox hBox;
	private VBox vBox;
	private TextFlow flow;
	private Button clear;
	private Button submit;
	private Button train;
	private Group root;
	private BorderPane borderPane;

	public Gui() {
		this.controller = new ControllerImpl(this);
		this.canvas = new NeuroCanvas(280, 280, Color.WHITE, Color.BLACK, 10);
		this.hBox = new HBox();
		this.vBox = new VBox();
		this.flow = new TextFlow();
		this.clear = new Button("Clear");
		this.submit = new Button("Submit");
		this.train = new Button("Train neural network");
		this.root = new Group();
		this.borderPane = new BorderPane();
	}

	@Override
	public void start(Stage primaryStage) {
		canvas.draw();
		borderPane.setCenter(canvas);
		hBox.getChildren().addAll(clear, submit, train);
		borderPane.setBottom(hBox);
		borderPane.setRight(vBox);
		root.getChildren().add(borderPane);
		primaryStage.setTitle("NeuroCanvas");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
		handleButtons();
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void handleButtons() {
		clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	canvas.clearScreen();
            	flow.getChildren().clear();
            	vBox.getChildren().clear();
            }
        });
		submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	flow.getChildren().clear();
            	vBox.getChildren().clear();
            	flow = showPredictions(controller.makePrediction(canvas.getPixels()));
            	vBox.getChildren().add(flow);
            }
        });
	}

	public TextFlow showPredictions(double[] predictions) {
    	double max = max(predictions);
    	Text bText = new Text();
    	Text text1 = new Text();
    	Text text2 = new Text();
    	String t = "Predictions:\n";
    	for (int i = 0; i < predictions.length; i++) {
    		if (predictions[i] != max) {
    			t = t + i + ": " + predictions[i] + "\n";
    		} else {
    			text1.setText(t);
            	bText.setText(i + ": " + Double.toString(max) + "\n");
            	bText.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
            	t = "";
    		}
    		text2.setText(t);
    	}
    	flow.getChildren().add(text1);
    	flow.getChildren().add(bText);
    	flow.getChildren().add(text2);
    	return flow;
	}

	public double max(double[] arr) {
        int i;
        double max = arr[0];
        for (i = 1; i < arr.length; i++)
            if (arr[i] > max)
                max = arr[i];
        return max;
    }

}


