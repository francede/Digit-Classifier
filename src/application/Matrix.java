package application;

import java.util.Random;

/**
 * A slightly rudimentary matrix library with essential matrix operations including but not limited to:
 * dot multiplication, transposing, elementwise and matrix to matrix operations(+-/*)
 * @author Francesco
 *
 */
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

    @Override
    public String toString(){
    	String str = "";
    	for(int i = 0; i < this.rows; i++){
    		for(int j = 0; j < this.cols; j++){
    			String number = String.format("%.2f",this.data[i][j]);
    			str += number + "\t";
    		}
    		str += "\n";
    	}
    	return str;
    }
//STATIC MATRIX OPERATIONS

    /**
     * Creates a dot product of two matrices
     * @param a: First matrix
     * @param b: Second matrix
     * Note that the order in which the param matrices are given matters:
     * a's columns need to match with b's rows
     * @return Returns a matrix which is the dot product of the two matrices.
     * Returns false if a's columns and b's rows don't match
     */
    public static Matrix dotProduct(Matrix a, Matrix b){
    	if(a.cols != b.rows) return null;
    	Matrix result = new Matrix(a.rows, b.cols);

    	for(int i = 0; i < result.rows; i++){
    		for(int j = 0; j < result.cols; j++){
    			float element_sum = 0;
    			for(int k = 0; k < a.cols; k++){
    				element_sum += a.data[i][k] * b.data[k][j];
    			}
    			result.data[i][j] = element_sum;
    		}
    	}
    	return result;
    }

    /**
     * Transposes a matrix
     * @param m: Matrix to transpose
     * @return Returns the transposed matrix
     */
    public static Matrix transpose(Matrix m){
    	Matrix result = new Matrix(m.cols, m.rows);
    	for(int i = 0; i < result.rows; i++){
    		for(int j = 0; j < result.cols; j++){
    			result.data[i][j] = m.data[j][i];
    		}
    	}
    	return result;
    }

    /**
     * Tranforms an array into a one dimensional matrix
     * @param array: Array to tranform into a matrix
     * @return Returns a one dimensional matrix
     */
    public static Matrix arrayToMatrix(float[] array){
    	Matrix result = new Matrix(array.length, 1);
    	for(int i = 0; i < array.length; i++){
    		result.data[i][0] = array[i];
    	}
    	return result;
    }

    /**
     * Creates a clone of the given matrix without modifying the original
     * @param m: Matrix to clone
     * @return Returns the clone matrix
     */
    public static Matrix clone(Matrix m){
    	Matrix result = new Matrix(m.rows, m.cols);
    	for(int i = 0; i < m.rows; i++){
    		for(int j = 0; j < m.cols; j++){
    			result.data[i][j] = m.data[i][j];
    		}
    	}
    	return result;
    }

//MATRIX ELEMENTWISE AND NUMERIC OPERATIONS

    /**
     * Adds a number to every element of the matrix
     * @param number: number to add to every element of the matrix
     */
    public boolean add(float number){
    	for(int i = 0; i < rows; i++){
    		for(int j = 0; j < cols; j++){
    			data[i][j] += number;
    		}
    	}
    	return true;
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

	/**
	 * Subtracts a number from every element of the matrix
	 * @param number: number to subtract from every element of the matrix
	 */
	public boolean sub(float number){
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++){
				data[i][j] -= number;
			}
		}
		return true;
	}

	/**
	 * Subtracts element wise two matrices together
	 * @param matrix: matrix to subtract
	 * @return Returns true if the matrices were subtracted successfully. Returns false if the matrices are of different size
	 */
	public boolean sub(Matrix matrix){
		if(matrix.rows != this.rows || matrix.cols != this.cols) return false;
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++){
				data[i][j] -= matrix.data[i][j];
			}
		}
		return true;
	}

    /**
     * Divides every element of the matrix by a number
     * @param number: number to divide every element of the matrix with
     * @return Returns false if number is 0
     */
    public boolean divide(float number){
    	if(number == 0) return false;
    	for(int i = 0; i < rows; i++){
    		for(int j = 0; j < cols; j++){
    			data[i][j] /= number;
    		}
    	}
    	return true;
    }

    /**
     * Divides elementwise numbers of this matrix with given matrix
     * @param matrix: matrix to divide with
     * @return Returns true if the matrices were divided successfully.
     * Returns false if the matrices are of different size or if the divider matrix contains zeros
     */
    public boolean divide(Matrix matrix){
    	if(matrix.rows != this.rows || matrix.cols != this.cols) return false;
    	for(int i = 0; i < rows; i++){
    		for(int j = 0; j < cols; j++){
    			if(matrix.data[i][j] == 0) return false;
    			data[i][j] += matrix.data[i][j];
    		}
    	}
    	return true;
    }

    /**
     * Multiplies every element of the matrix with a number
     * @param number: number to multiply every element of the matrix with
     */
    public boolean multiply(float number){
    	for(int i = 0; i < rows; i++){
    		for(int j = 0; j < cols; j++){
    			data[i][j] *= number;
    		}
    	}
    	return true;
    }

    /**
     * Adds elementwise two matrices together
     * @param matrix: matrix to add
     * @return Returns true if the matrices were added successfully. Returns false if the matrices are of different size
     */
    public boolean multiply(Matrix matrix){
    	if(matrix.rows != this.rows || matrix.cols != this.cols) return false;
    	for(int i = 0; i < rows; i++){
    		for(int j = 0; j < cols; j++){
    			data[i][j] *= matrix.data[i][j];
    		}
    	}
    	return true;
    }

    /**
     * Maps a function to every element of the matrix. In the event of a failed mapping,
     * restores to the original matrix
     * @param f: FloatFunction to map to each element(see lambda functions)
     * @return Returns true if successful mapping and false upon failed mapping
     */
    public boolean map(FloatOperation f){
    	Matrix backup = Matrix.clone(this);
    	try{
	    	for(int i = 0; i < this.rows; i++){
	    		for(int j = 0; j < this.cols; j++){
	    			this.data[i][j] = f.operation(this.data[i][j]);
	    		}
	    	}
    	}catch(Exception e){
    		this.data = backup.data;
    		return false;
    	}
    	return true;
    }

    /**
     * A functional interface for lambda functions of one parameter(float) and of return type float
     * @author Francesco
     */
    public interface FloatOperation{
    	float operation(float x);
    }
}
