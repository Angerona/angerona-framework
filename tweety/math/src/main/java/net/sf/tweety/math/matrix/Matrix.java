package net.sf.tweety.math.matrix;

import net.sf.tweety.math.term.*;

/**
 * This class models a matrix of terms.
 * 
 * @author Matthias Thimm
 */
public class Matrix {

	/**
	 * The entries of the matrix
	 */
	private Term[][] entries;
	
	/**
	 * Creates a new matrix of the given dimension.
	 * @param n the x dimension of the matrix.
	 * @param m the y dimension of the matrix.
	 */
	public Matrix(int n, int m){
		this.entries = new Term[n][m];
	}
	
	/**
	 * Returns the entry with the given coordinates.
	 * @param x the x coordinate of the entry.
	 * @param y the y coordinate of the entry.
	 * @return the entry at the given coordinates.
	 */
	public Term getEntry(int x, int y){
		return this.entries[x][y];
	}
	
	/**
	 * Sets the entry at the given coordinates.
	 * @param x the x coordinate of the entry.
	 * @param y the y coordinate of the entry.
	 * @param entry the entry to be set.
	 */
	public void setEntry(int x, int y, Term entry){
		this.entries[x][y] = entry;
	}
	
	/**
	 * Returns the x dimension of this matrix.
	 * @return the x dimension of this matrix.
	 */
	public int getXDimension(){
		return this.entries.length;
	}
	
	/**
	 * Returns the y dimension of this matrix.
	 * @return the y dimension of this matrix.
	 */
	public int getYDimension(){
		return this.entries[0].length;
	}
	
	/**
	 * Multiply this matrix with the given one.
	 * @param other a matrix
	 * @return the product of the two matrices.
	 * @throw IllegalArgumentException if the x dimension of this matrix
	 * 	does not equal the y dimension of the other matrix
	 */
	public Matrix mult(Matrix other) throws IllegalArgumentException{
		if(this.getXDimension() != other.getYDimension())
			throw new IllegalArgumentException("Wrong dimensions of matrices.");
		Matrix result = new Matrix(other.getXDimension(),this.getYDimension());
		for(int i = 0; i < other.getXDimension(); i++)
			for(int j = 0; j < this.getYDimension(); j++){
				Term entry = new FloatConstant(0);
				for(int k = 0; k < this.getXDimension(); k++)
					entry = entry.add(this.getEntry(k, j).mult(other.getEntry(i, k)));				
				result.setEntry(i, j, entry);
			}
		return result;
	}
	
	/**
	 * Multiply this matrix with the given scalar
	 * (every entry is multiplied)
	 * @param scalar a term
	 * @return a new matrix.
	 */
	public Matrix mult(Term scalar){
		Matrix result = new Matrix(this.getXDimension(),this.getYDimension());
		for(int i = 0; i < this.getXDimension(); i++)
			for(int j = 0; j < this.getYDimension(); j++)
				result.setEntry(i, j, this.getEntry(i, j).mult(scalar));
		return result;
	}
	
	/**
	 * Multiply this matrix with the given scalar
	 * (every entry is multiplied)
	 * @param scalar a double
	 * @return a new matrix.
	 */
	public Matrix mult(double scalar){
		return this.mult(new FloatConstant(scalar));
	}
	
	/**
	 * Transposes this matrix, i.e. switches x and y dimension.
	 * @return the transposed matrix.
	 */
	public Matrix transpose(){
		Matrix result = new Matrix(this.getYDimension(), this.getXDimension());
		for(int i = 0; i < this.getXDimension(); i++)
			for(int j = 0; j < this.getYDimension(); j++)
				result.setEntry(j, i, this.getEntry(i, j));
		return result;
	}
	
	/**
	 * Adds the given matrix to this one and returns the result.
	 * @param other a matrix
	 * @return the sum of the two matrices.
	 * @throw IllegalArgumentException if the dimensions of the matrices
	 * 	do not correspond.
	 */
	public Matrix add(Matrix other) throws IllegalArgumentException{
		if(this.getXDimension() != other.getXDimension() || this.getYDimension() != other.getYDimension())
			throw new IllegalArgumentException("Wrong dimensions of matrices.");
		Matrix result = new Matrix(this.getXDimension(),this.getYDimension());
		for(int i = 0; i < this.getXDimension(); i++)
			for(int j = 0; j < this.getYDimension(); j++)
				result.setEntry(i, j, this.getEntry(i, j).add(other.getEntry(i, j)));
		return result;
	}
	
	/**
	 * Makes a subtraction of the given matrix from this one and returns the result.
	 * @param other a matrix
	 * @return the subtraction of the two matrices.
	 * @throw IllegalArgumentException if the dimensions of the matrices
	 * 	do not correspond.
	 */
	public Matrix minus(Matrix other)throws IllegalArgumentException{
		return this.add(other.mult(-1));
	}
	
	/**
	 * Simplifies every entry.
	 * @return the simplified matrix.
	 */
	public Matrix simplify(){
		Matrix result = new Matrix(this.getXDimension(),this.getYDimension());
		for(int i = 0; i < this.getXDimension(); i++)
			for(int j = 0; j < this.getYDimension(); j++)
				result.setEntry(i, j, this.getEntry(i, j).simplify());
		return result;
	}
	
	/**
	 * Returns the distance of this matrix to the zero matrix.
	 * @return the distance of this matrix to the zero matrix.
	 */
	public double distanceToZero(){
		double result = 0;
		for(int i = 0; i < this.getXDimension(); i++)
			for(int j = 0; j< this.getYDimension(); j++)
				result += Math.abs(this.getEntry(i, j).doubleValue());
		return result;
	}
	
	/**
	 * Returns the identity matrix of the given dimension.
	 * @param dim the dimension.
	 * @return the identity matrix of the given dimension.
	 */
	public static Matrix getIdentityMatrix(int dim){
		Matrix result = new Matrix(dim,dim);
		for(int i = 0; i < dim; i++)
			for(int j = 0; j < dim; j++)
				result.setEntry(i, j, new IntegerConstant((i==j)?(1):(0)));
		return result;
	}
	
	/**
	 * Checks whether each entry in this matrix describes a finite number.
	 * @return "true" iff this matrix is finite.
	 */
	public boolean isFinite(){
		for(int i = 0; i < this.getXDimension(); i++)
			for(int j = 0; j < this.getYDimension(); j++)
				if(Double.isInfinite(this.getEntry(i, j).doubleValue()) || Double.isNaN(this.getEntry(i, j).doubleValue()))
					return false;
		return true;		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String s = "";
		for(int j = 0; j < this.getYDimension(); j++){
			s += "[ ";
			for(int i = 0; i < this.getXDimension(); i++)
				s += this.getEntry(i, j).toString() + " ";
			s += "]\n";
		}
		return s;
	}
}
