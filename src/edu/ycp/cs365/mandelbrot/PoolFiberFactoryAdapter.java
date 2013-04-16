package edu.ycp.cs365.mandelbrot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jetlang.fibers.Fiber;
import org.jetlang.fibers.PoolFiberFactory;

public class PoolFiberFactoryAdapter implements FiberFactory {
	private ExecutorService threadPool;
	private PoolFiberFactory delegate;
	
	public PoolFiberFactoryAdapter(int numThreads) {
		threadPool = Executors.newFixedThreadPool(numThreads);
		delegate = new PoolFiberFactory(threadPool);
	}

	@Override
	public void dispose() {
		delegate.dispose();
		threadPool.shutdown();
	}

	@Override
	public Fiber create() {
		return delegate.create();
	}

}
