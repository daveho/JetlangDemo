package edu.ycp.cs365.mandelbrot;

import java.util.Arrays;
import java.util.List;

/**
 * A list of {@link Row}s that is the overall result of the
 * Mandelbrot set computation.
 * 
 * @author David Hovemeyer
 */
public class Grid {
	//private List<Row> rowList;
	
	private Row[] rowList;
	private int rowCount;

	public Grid(int numRows) {
		//this.rowList = new ArrayList<Row>();
		this.rowList = new Row[numRows];
	}
	
	public int getRowCount() {
		return rowCount;
	}
	
	public void addRow(Row row) {
		//rowList.add(row);
		rowList[row.getRow()] = row;
		rowCount++;
	}
	
	public void sort() {
		/*
		Collections.sort(rowList, new Comparator<Row>() {
			@Override
			public int compare(Row o1, Row o2) {
				if (o1.getY() < o2.getY()) {
					return -1;
				} else if (o1.getY() > o2.getY()) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		*/
	}
	
	public List<Row> getRowList() {
		return Arrays.asList(rowList);
	}
}
