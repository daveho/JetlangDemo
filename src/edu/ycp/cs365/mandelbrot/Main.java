package edu.ycp.cs365.mandelbrot;

import java.util.concurrent.LinkedBlockingQueue;

public class Main {
	public static final int WIDTH = Integer.getInteger("width", 20);
	public static final int HEIGHT = Integer.getInteger("height", 20);

	public static final int NUM_CORES = Integer.getInteger("numCores", 2);
	public static final boolean PRINT = Boolean.parseBoolean(System.getProperty("print", "true"));
	
	public static final boolean USE_THREAD_POOL = Boolean.parseBoolean(System.getProperty("threadPool", "true"));

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Using " + NUM_CORES + " cores");

		final MandelbrotSpec mspec = new MandelbrotSpec(-2.0, -2.0, 2.0, 2.0, WIDTH, HEIGHT);

		long begin = System.currentTimeMillis();
		LinkedBlockingQueue<Grid> q = new LinkedBlockingQueue<Grid>();
		
		FiberFactory pool;
		if (USE_THREAD_POOL) {
			System.out.println("Using thread pool");
			pool = new PoolFiberFactoryAdapter(NUM_CORES);
		} else {
			System.out.println("Using dedicated threads");
			pool = new ThreadFiberFactory();
		}
		
		MandelbrotActor mandelbrotActor = new MandelbrotActor(mspec, pool, q);
		
		mandelbrotActor.start(NUM_CORES);
		
		Grid result = q.take();
		long end = System.currentTimeMillis();

		System.out.println("Computation completed in " + (end - begin) + " ms");

		pool.dispose();
		
		if (PRINT) {
			for (Row r : result.getRowList()) {
				int[] iterCounts = r.getIterCounts();
				for (int i = 0; i < iterCounts.length; i++) {
					if (i > 0) {
						System.out.print(",");
					}
					System.out.print(iterCounts[i]);
				}
				System.out.println();
			}
		}
		
		System.out.println("All done!");
	}
}
