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
	private Stage predictionWindow;
	private Stage correctWindow;
	private Stage trainingWindow;

	public Gui() {
		this.controller = new ControllerImpl(this);
		this.canvas = new NeuroCanvas(280, 280, Color.WHITE, Color.BLACK, 10);
		this.correctWindow = new Stage();
		this.predictionWindow = new Stage();
		this.trainingWindow = new Stage();
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
	        	 trainingPage();
		    }
		});
	}

	private void handlePredictionsPageButtons(Button right, Button wrong) {
		right.setOnAction(new EventHandler<ActionEvent>() {
	         @Override
	         public void handle(ActionEvent event) {
	        	predictionWindow.close();
	        	canvas.clearScreen();
		    }
		});
		wrong.setOnAction(new EventHandler<ActionEvent>() {
	         @Override
	         public void handle(ActionEvent event) {
	        	 correctPage();
	         }
		});
	}

	private void handleCorrectPageButtons(Button submitAnswer, ToggleGroup toggleGroup) {
		submitAnswer.setOnAction(new EventHandler<ActionEvent>() {
	         @Override
	         public void handle(ActionEvent event) {
	        	 RadioButton selectedRadioButton = (RadioButton)toggleGroup.getSelectedToggle();
	        	 int toogleGroupValue = Integer.parseInt(selectedRadioButton.getText());
	        	 System.out.println(toogleGroupValue);
	        	 correctWindow.close();
	        	 predictionWindow.close();
	        	 canvas.clearScreen();
		    }
		});
	}

	private void handleTrainPageButtons(Button train, Button back, ToggleGroup toggleGroup) {
		train.setOnAction(new EventHandler<ActionEvent>() {
	         @Override
	         public void handle(ActionEvent event) {
	        	 RadioButton selectedRadioButton = (RadioButton)toggleGroup.getSelectedToggle();
	        	 int toggleGroupValue = Integer.parseInt(selectedRadioButton.getText());
	        	 controller.trainNetwork(toggleGroupValue);
	         }
	    });
		back.setOnAction(new EventHandler<ActionEvent>() {
			 @Override
	         public void handle(ActionEvent event) {
	        	 trainingWindow.close();
		    }
		});
	}

	private void trainingPage() {
		ToggleGroup toggleGroup = new ToggleGroup();

		Label label = new Label("How many pictures?");
        RadioButton b100 = new RadioButton("100");
        RadioButton b500 = new RadioButton("500");
        RadioButton b1000 = new RadioButton("1000");
        RadioButton b5000 = new RadioButton("5000");
        RadioButton b10000 = new RadioButton("10000");
        RadioButton b30000 = new RadioButton("30000");
        RadioButton b60000 = new RadioButton("60000");

        b100.setToggleGroup(toggleGroup);
        b500.setToggleGroup(toggleGroup);
        b1000.setToggleGroup(toggleGroup);
        b5000.setToggleGroup(toggleGroup);
        b10000.setToggleGroup(toggleGroup);
        b30000.setToggleGroup(toggleGroup);
        b60000.setToggleGroup(toggleGroup);

        Button train = new Button("Train");
        Button back = new Button("Back");

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(5);
        vBox.getChildren().addAll(label, b100, b500, b1000, b5000, b10000, b30000, b60000);
        vBox.getChildren().addAll(train, back);

        handleTrainPageButtons(train, back, toggleGroup);

        Scene scene = new Scene(vBox, 200, 300);
        scene.setRoot(vBox);
        trainingWindow.setScene(scene);
        trainingWindow.setTitle("Training");
        trainingWindow.show();
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
        predictionWindow.setTitle("Predictions");
        predictionWindow.setScene(new Scene(root));
        predictionWindow.show();
        handlePredictionsPageButtons(right, wrong);
	}

	private void correctPage() {

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

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10));
        hBox.setSpacing(5);
        hBox.getChildren().addAll(label, button0, button1, button2, button3, button4, button5, button6, button7, button8, button9, buttonNone);
        hBox.getChildren().add(submitAnswer);

        handleCorrectPageButtons(submitAnswer, toggleGroup);

        Scene scene = new Scene(hBox, 600, 100);
        scene.setRoot(hBox);
        correctWindow.setScene(scene);
        correctWindow.setTitle("The right answer");
        correctWindow.show();
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


