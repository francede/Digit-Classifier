package model;

public class MatrixException extends RuntimeException{
	public MatrixException(String msg){
		super("Error: Incopatible matrix sizes." + msg);
	}
}
