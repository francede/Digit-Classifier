package view;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import controller.ControllerImpl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;


public class Gui extends Application {

	private ControllerImpl controller;
	private NeuroCanvas canvas;
	private Stage startWindow;
	private Stage predictionWindow;
	private Stage correctWindow;
	private Stage trainingWindow;
	private Stage showProgressWindow;
	private ProgressIndicator progressIndicator;
	private ProgressBar progressBar;

	public Gui() {
		this.controller = new ControllerImpl(this);
		this.canvas = new NeuroCanvas(280, 280, Color.WHITE, Color.BLACK, 10);
		this.startWindow = new Stage();
		this.correctWindow = new Stage();
		this.predictionWindow = new Stage();
		this.trainingWindow = new Stage();
		this.showProgressWindow = new Stage();
		this.progressIndicator = new ProgressIndicator(0);
		this.progressBar = new ProgressBar(0);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage = this.startWindow;
		startPage(startWindow);
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void progressPage() {
		Group root = new Group();
		HBox hBox = new HBox();
		hBox.getChildren().addAll(progressBar, progressIndicator);
		Scene scene = new Scene(root, 300, 150);
        showProgressWindow.setScene(scene);
        showProgressWindow.setTitle("Progress Control");
        scene.setRoot(hBox);
        showProgressWindow.show();
	}

	public void showProgress(int i, int amountOfTrainingNumbers) {
		Float value = new Float(i/amountOfTrainingNumbers);
		progressBar.setProgress(value);
        progressIndicator.setProgress(value);
    }

	private void trainingPage() {
		Label labelP = new Label("How many pictures?");
		Slider sliderP = new Slider();
	    sliderP.setMin(0);
	    sliderP.setMax(60000);
	    Label valueP = new Label(Integer.toString((int)sliderP.getValue()));
	    Label labelLR = new Label("Learning rate");
	    Slider sliderLR = new Slider();
	    sliderLR.setMin(0.01);
	    sliderLR.setMax(1);
	    sliderLR.setValue(0.1);
	    Label valueLR = new Label(Double.toString(sliderLR.getValue()));
        Button train = new Button("Train");
        Button back = new Button("Back");
        Button reset = new Button("Reset");

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(5);
        vBox.getChildren().addAll(labelP, sliderP, valueP, labelLR, sliderLR, valueLR);
        vBox.getChildren().addAll(train, reset, back);

        Scene scene = new Scene(vBox, 200, 300);
        scene.setRoot(vBox);
        trainingWindow.setScene(scene);
        trainingWindow.setTitle("Training");
        trainingWindow.show();

        train.setOnAction(new EventHandler<ActionEvent>() {
	         @Override
	         public void handle(ActionEvent event) {
	        	 progressPage();
	        	 int slidervalueP = (int)sliderP.getValue();
                 controller.trainNetwork(slidervalueP);
	        	 double slidervalueLR = sliderLR.getValue();
	        	 //controller.setLearningRate(slidervalueLR);
	         }
	    });

		back.setOnAction(new EventHandler<ActionEvent>() {
			 @Override
	         public void handle(ActionEvent event) {
	        	 trainingWindow.close();
		    }
		});

		reset.setOnAction(new EventHandler<ActionEvent>() {
			 @Override
	         public void handle(ActionEvent event) {
	        	 controller.resetNetwork();
		    }
		});

		sliderP.valueProperty().addListener(new ChangeListener<Number>() {
           @Override
           public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
        	   valueP.setText(Integer.toString(arg2.intValue()));
           }
		});

		sliderLR.valueProperty().addListener(new ChangeListener<Number>() {
	        @Override
	        public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
	        	valueLR.setText(String.format("%.2f", arg2));
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
    	VBox predictions = showPredictions(controller.makePrediction(canvas.getPixels()));
    	VBox vBox = new VBox();
    	borderPane.setLeft(vBox);
		vBox.setPrefSize(100, 280);
		Label title = new Label("Predictions");
		title.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
    	vBox.getChildren().add(title);
    	vBox.getChildren().add(predictions);
    	vBox.setMargin(predictions, new Insets(10,10,10,10));
		vBox.setMargin(title, new Insets(10,10,10,10));
        root.getChildren().add(borderPane);
        predictionWindow.setTitle("Predictions");
        predictionWindow.setX(startWindow.getX() + 300);
        predictionWindow.setY(startWindow.getY());
        predictionWindow.setScene(new Scene(root));
        predictionWindow.show();
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

	private void correctPage() {
		String[] values = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "None"};
    	RadioButton buttons [] = new RadioButton [values.length];
    	ToggleGroup toggleGroup = new ToggleGroup();
		Label label = new Label("The right answer: ");
		Button submitAnswer = new Button("Submit");

        for (int i = 0; i < values.length; i++) {
        	RadioButton button = new RadioButton(values[i]);
        	button.setToggleGroup(toggleGroup);
        	buttons[i] = button;
        }

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10));
        hBox.setSpacing(5);
        hBox.getChildren().add(label);
        hBox.getChildren().addAll(buttons);
        hBox.getChildren().add(submitAnswer);

        submitAnswer.setOnAction(new EventHandler<ActionEvent>() {
	         @Override
	         public void handle(ActionEvent event) {
	        	 RadioButton selectedRadioButton = (RadioButton)toggleGroup.getSelectedToggle();
	        	 int toggleGroupValue = Integer.parseInt(selectedRadioButton.getText());
	        	 System.out.println(toggleGroupValue);
	        	 correctWindow.close();
	        	 predictionWindow.close();
	        	 canvas.clearScreen();
		    }
		});

       Scene scene = new Scene(hBox, 600, 100);
       scene.setRoot(hBox);
       correctWindow.setScene(scene);
       correctWindow.setTitle("The right answer");
       correctWindow.show();
    }


	private VBox showPredictions(double[] predictions) {
		VBox vBox = new VBox();
    	double max = max(predictions);
    	HBox[] hBoxes = new HBox[predictions.length];
    	for (int i = 0; i < predictions.length; i++) {
    		Label label = new Label(Integer.toString(i) + ": ");
    		Text text = new Text(String.format("%.2f", predictions[i]));
    		if (predictions[i] == max) {
    			text.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
    			label.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
    		}
    		HBox hBox = new HBox();
    		hBox.getChildren().addAll(label, text);
    		hBoxes[i] = hBox;
    	}
    	vBox.getChildren().addAll(hBoxes);
    	return vBox;
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


