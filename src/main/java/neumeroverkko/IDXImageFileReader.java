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
	 * Returns a number image from the training set as a int[] of length 785, 
	 * where the first 784 integers are the gray scale pixel values of the image
	 * (0 = white, 255 = black) and the last integer is the number the pixels represent (0-9). 
	 * Pixels are organized row-wise.
	 */	
	public int[] getSingleImageAsPixels();
	
	/**
	 * Returns all 60000 number images from the training set as a ArrayList of int[].
	 * The last integer of each int[] is the number the pixels represent.
	 */
	public ArrayList<int[]> getAllImagesAsPixels();
	

}
