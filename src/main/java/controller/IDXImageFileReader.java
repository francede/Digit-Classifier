package controller;

import java.util.ArrayList;

import model.InputData;

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
	public InputData getSingleImageAsPixels();
	
	/**
	 * Returns X amount of ImageAsPixelsAndLabel objects constructed from the training set as a ArrayList.
	 */
	public ArrayList<InputData> getMultipleImagesAsPixels(int amountOfImages);
	
	/**
	 * Returns all 60000 number images as ImageAsPixelsAndLabel objects 
	 * from the training set as an ArrayList.
	 */
	public ArrayList<InputData> getAllImagesAsPixels();
	
	/**
	 * If you want to verify the numbers from the training set. 
	 * Starts extracting from the beginning of the set.
	 * @param amount The amount of produced PNG's
	 */
	public void createPNGFiles(int amount);
	
	/**
	 * If you want to verify the numbers from the training set. 
	 * Starts extracting from the beginning of the set.
	 * @param amount the amount of produced PNG's
	 * @param restricOutputToTheseLabels If you want to output only certain numbers
	 */
	public void createPNGFiles(int amount, int... restrictOutputToTheseLabels);
	
	/**
	 * For testing if the neural network can predict correctly the same number it learned from.
	 */
	public InputData getTheFirstNumberFromTrainingFile();
	public ArrayList<InputData> getTheFirstXAmountOfNumbersFromTrainingFile(int amount);
	

}
