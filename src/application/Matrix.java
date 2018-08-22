package application;

import java.util.Random;

public class Matrix {
    private int rows;
    private int cols;
    private float[][] data;

    /**
     * Creates a rows x cols size matrix full of zeros
     * @param rows: number of rows in the matrix
     * @param cols: number of columns in the matrix
     */
    public Matrix(int rows,int cols){
    	this.rows = rows;
    	this.cols = cols;
    	this.data = new float[rows][cols];
    	for(int i = 0; i < rows; i++){
    		for(int j = 0; j < cols; j++){
    			this.data[i][j] = 0;
    		}
    	}
    }

    /**
     * Randomizes all elements of the matrix with a number between -1 and 1 according to normal distribution
     */
    public void randomize(){
    	Random r = new Random();
    	for(int i = 0; i < rows; i++){
    		for(int j = 0; j < cols; j++){
    			data[i][j] = (float)r.nextGaussian();
    		}
    	}
    }

    /**
     * Adds a number to every element of the matrix
     * @param number: number to add to every element of the matrix
     */
    public void add(float number){
    	for(int i = 0; i < rows; i++){
    		for(int j = 0; j < cols; j++){
    			data[i][j] += number;
    		}
    	}
    }

    /**
     * Adds elementwise two matrices together
     * @param matrix: matrix to add
     * @return Returns true if the matrices were added successfully. Returns false if the matrices are of different size
     */
    public boolean add(Matrix matrix){
    	if(matrix.rows != this.rows || matrix.cols != this.cols) return false;
    	for(int i = 0; i < rows; i++){
    		for(int j = 0; j < cols; j++){
    			data[i][j] += matrix.data[i][j];
    		}
    	}
    	return true;
    }
}
