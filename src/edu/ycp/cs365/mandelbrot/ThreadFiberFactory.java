package edu.ycp.cs365.mandelbrot;

import org.jetlang.fibers.Fiber;
import org.jetlang.fibers.ThreadFiber;

public class ThreadFiberFactory implements FiberFactory {
	@Override
	public Fiber create() {
		return new ThreadFiber();
	}


	@Override
	public void dispose() {
	}
}
