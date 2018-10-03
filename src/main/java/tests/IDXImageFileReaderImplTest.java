package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import controller.IDXImageFileReader;
import controller.IDXImageFileReaderImpl;
import model.InputData;
import model.InputDataNumberImages;

public class IDXImageFileReaderImplTest {
	
	private static IDXImageFileReader IDXReader;

	@Before public void initTest(){
		IDXReader = new IDXImageFileReaderImpl();
	}
	
	@Test public void getOneImageAsPixels() {
		System.out.println("---Test: get one image as pixels---");
		InputData imageAsPixels;
		imageAsPixels = IDXReader.getSingleImageAsPixels();
		assertEquals("The image double[] length is wrong. ", 784, imageAsPixels.getInput().length, 0.1);
		checkPixelValues(imageAsPixels);

	}
	
	public void checkPixelValues(InputData imageAsPixels) {
		for (double i : imageAsPixels.getInput()) {
			System.out.println(i);
			assertTrue("The pixel value is below 0", i >= 0);
			assertTrue("The pixel value is greater than 1", i <= 1);
		}
	}
	
//	@Ignore("It is not necessary to create PNG files unless you want to validate the pictures visually")
	@Test public void createPNGfiles() {
		IDXReader.createPNGFiles(200);
	}
	
	@Test public void checkLabelValues() {
		System.out.println("---Test: check label values from 5 first images in the training set---");
		ArrayList<InputData> multipleImagesAsPixels;
		int amountOfImages = 5;
		String[] rightLabels = {"5","0","4","1","9"};
		multipleImagesAsPixels = IDXReader.getMultipleImagesAsPixels(amountOfImages);
		for (int i = 0; i < amountOfImages; i++) {
//			System.out.println("label: " + multipleImagesAsPixels.get(i).getLabel());
			assertEquals("The label was wrong for a number", rightLabels[i], multipleImagesAsPixels.get(i).getLabel());
		}
	}
	
	@Test public void getMultipleImagesAsPixels() {
		System.out.println("---Test: get multiple images as pixels---");
		ArrayList<InputData> multipleImagesAsPixels;
		int amountOfImages = 5;
		multipleImagesAsPixels = IDXReader.getMultipleImagesAsPixels(amountOfImages);
		assertNotNull("The image Arraylist is null", multipleImagesAsPixels);
		for (InputData imageAsPixels : multipleImagesAsPixels) {
			checkPixelValues(imageAsPixels);
		}
	}
	
	@Ignore("Test takes a lot of time")
	@Test public void getAllImagesAsPixels() {
		System.out.println("---Test: get all images as pixels---");
		ArrayList<InputData> allImagesAsPixels;
		allImagesAsPixels = IDXReader.getAllImagesAsPixels();
		assertNotNull("The image Arraylist is null", allImagesAsPixels);
		for (InputData imageAsPixels : allImagesAsPixels) {
			checkPixelValues(imageAsPixels);
		}
	}

}
