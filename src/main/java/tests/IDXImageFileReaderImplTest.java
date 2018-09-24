package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import neumeroverkko.IDXImageFileReader;
import neumeroverkko.IDXImageFileReaderImpl;
import neumeroverkko.InputDataNumberImages;

public class IDXImageFileReaderImplTest {
	
	private static IDXImageFileReader IDXReader;

	@Before public void initTest(){
		IDXReader = new IDXImageFileReaderImpl();
	}
	
	@Test public void getOneImageAsPixels() {
		System.out.println("---Test: get one image as pixels---");
		InputDataNumberImages imageAsPixels;
		imageAsPixels = IDXReader.getSingleImageAsPixels();
		assertEquals("The image double[] length is wrong. ", 784, imageAsPixels.getInput().length, 0.1);
		checkPixelValues(imageAsPixels);

	}
	
	public void checkPixelValues(InputDataNumberImages imageAsPixels) {
		for (double i : imageAsPixels.getInput()) {
			assertTrue("The pixel value is below 0", i >= 0);
			assertTrue("The pixel value is greater than 1", i <= 1);
		}
	}
	
	@Test public void createPNGfiles() {
		IDXReader.createPNGFiles(5);
		IDXReader.createPNGFiles(5);
	}
	
	@Test public void checkLabelValues() {
		System.out.println("---Test: check label values from 5 first images in the training set---");
		ArrayList<InputDataNumberImages> multipleImagesAsPixels;
		int amountOfImages = 5;
		int[] rightLabels = {5,0,4,1,9};
		multipleImagesAsPixels = IDXReader.getMultipleImagesAsPixels(amountOfImages);
		for (int i = 0; i < amountOfImages; i++) {
//			System.out.println("label: " + multipleImagesAsPixels.get(i).getLabel());
			assertEquals("The label was wrong for a number", rightLabels[i], multipleImagesAsPixels.get(i).getLabel());
		}
	}
	
	@Test public void getMultipleImagesAsPixels() {
		System.out.println("---Test: get multiple images as pixels---");
		ArrayList<InputDataNumberImages> multipleImagesAsPixels;
		int amountOfImages = 5;
		multipleImagesAsPixels = IDXReader.getMultipleImagesAsPixels(amountOfImages);
		assertNotNull("The image Arraylist is null", multipleImagesAsPixels);
		for (InputDataNumberImages imageAsPixels : multipleImagesAsPixels) {
			checkPixelValues(imageAsPixels);
		}
	}
	
	@Ignore("Test takes a lot of time")
	@Test public void getAllImagesAsPixels() {
		System.out.println("---Test: get all images as pixels---");
		ArrayList<InputDataNumberImages> allImagesAsPixels;
		allImagesAsPixels = IDXReader.getAllImagesAsPixels();
		assertNotNull("The image Arraylist is null", allImagesAsPixels);
		for (InputDataNumberImages imageAsPixels : allImagesAsPixels) {
			checkPixelValues(imageAsPixels);
		}
	}

}
