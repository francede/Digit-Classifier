package controller;

import java.util.ArrayList;

import model.InputDataNumberImages;

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
	public InputDataNumberImages getSingleImageAsPixels();
	
	/**
	 * Returns X amount of ImageAsPixelsAndLabel objects constructed from the training set as a ArrayList.
	 */
	public ArrayList<InputDataNumberImages> getMultipleImagesAsPixels(int amountOfImages);
	
	/**
	 * Returns all 60000 number images as ImageAsPixelsAndLabel objects 
	 * from the training set as an ArrayList.
	 */
	public ArrayList<InputDataNumberImages> getAllImagesAsPixels();
	
	/**
	 * If you want to verify the numbers from the training set. 
	 * Starts extracting from the beginning of the set.
	 */
	public void createPNGFiles(int amount);
	

}
