
package com.github.schuettec.cobra2d.engine;

import com.github.schuettec.cobra2d.map.Map;
import com.github.schuettec.cobra2d.renderer.Renderer;

public class WorldUpdater {
	protected int fps;
	protected int actualFPS;
	protected int actualUPS;
	protected boolean running = false;
	protected int noDelaysPerYield = 5;
	protected int maxFrameSkips = 10;

	protected boolean doMapUpdate;
	protected boolean doRender;

	protected double updateTime;
	protected double renderTime;

	protected Map map;
	protected Renderer renderer;

	protected Thread rendererThread;
	protected Runnable rendererProcess = new Runnable() {

		@Override
		public void run() {
			long startTime, stopTime, neededTime, sleepTime, oversleepTime = 0;
			long excess = 0L;
			int noDelays = 0;

			final long period = (1000 / fps) * 1000000L;

			while (running) {

				// some statistics in every loop
				int mapUpdates = 0;

				// Start
				startTime = System.nanoTime();
				// Update world
				if (doMapUpdate) {
					map.update();
					mapUpdates++;
				}
				updateTime = (System.nanoTime() - startTime) / 1000000.0;

				final long renderStart = System.nanoTime();
				if (doRender) {
					renderer.render();
				}
				renderTime = (System.nanoTime() - renderStart) / 1000000.0;

				// Stop
				stopTime = System.nanoTime();
				neededTime = stopTime - startTime;
				sleepTime = (period - neededTime) - oversleepTime;

				if (sleepTime > 0) {
					// some time left in this cycle
					try {
						Thread.sleep(sleepTime / 1000000L); // nano ->
						// ms
					} catch (final InterruptedException ex) {
					}
					oversleepTime = (System.nanoTime() - stopTime) - sleepTime;
				} else {
					// Frame took longer than period
					excess -= sleepTime;
					oversleepTime = 0;
					if (++noDelays >= noDelaysPerYield) {
						Thread.yield(); // give another thread a chance
						// to
						// run
						noDelays = 0;
					}
				}
				/*
				 * If frame animation is taking too long, update the game state without
				 * rendering it, to get the updates/sec nearer to the required FPS.
				 */
				if (doMapUpdate) {
					int skips = 0;
					while ((excess > period) && (skips < WorldUpdater.this.maxFrameSkips)) {
						excess -= period;
						if (!running) {
							break;
						}
						// Update world, test again for loop
						if (doMapUpdate) {
							map.update(); // update
							mapUpdates++;
						}
						// state but don't render
						skips++;
					}
				}
				stopTime = System.nanoTime();
				neededTime = stopTime - startTime;
				final int runningTime = (int) Math.round(neededTime / 1000000.0);
				actualFPS = (int) Math.round(1000.0 / runningTime);
				// Hochrechnung auf eine Sekunde
				final double factor = 1000.0 / runningTime;
				actualUPS = (int) Math.round(mapUpdates * factor);
			}
		}
	};

	public WorldUpdater(int fps, boolean doMapUpdate, boolean doRender, Map map, Renderer renderer) {
		super();
		this.fps = fps;
		this.doMapUpdate = doMapUpdate;
		this.doRender = doRender;
		this.map = map;
		this.renderer = renderer;
	}

	public void start() {
		running = true;
		rendererThread = new Thread(rendererProcess);
		rendererThread.start();
	}

	public void stop() {
		this.running = false;
	}
}
