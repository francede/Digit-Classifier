package view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import java.awt.image.BufferedImage;
import controller.Controller;
import controller.ControllerImpl;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.WritableImage;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;


public class Gui extends Application {

	private Controller controller;
	private NeuroCanvas canvas;
	private Stage startWindow;
	private Stage predictionWindow;
	private Stage correctWindow;
	private Stage trainingWindow;
	private Stage progressWindow;

	public Gui() {
		this.controller = new ControllerImpl(this);
		this.canvas = new NeuroCanvas(280, 280);
		this.startWindow = new Stage();
		this.correctWindow = new Stage();
		this.predictionWindow = new Stage();
		this.trainingWindow = new Stage();
		this.progressWindow = new Stage();
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage = this.startWindow;
		startPage(startWindow);
	}

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Creates window for the canvas that user can draw on.
	 * Creates buttons for submitting the answer, clearing the canvas, loading the data,
	 * training, reseting and saving the network and event handlers of the buttons.
	 * @param primaryStage is the stage that opens when the program starts.
	 */
	private void startPage(Stage primaryStage) {
		Group root  = new Group();
		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(canvas);
		Button clear = new Button("Clear screen");
		Button submit = new Button("Submit");
		Button train = new Button("Train network");
		Button load = new Button("Load from database");
		Button save = new Button("Save to database");
		Button reset = new Button("Reset network");
		Button[] buttons = {clear, submit, train, load, save, reset};
		VBox vBox = new VBox();
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setPrefWidth(canvas.getWidth()/2);
		}
		HBox hBox1 = new HBox();
		HBox hBox2 = new HBox();
		HBox hBox3 = new HBox();
		hBox1.getChildren().addAll(clear, submit);
		hBox2.getChildren().addAll(load, save);
		hBox3.getChildren().addAll(reset, train);
		vBox.getChildren().addAll(hBox1, hBox2, hBox3);
		borderPane.setBottom(vBox);
		root.getChildren().add(borderPane);
		canvas.draw();
		clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	canvas.clearScreen();
            }
        });
		reset.setOnAction(new EventHandler<ActionEvent>() {
			 @Override
	         public void handle(ActionEvent event) {
	        	 controller.resetNetwork();
		    }
		});
		submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	predictionsPage();
            	canvas.clearScreen();
            }
        });
		train.setOnAction(new EventHandler<ActionEvent>() {
	         @Override
	         public void handle(ActionEvent event) {
	        	 trainingPage();
		    }
		});
		load.setOnAction(new EventHandler<ActionEvent>() {
	         @Override
	         public void handle(ActionEvent event) {
	        	 controller.loadNetwork();
		    }
		});
		save.setOnAction(new EventHandler<ActionEvent>() {
	         @Override
	         public void handle(ActionEvent event) {
	        	 controller.saveNetwork();
		    }
		});
		primaryStage.setTitle("NeuroCanvas");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}

	/**
	 * Creates a window for a progressbar that shows the progress of the task given as parameter.
	 * Starts the task and adds a button to cancel the started progress.
	 * @param task represents the task that progressbar listens.
	 */
	private void progressPage(Task task) {
		Group root = new Group();
		Scene scene = new Scene(root);

		Label title = new Label("Learning...");

		ProgressBar progressBar = new ProgressBar(0);
		progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(task.progressProperty());
        progressBar.setPrefWidth(250);
        Label value = new Label(String.format("%.0f%%", progressBar.getProgress()*100));

        HBox hBox = new HBox();
		hBox.getChildren().addAll(progressBar, value);

		Button cancelButton = new Button("Cancel");

		VBox vBox = new VBox();
		vBox.setPadding(new Insets(10));
        vBox.setSpacing(5);
		vBox.getChildren().addAll(title, hBox, cancelButton);

		progressBar.progressProperty().addListener(new ChangeListener<Number>() {
	        @Override
	        public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
	        	value.setText(String.format("%.0f%%", (arg2.doubleValue()*100)));
	        	if (arg2.doubleValue() == 1) {
	        		progressWindow.close();
	        		trainingWindow.close();
	        	}
	        }
	    });
        new Thread(task).start();
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                task.cancel(true);
                progressBar.progressProperty().unbind();
                progressWindow.close();
            }
        });
        scene.setRoot(vBox);
        progressWindow.setScene(scene);
        progressWindow.setTitle("Progress Control");
        progressWindow.show();
	}

	/**
	 * Creates a window that contains a slider to set the amount of images to be read,
	 *  slider to set the learning rate and button to start training.
	 */
	private void trainingPage() {
		Label labelP = new Label("How many images?");
		Slider sliderP = new Slider();
	    sliderP.setMin(1);
	    sliderP.setMax(60000);
	    Label valueP = new Label(Integer.toString((int)sliderP.getValue()));
	    Label labelLR = new Label("Learning rate");
	    Slider sliderLR = new Slider();
	    sliderLR.setMin(0.01);
	    sliderLR.setMax(1);
	    sliderLR.setValue(0.1);
	    Label valueLR = new Label(Double.toString(sliderLR.getValue()));
        Button train = new Button("Start training");
        //Button back = new Button("Back");

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(5);
        vBox.getChildren().addAll(labelP, sliderP, valueP, labelLR, sliderLR, valueLR);
        vBox.getChildren().addAll(train);

        Scene scene = new Scene(vBox);
        scene.setRoot(vBox);
        trainingWindow.setScene(scene);
        trainingWindow.setTitle("Training");
        trainingWindow.show();

        train.setOnAction(new EventHandler<ActionEvent>() {
	         @Override
	         public void handle(ActionEvent event) {
	        	 int slidervalueP = (int)sliderP.getValue();
	        	 progressPage(controller.trainNetwork(slidervalueP));
	        	 double slidervalueLR = sliderLR.getValue();
	        	 //controller.setLearningRate(slidervalueLR);
	        	 trainingWindow.close();
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

	/**
	 * Creates a window on which the predictions are shown.
	 * Adds buttons "right" and "wrong" to let the user tell the system whether its guess was right or wrong.
	 * Shows the drawn image to be delivered to controller.
	 */
	private void predictionsPage() {
		Button right = new Button("Right");
		Button wrong = new Button("Wrong");
		HBox buttonPane = new HBox();
		buttonPane.getChildren().addAll(right, wrong);
		Group root = new Group();
		NeuroCanvas imageCanvas = new NeuroCanvas(280, 280);
		imageCanvas.showImage(canvas.takeSnapShot());
		//BufferedImage scaledImage = canvas.scale((canvas.crop(canvas.canvasToBimg())), 20, 28);
		//imageCanvas.showImage(canvas.writePixels((canvas.getImagePixels(scaledImage)), 28));
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
		    }
		});
		wrong.setOnAction(new EventHandler<ActionEvent>() {
	         @Override
	         public void handle(ActionEvent event) {
	        	 correctPage();
	        	 predictionWindow.close();
	         }
		});
	}

	/**
	 * Creates a window, on which the user can tell the right number by radio buttons.
	 */
	private void correctPage() {
		Group root  = new Group();
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

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(5);
        vBox.getChildren().add(label);
        vBox.getChildren().addAll(buttons);
        vBox.getChildren().add(submitAnswer);

        root.getChildren().add(vBox);

        submitAnswer.setOnAction(new EventHandler<ActionEvent>() {
	         @Override
	         public void handle(ActionEvent event) {
	        	 RadioButton selectedRadioButton = (RadioButton)toggleGroup.getSelectedToggle();
	        	 int toggleGroupValue = Integer.parseInt(selectedRadioButton.getText());
	        	 System.out.println(toggleGroupValue);
	        	 correctWindow.close();
	        	 //predictionWindow.close();
		    }
		});

       correctWindow.setScene(new Scene(root));
       correctWindow.setTitle("The right answer");
       correctWindow.show();
    }

	/**
	 * Shows the predictions that it gets as a parameter in vbox.
	 * @param predictions is a double array that includes the prediction for each number(0-9).
	 * @return vbox that includes the predictions as text nodes.
	 */
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

	/**
	 * Finds the max value of an array.
	 * @param arr is the array, which max value is searched
	 * @return max value of an array
	 */
	private double max(double[] arr) {
        int i;
        double max = arr[0];
        for (i = 1; i < arr.length; i++)
            if (arr[i] > max)
                max = arr[i];
        return max;
    }

}


