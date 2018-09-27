package view;

import controller.ControllerImpl;
import javafx.stage.Window;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;


public class Gui extends Application {

	private ControllerImpl controller;
	private NeuroCanvas canvas;
	private Stage predictionPWindow;
	private Stage rButtonWindow;

	public Gui() {
		this.controller = new ControllerImpl(this);
		this.canvas = new NeuroCanvas(280, 280, Color.WHITE, Color.BLACK, 10);
		this.rButtonWindow = new Stage();
		this.predictionPWindow = new Stage();
	}

	@Override
	public void start(Stage primaryStage) {
		startPage(primaryStage);
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void handleStartPageButtons(Button clear, Button submit, Button train) {
		clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	canvas.clearScreen();
            }
        });
		submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	predictionsPage();
            }
        });
		train.setOnAction(new EventHandler<ActionEvent>() {
	         @Override
	         public void handle(ActionEvent event) {

		    }
		});
	}

	private void handlePredictionsPageButtons(Button right, Button wrong) {
		right.setOnAction(new EventHandler<ActionEvent>() {
	         @Override
	         public void handle(ActionEvent event) {
	        	predictionPWindow.close();
	        	canvas.clearScreen();
		    }
		});
		wrong.setOnAction(new EventHandler<ActionEvent>() {
	         @Override
	         public void handle(ActionEvent event) {
	        	 rButtonPage();
	         }
		});
	}

	private void handleRBPageButtons(Button submitAnswer, ToggleGroup toggleGroup) {
		submitAnswer.setOnAction(new EventHandler<ActionEvent>() {
	         @Override
	         public void handle(ActionEvent event) {
	        	 RadioButton selectedRadioButton = (RadioButton)toggleGroup.getSelectedToggle();
	        	 int toogleGroupValue = Integer.parseInt(selectedRadioButton.getText());
	        	 System.out.println(toogleGroupValue);
	        	 rButtonWindow.close();
	        	 predictionPWindow.close();
	        	 canvas.clearScreen();
		    }
		});
	}

	private void startPage(Stage primaryStage) {
		Group root  = new Group();
		BorderPane borderPane = new BorderPane();
		HBox buttonPane = new HBox();
		borderPane.setCenter(canvas);
		Button clear = new Button("Clear");
		Button submit = new Button("Submit");
		Button train = new Button("Train neural network");
		buttonPane.getChildren().addAll(clear, submit, train);
		borderPane.setBottom(buttonPane);
		root.getChildren().add(borderPane);
		canvas.draw();
		handleStartPageButtons(clear, submit, train);
		primaryStage.setTitle("NeuroCanvas");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}

	private void predictionsPage() {
		Button right = new Button("Right");
		Button wrong = new Button("Wrong");
		HBox buttonPane = new HBox();
		buttonPane.getChildren().addAll(right, wrong);
		Group root = new Group();
		NeuroCanvas imageCanvas = new NeuroCanvas(280, 280, Color.WHITE, Color.BLACK, 10);
		imageCanvas.showImage(canvas.takeSnapShot());
    	BorderPane borderPane = new BorderPane();
    	borderPane.setCenter(imageCanvas);
        borderPane.setBottom(buttonPane);
        TextFlow textFlow = new TextFlow();
    	textFlow = showPredictions(controller.makePrediction(canvas.getPixels()), textFlow);
    	VBox vBox = new VBox();
    	borderPane.setLeft(vBox);
		vBox.setPrefSize(100, 280);
		Label title = new Label("Predictions");
		title.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
    	vBox.getChildren().add(title);
    	vBox.getChildren().add(textFlow);
    	vBox.setMargin(textFlow, new Insets(10,10,10,10));
		vBox.setMargin(title, new Insets(10,10,10,10));
        root.getChildren().add(borderPane);
        predictionPWindow.setTitle("Second Stage");
        predictionPWindow.setScene(new Scene(root));
        predictionPWindow.show();
        handlePredictionsPageButtons(right, wrong);
	}

	private void rButtonPage() {

		ToggleGroup toggleGroup = new ToggleGroup();
		Label label = new Label("The right answer: ");

        RadioButton button0 = new RadioButton("0");
        RadioButton button1 = new RadioButton("1");
        RadioButton button2 = new RadioButton("2");
        RadioButton button3 = new RadioButton("3");
        RadioButton button4 = new RadioButton("4");
        RadioButton button5 = new RadioButton("5");
        RadioButton button6 = new RadioButton("6");
        RadioButton button7 = new RadioButton("7");
        RadioButton button8 = new RadioButton("8");
        RadioButton button9 = new RadioButton("9");
        RadioButton buttonNone = new RadioButton("None");

        button0.setToggleGroup(toggleGroup);
        button1.setToggleGroup(toggleGroup);
        button2.setToggleGroup(toggleGroup);
        button3.setToggleGroup(toggleGroup);
        button4.setToggleGroup(toggleGroup);
        button5.setToggleGroup(toggleGroup);
        button6.setToggleGroup(toggleGroup);
        button7.setToggleGroup(toggleGroup);
        button8.setToggleGroup(toggleGroup);
        button9.setToggleGroup(toggleGroup);
        buttonNone.setToggleGroup(toggleGroup);

        Button submitAnswer = new Button("Submit");

        HBox rButtonHBox = new HBox();
        rButtonHBox.setPadding(new Insets(10));
        rButtonHBox.setSpacing(5);
        rButtonHBox.getChildren().addAll(label, button0, button1, button2, button3, button4, button5, button6, button7, button8, button9, buttonNone);
        rButtonHBox.getChildren().add(submitAnswer);

        handleRBPageButtons(submitAnswer, toggleGroup);

        Scene rButtonScene = new Scene(rButtonHBox, 600, 100);
        rButtonScene.setRoot(rButtonHBox);
        rButtonWindow.setScene(rButtonScene);
        rButtonWindow.setTitle("The right answer");
        rButtonWindow.show();
	}

	private TextFlow showPredictions(double[] predictions, TextFlow flow) {
    	double max = max(predictions);
    	Text bText = new Text();
    	Text text1 = new Text();
    	Text text2 = new Text();
    	String t = "";
    	for (int i = 0; i < predictions.length; i++) {
    		if (predictions[i] != max) {
    			t = t + i + ": " + predictions[i] + "\n";
    		} else {
    			text1.setText(t);
            	bText.setText(i + ": " + Double.toString(max) + "\n");
            	bText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
            	t = "";
    		}
    		text2.setText(t);
    	}
    	flow.getChildren().add(text1);
    	flow.getChildren().add(bText);
    	flow.getChildren().add(text2);
    	return flow;
	}

	private double max(double[] arr) {
        int i;
        double max = arr[0];
        for (i = 1; i < arr.length; i++)
            if (arr[i] > max)
                max = arr[i];
        return max;
    }

}


