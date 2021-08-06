package com.base.engine.core;

import java.util.ArrayList;

import javax.sound.midi.Sequence;

import com.base.engine.entity.Guard;
import com.base.engine.graphics.BasicShader;
import com.base.engine.graphics.Material;
import com.base.engine.graphics.Shader;
import com.base.engine.graphics.Window;
import com.base.engine.input.Input;
import com.base.engine.level.Level;
import com.base.engine.maths.Matrix4f;
import com.base.engine.utils.AudioUtil;

public class Game {
	private static final int STARTING_LEVEL = 1;
	private static Level level;
	private static Shader shader;

	private static int levelNum;
	private static ArrayList<Sequence> playlist = new ArrayList<Sequence>();
	private static int track;
	private static boolean isRunning;

	public Game() {
		shader = BasicShader.getInstance();
		Transform.setProjection(70f, Window.getWidth(), Window.getHeight(), 0.1f, 1000f);

		for (int i = 2; i <= 27; i++)
			playlist.add(ResourceLoader.loadMidi("WOLF" + i + ".mid"));

		track = STARTING_LEVEL - 1;
		levelNum = STARTING_LEVEL - 1;
		loadLevel(1);

		isRunning = true;
	}

	public void input() {
		level.input();

		if (Input.getKey(Input.KEY_1))
			System.exit(0);

//		if(Input.getKeyDown(Input.KEY_R))
//		{
//			AudioUtil.playMidi(playlist.get(track));
//		}
	}

	public void update() {
		if (isRunning) {
			level.update();
		}
	}

	public void render() {
		if (isRunning) {
			level.render();
		}
	}

	public static void updateShader(Matrix4f worldMatrix, Matrix4f projectedMatrix, Material material) {
		shader.bind();
		shader.updateUniforms(worldMatrix, projectedMatrix, material);
	}

	public static void loadLevel(int offset) {
		try {
			int deadGuards = 0;
			int totalGuards = 0;
			boolean displayGuards = false;

			if (level != null) {
				totalGuards = level.getGuards().size();

				for (Guard monster : level.getGuards()) {
					if (!monster.isAlive())
						deadGuards++;
				}

				displayGuards = true;
			}

			levelNum += offset;
			level = new Level(ResourceLoader.loadBitmap("level" + levelNum + ".png").flipX(), new Material(ResourceLoader.loadTexture("WolfCollection.png")));

			track += offset;

			AudioUtil.playMidi(playlist.get(track));

			while (track >= playlist.size())
				track -= playlist.size();

			System.out.println("=============================");
			System.out.println("Level " + levelNum + ": No Name");
			System.out.println("=============================");

			if (displayGuards) {
				System.out.println("Killed " + deadGuards + "/" + totalGuards + " baddies: " + ((float) deadGuards / (float) totalGuards) * 100f + "%");
			}
		} catch (Exception ex) {
			isRunning = false;
			System.out.println("A winner is you!");
			AudioUtil.playMidi(null);
		}

	}

	public static Level getLevel() {
		return level;
	}

	public static Shader getShader() {
		return shader;
	}

	public static void setIsRunning(boolean value) {
		isRunning = value;
	}
}