package com.github.schuettec.cobra2d.world;

/**
 * Used to actively update the game world if no active renderer is present. Used for dedicated server hosting.
 */
public class ActiveWorldUpdater {
	protected int fps;
	protected int actualFPS;
	protected int actualUPS;
	protected boolean running = false;
	protected int noDelaysPerYield = 5;
	protected int maxFrameSkips = 10;

	protected boolean doMapUpdate;

	protected double updateTime;

	protected Cobra2DWorld world;

	protected Thread rendererThread;
	protected Runnable rendererProcess = new Runnable() {

		@Override
		public void run() {
			// Currently the active world updater does actively compensate any delays, so the delta time should be neutral
			long deltaStart = 0;
			float deltaTime = 1f;
			long startTime, stopTime, neededTime, sleepTime, oversleepTime = 0;
			long excess = 0L;
			int noDelays = 0;

			final long period = (1000 / fps) * 1000000L;

			while (running) {

				deltaStart = System.nanoTime();

				// Start
				startTime = System.nanoTime();
				// Update world
				if (doMapUpdate) {
					world.update(fps, deltaTime);
				}
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
					while ((excess > period) && (skips < ActiveWorldUpdater.this.maxFrameSkips)) {
						excess -= period;
						if (!running) {
							break;
						}
						// Update world, test again for loop
						if (doMapUpdate) {
							// Update world
							if (doMapUpdate) {
								world.update(fps, deltaTime);
							}
						}
						// state but don't render
						skips++;
					}
				}
				stopTime = System.nanoTime();
				neededTime = stopTime - startTime;

				deltaTime = (System.nanoTime() - deltaStart) * 1e-9f;
			}
		}
	};

	public ActiveWorldUpdater(int fps, boolean doMapUpdate, Cobra2DWorld world) {
		super();
		this.fps = fps;
		this.doMapUpdate = doMapUpdate;
		this.world = world;
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
