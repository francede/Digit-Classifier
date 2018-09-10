package neumeroverkko;

import java.util.ArrayList;

/**
 * Returns number images from the training set as lists of gray scale pixels.
 * The pixels are organized row-wise.
 * 
 * @author Antti Nieminen
 */ 
public interface IDXImageFileReader {
	
	/**
	 * Returns an ImageAsPixel object constructed from the training set. 
	 * 
	 * The object has an attribute 'label', which stores the actual number the pictures represent (0-9) 
	 * and 'pixels' (int[]) of length 784, which are the
	 * grayscale pixel values of the image organized row-wise (0 = white, 255 = black).
	 * 
	 * The original image is 28x28 pixels.
	 */	
	public ImageAsPixels getSingleImageAsPixels();
	
	/**
	 * Returns X amount of number images constructed from the training set as a ArrayList.
	 * 
	 * The last integer of each int[] is the number the pixels represent.
	 */
	public ArrayList<ImageAsPixels> getMultipleImagesAsPixels(int amountOfImages);
	
	/**
	 * Returns all 60000 number images from the training set as a ArrayList of int[].
	 * The last integer of each int[] is the number the pixels represent.
	 */
	public ArrayList<int[]> getAllImagesAsPixels();
	

}
