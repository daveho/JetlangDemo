package edu.ycp.cs365.mandelbrot;

import org.jetlang.channels.Channel;
import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;

public class RowActor {
	private Fiber fiber;
	private Channel<RowSpec> inbound;
	private Channel<Row> outbound;
	private int totalRows;
	private int rowsDone;

	public RowActor(Fiber fiber, Channel<RowSpec> inbound, Channel<Row> outbound, int totalRows) {
		this.fiber = fiber;
		this.inbound = inbound;
		this.outbound = outbound;
		this.totalRows = totalRows;
		this.rowsDone = 0;
		
		inbound.subscribe(fiber, new Callback<RowSpec>() {
			@Override
			public void onMessage(RowSpec message) {
				processRow(message);
			}
		});
	}
	
	public Channel<RowSpec> getInbound() {
		return inbound;
	}

	protected void processRow(RowSpec message) {
//		System.out.println("process row " + message.getY());
		
		Row row = new Row(message.getRow(), message.getY(), message.getNumSamples());
		compute(row, message);
//		System.out.println("Posting row " + message.getRow());
		outbound.publish(row);
		
		// Have we processed all of the rows?
		rowsDone++;
		if (rowsDone >= totalRows) {
			fiber.dispose();
		}
	}
	
	private void compute(Row row, RowSpec rowSpec) {
		for (int i = 0; i < rowSpec.getNumSamples(); i++) {
			Complex c = new Complex(rowSpec.getXmin() + i*rowSpec.getDx(), rowSpec.getY());
			Complex z = new Complex(0.0, 0.0);
			int count = 0;
			while (z.magnitude() < 2.0 && count < 800) {
				z = z.multiply(z).add(c);
				count++;
			}
			row.setIterCount(i, count);
		}
	}

}
