package com.base.engine;

import com.base.engine.core.Game;
import com.base.engine.core.Time;
import com.base.engine.graphics.Window;
import com.base.engine.input.Input;
import com.base.engine.utils.RenderUtil;

public class Main {

	public static final int WIDTH = 1024;
	public static final int HEIGHT = 600;
	public static final String TITLE = "Castle Ala-deen";
	public static final double FRAME_CAP = 60.0;

	private boolean isRunning;
	private Game game;

	public Main() {
		// System.out.println(RenderUtil.getOpenGLVersion());
		RenderUtil.initGraphics();
		isRunning = false;
		game = new Game();
	}

	public void start() {
		if (isRunning)
			return;

		run();
	}

	public void stop() {
		if (!isRunning)
			return;

		isRunning = false;
	}

	private void run() {
		isRunning = true;

		int frames = 0;
		long frameCounter = 0;

		final double frameTime = 1.0 / FRAME_CAP;

		long lastTime = Time.getTime();
		double unprocessedTime = 0;

		while (isRunning) {
			boolean render = false;

			long startTime = Time.getTime();
			long passedTime = startTime - lastTime;
			lastTime = startTime;

			unprocessedTime += passedTime / (double) Time.SECOND;
			frameCounter += passedTime;

			while (unprocessedTime > frameTime) {
				render = true;

				unprocessedTime -= frameTime;

				if (Window.isCloseRequested())
					stop();

				Time.setDelta(frameTime);

				game.input();
				Input.update();

				game.update();

				if (frameCounter >= Time.SECOND) {
					// System.out.println(frames);
					frames = 0;
					frameCounter = 0;
				}
			}
			if (render) {
				render();
				frames++;
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		cleanUp();
	}

	private void render() {
		RenderUtil.clearScreen();
		game.render();
		Window.render();
	}

	private void cleanUp() {
		Window.dispose();
	}

	public static void main(String[] args) {
		Window.createWindow(WIDTH, HEIGHT, TITLE);

		Main game = new Main();

		game.start();
		System.exit(0);
	}

}
