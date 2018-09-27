package view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;


public class NeuroCanvas extends Canvas {

	private GraphicsContext gc;
	private final double insideImgSize = 20;
	private final double backGroundImgSize = 28;

	public NeuroCanvas(int w, int h, Color bgColor, Color sColor, int lineWidth) {
		super(w, h);
		gc = this.getGraphicsContext2D();
		gc.setFill(bgColor);
        gc.fillRect(0, 0, w, h);
        gc.setStroke(sColor);
        gc.setLineWidth(lineWidth);
	}

	public void draw() {
		this.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>(){
		    @Override
		    public void handle(MouseEvent event) {
		        gc.beginPath();
		        gc.moveTo(event.getX(), event.getY());
		        gc.stroke();
		    }
		});
		this.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>(){
		    @Override
		    public void handle(MouseEvent event) {
		        gc.lineTo(event.getX(), event.getY());
		        gc.stroke();
		        gc.closePath();
		        gc.beginPath();
		        gc.moveTo(event.getX(), event.getY());
		    }
		});
		this.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
		    @Override
		    public void handle(MouseEvent event) {
		        gc.lineTo(event.getX(), event.getY());
		        gc.stroke();
		        gc.closePath();
		    }
		});
	}

	public void clearScreen() {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

	public BufferedImage canvasToBimg() {
		WritableImage wimg = new WritableImage((int)this.getWidth(), (int)this.getHeight());
		BufferedImage bimg = new BufferedImage((int)this.getWidth(), (int)this.getHeight(), BufferedImage.TYPE_INT_RGB);
		this.snapshot(new SnapshotParameters(), wimg);
		Image img = SwingFXUtils.fromFXImage(wimg, null);
		Graphics graphics = bimg.getGraphics();
		graphics.drawImage(img, 0, 0, null);
		graphics.dispose();
		return bimg;
	}

	public int getRGBblue(int color ){
		return color & 0xff;
    }

	public BufferedImage crop(BufferedImage image) {
		int yMin = yMin(image);
		int xMin = xMin(image);
		int yMax = yMax(image);
		int xMax = xMax(image);
		int width = xMax-xMin+1;
		int height = yMax-yMin+1;
		BufferedImage croppedImage = image.getSubimage(xMin, yMin, width, height);
		return croppedImage;
	}

	public BufferedImage scale(BufferedImage sourceImg, double insideImgSize, double backGroundImgSize) {
			double height;
			double width;
		if (sourceImg.getWidth() <= sourceImg.getHeight()) {
			double scale = insideImgSize/sourceImg.getHeight();
			height = insideImgSize;
			width = sourceImg.getWidth()*scale;
		} else {
			double scale = insideImgSize/sourceImg.getWidth();
			height = sourceImg.getHeight()*scale;
			width = insideImgSize;
		}
		double dx = (backGroundImgSize - width)/2;
		double dy = (backGroundImgSize - height)/2;
		BufferedImage newImage = new BufferedImage((int)backGroundImgSize, (int)backGroundImgSize, BufferedImage.TYPE_INT_RGB);
		Graphics graphics = newImage.getGraphics();
		graphics.setColor(java.awt.Color.WHITE);
		graphics.fillRect(0, 0, (int)backGroundImgSize, (int)backGroundImgSize);
		graphics.drawImage(sourceImg, (int)dx, (int)dy, (int)width, (int)height, null);
		graphics.dispose();
		return newImage;
	}

//	public WritableImage bimgToWimg(BufferedImage image) {
//		WritableImage wimg = SwingFXUtils.toFXImage(image, null);
//		return wimg;
//	}

//	public void test() {
//		getPixels();
//}
	public void showImage(WritableImage wimg) {
		gc.drawImage(wimg, 0, 0);
	}
	
	public WritableImage takeSnapShot() {
		WritableImage wimg = new WritableImage((int)this.getWidth(), (int)this.getHeight());
		this.snapshot(new SnapshotParameters(), wimg);
		return wimg;
	}
	

	public double[] getPixels() {
		BufferedImage originalImage = canvasToBimg();
		BufferedImage scaledImage = scale(crop(originalImage), insideImgSize, backGroundImgSize);
		int [] tempRgbArray = new int[scaledImage.getWidth()*scaledImage.getHeight()];
		tempRgbArray = scaledImage.getRGB(0, 0, scaledImage.getWidth(), scaledImage.getHeight(), null, 0, scaledImage.getWidth());
		double [] rgbArray = new double[scaledImage.getWidth()*scaledImage.getHeight()];
		for (int i = 0; i < rgbArray.length; i++) {
			rgbArray[i] = getRGBblue(tempRgbArray[i]);
			System.out.println(rgbArray[i]);
		}
		return rgbArray;
	}

	public int yMin(BufferedImage image) {
		for (int y=0; y < (int)image.getHeight(); y++) {
			for (int x=0; x < (int)image.getWidth(); x++) {
				if (getRGBblue(image.getRGB(x, y)) != 255) {
					return y;
				}
			}
		}
		return 0;
	}

	public int xMin(BufferedImage image) {
		for (int x=0; x < (int)image.getWidth(); x++) {
			for (int y=0; y < (int)image.getHeight(); y++) {
				if (getRGBblue(image.getRGB(x, y)) != 255) {
					return x;
				}
			}
		}
		return 0;
	}

	public int yMax(BufferedImage image) {
		for (int y = (int)image.getHeight()-1; y > 0; y--) {
			for (int x=0; x < (int)image.getWidth(); x++) {
				if (getRGBblue(image.getRGB(x, y)) != 255) {
					return y;
				}
			}
		}
		return 0;
	}

	public int xMax(BufferedImage image) {
		for (int x=(int)image.getWidth()-1; x > 0; x--) {
			for (int y=0; y < (int)image.getWidth(); y++) {
				if (getRGBblue(image.getRGB(x, y)) != 255) {
					return x;
				}
			}
		}
		return 0;
	}

}

