package view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.event.EventHandler;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;

public class NeuroCanvas extends Canvas {

	private GraphicsContext gc;
	private final double INSIDEIMGSIZE = 20;
	private final double BACKGROUNDIMGSIZE = 28;

	/**
	 * Constructor that sets the graphics context for the canvas and the drawing properties.
	 * @param w is the width of the canvas
	 * @param h is the height of the canvas
	 */
	public NeuroCanvas(int w, int h) {
		super(w, h);
		gc = this.getGraphicsContext2D();
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, w, h);
		gc.setStroke(Color.rgb(0, 0, 0, 0.5));
		gc.setLineCap(StrokeLineCap.ROUND);
		DropShadow ds = new DropShadow();
		ds.setRadius(5);
		ds.setColor(Color.rgb(0, 0, 0, 0.5));
		gc.setEffect(ds);
		gc.setLineWidth(20);
	}

	/**
	 * Draws the user's drawn image on canvas as the user presses, drags and releases the mouse.
	 */
	public void draw() {
		this.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				gc.beginPath();
				gc.moveTo(event.getX(), event.getY());
				gc.stroke();
			}
		});
		this.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				gc.lineTo(event.getX(), event.getY());
				gc.stroke();
				gc.closePath();
				gc.beginPath();
				gc.moveTo(event.getX(), event.getY());
			}
		});
		this.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				gc.lineTo(event.getX(), event.getY());
				gc.stroke();
				gc.closePath();
			}
		});
	}

	/**
	 * Clears the canvas.
	 */
	public void clearScreen() {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

	/**
	 * Takes a snapshot of canvas and returns the image.
	 * @return image drawn on canvas as buffered image.
	 */
	public BufferedImage canvasToBimg() {
		BufferedImage bimg = new BufferedImage((int)this.getWidth(), (int)this.getHeight(), BufferedImage.TYPE_INT_RGB);
		WritableImage wimg = this.takeSnapShot();
		Image img = SwingFXUtils.fromFXImage(wimg, null);
		Graphics graphics = bimg.getGraphics();
		graphics.drawImage(img, 0, 0, null);
		graphics.dispose();
		return bimg;
	}

	/**
	 * Finds out what is the value of blue color in RGB-value.
	 * @param color represents a 8-bit RGB color component packed into integer pixels.
	 * @return value between 0-255 depending on the parameter's value (0 = black, 255 = white).
	 */
	private int getRGBblue(int color) {
		return color & 0xff;
	}

	/**
	 * Searches the borders of the drawn area and crops the picture so that the white non-drawn area is cut off.
	 * @param image represents a buffered image to be cropped
	 * @return a buffered image that has been cropped so that the white borders has been cut off
	 */
	public BufferedImage crop(BufferedImage image) {
		int yMin = yMin(image);
		int xMin = xMin(image);
		int yMax = yMax(image);
		int xMax = xMax(image);
		int width = xMax - xMin + 1;
		int height = yMax - yMin + 1;
		BufferedImage croppedImage = image.getSubimage(xMin, yMin, width, height);
		return croppedImage;
	}

	/**
	 * Scales the image given as parameter to the size defined as insideImgSize and sets it to the center
	 *  of white background sized backGroundImgSize.
	 * @param sourceImg represents the image to be scaled and centered.
	 * @param insideImgSize represents the size of the image that is placed on the white background
	 * @param backGroundImgSize is the size of the background on which the other image is placed
	 * @return certain size of image set on a certain size of white background.
	 */
	public BufferedImage scale(BufferedImage sourceImg, double insideImgSize, double backGroundImgSize) {
		double height;
		double width;
		if (sourceImg.getWidth() <= sourceImg.getHeight()) {
			double scale = insideImgSize / sourceImg.getHeight();
			height = insideImgSize;
			width = sourceImg.getWidth() * scale;
		} else {
			double scale = insideImgSize / sourceImg.getWidth();
			height = sourceImg.getHeight() * scale;
			width = insideImgSize;
		}
		double dx = (backGroundImgSize - width) / 2;
		double dy = (backGroundImgSize - height) / 2;
		BufferedImage newImage = new BufferedImage((int) backGroundImgSize, (int) backGroundImgSize, BufferedImage.TYPE_INT_RGB);
		Graphics graphics = newImage.getGraphics();
		graphics.setColor(java.awt.Color.WHITE);
		graphics.fillRect(0, 0, (int) backGroundImgSize, (int) backGroundImgSize);
		graphics.drawImage(sourceImg, (int) dx, (int) dy, (int) width, (int) height, null);
		graphics.dispose();
		return newImage;
	}

	/**
	 * Draws the given writable image on canvas.
	 * @param wimg represents the image to be drawn on canvas.
	 */
	public void showImage(WritableImage wimg) {
		gc.drawImage(wimg, 0, 0);
	}

	/**
	 * Takes a snapshot of canvas.
	 * @return a writable image of canvas.
	 */
	public WritableImage takeSnapShot() {
		WritableImage wimg = new WritableImage((int) this.getWidth(), (int) this.getHeight());
		this.snapshot(new SnapshotParameters(), wimg);
		return wimg;
	}

	/**
	 * Writes an image from pixelarray.
	 * @param pixelarray must be an array of colorpixels arranged row-wise
	 * @param size is the width of the original image
	 * @return writableimage made of the pixelarray.
	 */
	public WritableImage writePixels(int[] pixelarray, int size) {
		WritableImage wimg = new WritableImage(size, size);
		PixelWriter pixelWriter = wimg.getPixelWriter();
		int k = 0;
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				pixelWriter.setArgb(x, y, pixelarray[k]);
				k++;
			}
		}
		return wimg;
	}

	/**
	 * Finds out the colors of pixels of an image.
	 * @param image represents the image, which pixel colors are aimed to find out.
	 * @return an array of integer pixels in the default RGB color model (TYPE_INT_ARGB)
	 *  and default sRGB color space from the image data. The pixel colors are arranged row-wise.
	 */
	public int[] getImagePixels(BufferedImage image) {
		int [] RGBArray = new int[image.getWidth()*image.getHeight()];
		RGBArray = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
		return RGBArray;
	}

	/**
	 * Picks the value of blue from RGB-values of an array and makes a new array of the blue values.
	 * The RGBvalues are reversed so that 255 represents black and 0 white.
	 * The black colors are lightened to make the image look more like hand-drawn image.
	 * @return array of grey values (0 = white, 255 = black) of image pixels arranged row-wise
	 *
	 */
	public double[] getPixels() {
		int [] tempArray = getImagePixels(scale((crop(canvasToBimg())), INSIDEIMGSIZE, BACKGROUNDIMGSIZE));
		double[] rgbArray = new double[tempArray.length];
		for (int i = 0; i < rgbArray.length; i++) {
			rgbArray[i] = 255 - getRGBblue(tempArray[i]);
			if (rgbArray[i] == 255) {
				rgbArray[i] = rgbArray[i] - 5 * Math.random();
			}
		}
		return rgbArray;
	}

	/**
	 * Finds the minimum drawn pixel of y-axis.
	 * @param image is a buffered image that's drawn area is examined.
	 * @return the minimum drawn pixel of y-axis.
	 */
	private int yMin(BufferedImage image) {
		for (int y = 0; y < (int) image.getHeight(); y++) {
			for (int x = 0; x < (int) image.getWidth(); x++) {
				if (getRGBblue(image.getRGB(x, y)) != 255) {
					return y;
				}
			}
		}
		return 0;
	}

	/**
	 * Finds the minimum drawn pixel of x-axis.
	 * @param image is a buffered image that's drawn area is examined.
	 * @return the minimum drawn pixel of x-axis.
	 */
	private int xMin(BufferedImage image) {
		for (int x = 0; x < (int) image.getWidth(); x++) {
			for (int y = 0; y < (int) image.getHeight(); y++) {
				if (getRGBblue(image.getRGB(x, y)) != 255) {
					return x;
				}
			}
		}
		return 0;
	}

	/**
	 * Finds the maximum drawn pixel of y-axis.
	 * @param image is a buffered image that's drawn area is examined.
	 * @return the maximum drawn pixel of y-axis.
	 */
	private int yMax(BufferedImage image) {
		for (int y = (int) image.getHeight() - 1; y > 0; y--) {
			for (int x = 0; x < (int) image.getWidth(); x++) {
				if (getRGBblue(image.getRGB(x, y)) != 255) {
					return y;
				}
			}
		}
		return 0;
	}

	/**
	 * Finds the maximum drawn pixel of x-axis.
	 * @param image is a buffered image that's drawn area is examined.
	 * @return the maximum drawn pixel of x-axis.
	 */
	private int xMax(BufferedImage image) {
		for (int x = (int) image.getWidth() - 1; x > 0; x--) {
			for (int y = 0; y < (int) image.getWidth(); y++) {
				if (getRGBblue(image.getRGB(x, y)) != 255) {
					return x;
				}
			}
		}
		return 0;
	}



}
