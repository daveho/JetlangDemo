package edu.ycp.cs365.mandelbrot;

import java.util.Queue;

import org.jetlang.channels.Channel;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;

public class MandelbrotActor {
	private MandelbrotSpec mspec;
	private FiberFactory pool;
	private Fiber fiber;
	private Channel<Row> inbound;
	private Queue<Grid> resultQueue;
	private Grid grid;
	
	public MandelbrotActor(MandelbrotSpec mspec, FiberFactory pool, Queue<Grid> resultQueue) {
		this.mspec = mspec;
		this.pool = pool;
		this.fiber = pool.create();
		this.resultQueue = resultQueue;
		this.grid = new Grid(mspec.getNrows());
		
	}

	public void start(int degreeOfParallelism) {
		// Create RowActors (and Fibers to back them)
		
		// Fibers to which rows actors are allocated
		Fiber[] fibers = new Fiber[degreeOfParallelism];
		
		// Row actors
		RowActor[] rowActors = new RowActor[degreeOfParallelism];
		
		// Channel where results will be sent by row actors
		this.inbound = new MemoryChannel<Row>();
		this.inbound.subscribe(fiber, new Callback<Row>() {
			@Override
			public void onMessage(Row message) {
				handleRow(message);
			}
		});
		
		// Start this actor's Fiber (so it will be able to process incoming messages)
		this.fiber.start();
		
		// Create fibers and row actors
		for (int j = 0; j < degreeOfParallelism; j++) {
			// How many rows will this row actor process?
			// We will assign rows in round-robin fashion.
			int numRows = (mspec.getNrows() + ((degreeOfParallelism - j) - 1)) / degreeOfParallelism;
			fibers[j] = pool.create();
			rowActors[j] = new RowActor(
					fibers[j],
					new MemoryChannel<RowSpec>(),
					this.inbound,
					numRows);
			fibers[j].start();
		}
		
		// Assign rows to actors (round robin, in order to distribute the load evenly)
		double dx = (mspec.getXmax() - mspec.getXmin()) / mspec.getNcols();
		double dy = (mspec.getYmax() - mspec.getYmin()) / mspec.getNrows();
		for (int j = 0; j < mspec.getNrows(); j++) {
			int whichFiber = j % degreeOfParallelism;
			RowSpec rowSpec = new RowSpec(j, mspec.getXmin(), mspec.getYmin() + j*dy, dx, mspec.getNcols());
			rowActors[whichFiber].getInbound().publish(rowSpec);
		}
	}

	private void handleRow(Row message) {
//		System.out.println("receive row " + message.getRow());
		
		grid.addRow(message);
		
		// Have all rows been received?
		if (grid.getRowCount() >= mspec.getNrows()) {
			// We're done!
			grid.sort();
			resultQueue.add(grid);
			fiber.dispose();
		}
	}
}
