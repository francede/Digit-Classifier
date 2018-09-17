package neumeroverkko;

import java.util.ArrayList;

/**
 * Reads and returns number images as ImageAsPixelsAndLabel objects from the training set.
 * 
 * The objects contains the labels of the numbers the pixels represent.
 * 
 * The pixels are organized row-wise and are grayscale values (0 = white, 255 = black).
 * The labels are the number drawn in the image (values 0-9).
 * 
 * @author Antti Nieminen
 */ 
public interface IDXImageFileReader {
	
	/**
	 * Returns one ImageAsPixelsAndLabel object constructed from the training set. 
	 */	
	public ImageAsPixelsAndLabel getSingleImageAsPixels();
	
	/**
	 * Returns X amount of ImageAsPixelsAndLabel objects constructed from the training set as a ArrayList.
	 */
	public ArrayList<ImageAsPixelsAndLabel> getMultipleImagesAsPixels(int amountOfImages);
	
	/**
	 * Returns all 60000 number images as ImageAsPixelsAndLabel objects 
	 * from the training set as an ArrayList.
	 */
	public ArrayList<ImageAsPixelsAndLabel> getAllImagesAsPixels();
	

}
