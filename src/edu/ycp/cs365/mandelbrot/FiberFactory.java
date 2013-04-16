package edu.ycp.cs365.mandelbrot;

import org.jetlang.core.Disposable;
import org.jetlang.fibers.Fiber;

public interface FiberFactory extends Disposable {
	public Fiber create();
}
