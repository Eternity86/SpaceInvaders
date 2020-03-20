package ru.eternity074.game.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ship extends GameObject {
	private List<int[][]> frames;
	private int frameIndex;
	private boolean loopAnimation = false;
	public boolean isAlive = true;

	public Ship(double x, double y) {
		super(x, y);
	}

	public boolean isVisible() {
		return isAlive || frameIndex < frames.size();
	}

	public void setAnimatedView(boolean isLoopAnimation, int[][]... viewFrames) {
		loopAnimation = isLoopAnimation;
		setMatrix(viewFrames[0]);
		frames = Arrays.asList(viewFrames);
		frameIndex = 0;
	}

	public void setStaticView(int[][] viewFrame) {
		super.setMatrix(viewFrame);
		frames = new ArrayList<>();
		frames.add(viewFrame);
		frameIndex = 0;
	}

	public void nextFrame() {
		frameIndex++;
		if (frameIndex >= frames.size() && loopAnimation) {
			frameIndex = 0;
		}
		if (frameIndex < frames.size()) {
			matrix = frames.get(frameIndex);
		}
	}

	@Override
	public void draw(Game game) {
		super.draw(game);
		nextFrame();
	}

	public Bullet fire() {
		return null;
	}

	public void kill() {
		isAlive = false;
	}

}
