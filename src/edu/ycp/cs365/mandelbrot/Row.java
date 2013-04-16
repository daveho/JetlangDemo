package edu.ycp.cs365.mandelbrot;

/**
 * Iteration counts for one row of the Mandelbrot set computation.
 * 
 * @author David Hovemeyer
 */
public class Row {
	private int row;
	private double y;
	private int[] iterCounts;
	
	public Row(int row, double y, int numSamples) {
		this.row = row;
		this.y = y;
		this.iterCounts = new int[numSamples];
	}
	
	public int getRow() {
		return row;
	}
	
	public double getY() {
		return y;
	}
	
	public void setIterCount(int index, int value) {
		iterCounts[index] = value;
	}
	
	public int[] getIterCounts() {
		return iterCounts;
	}
}
