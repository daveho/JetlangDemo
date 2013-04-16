#! /bin/bash

if [ -z "$THREAD_POOL" ]; then
	THREAD_POOL=false
fi

ncores=1
if [ "$1" != "" ]; then
	ncores="$1"
fi

java -DthreadPool=$THREAD_POOL \
	-Dprint=false -Dwidth=2000 -Dheight=2000 -DnumCores=$ncores \
	-classpath bin:lib/jetlang-0.2.10.jar edu.ycp.cs365.mandelbrot.Main
