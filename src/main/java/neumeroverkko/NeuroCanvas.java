package neumeroverkko;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Component;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;
import javafx.collections.FXCollections;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class NeuroCanvas extends Canvas {

	//private BufferedImage image;
	private GraphicsContext gc;
	private BufferedImage bufferedImage;

	public NeuroCanvas(int w, int h, Color bgColor, Color sColor, int lineWidth) {
		super(w, h);
		gc = this.getGraphicsContext2D();
		gc.setFill(bgColor);
        gc.fillRect(0, 0, w, h);
        gc.setStroke(sColor);
        gc.setLineWidth(lineWidth);
        bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	}

	public void draw() {
		this.addEventHandler(MouseEvent.MOUSE_PRESSED,
		        new EventHandler<MouseEvent>(){
		    public void handle(MouseEvent event) {
		        gc.beginPath();
		        gc.moveTo(event.getX(), event.getY());
		        gc.stroke();
		    }
		});
		this.addEventHandler(MouseEvent.MOUSE_DRAGGED,
		        new EventHandler<MouseEvent>(){
		    public void handle(MouseEvent event) {
		        gc.lineTo(event.getX(), event.getY());
		        gc.stroke();
		        gc.closePath();
		        gc.beginPath();
		        gc.moveTo(event.getX(), event.getY());
		    }
		});
		this.addEventHandler(MouseEvent.MOUSE_RELEASED,
		        new EventHandler<MouseEvent>(){
		    public void handle(MouseEvent event) {
		        gc.lineTo(event.getX(), event.getY());
		        gc.stroke();
		        gc.closePath();
		    }
		});
	}

	public void clearScreen() {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

	public int[] getPixels() {
		Graphics2D graphics = this.bufferedImage.createGraphics();


		return null;
	}

}
