package ru.eternity074.game.spaceinvaders;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Key;
import ru.eternity074.game.spaceinvaders.gameobjects.Bullet;
import ru.eternity074.game.spaceinvaders.gameobjects.EnemyFleet;
import ru.eternity074.game.spaceinvaders.gameobjects.PlayerShip;
import ru.eternity074.game.spaceinvaders.gameobjects.Star;

import java.util.ArrayList;
import java.util.List;

public class SpaceInvadersGame extends Game {
	private static final int PLAYER_BULLETS_MAX = 1;
	private static final int TURN_TIMER = 40;
	private static final int STARS_COUNT = 8;
	private static final int TEXT_SIZE = 28;
	private List<Star> stars;
	private List<Bullet> enemyBullets;
	private List<Bullet> playerBullets;
	private EnemyFleet enemyFleet;
	private PlayerShip playerShip;
	private boolean isGameStopped;
	private int animationsCount;
	private int score;
	public static final int COMPLEXITY = 5;
	public static final int WIDTH = 64;
	public static final int HEIGHT = 64;

	private void createGame() {
		createStars();
		score = 0;
		playerShip = new PlayerShip();
		enemyFleet = new EnemyFleet();
		enemyBullets = new ArrayList<>();
		playerBullets = new ArrayList<>();
		setTurnTimer(TURN_TIMER);
		isGameStopped = false;
		animationsCount = 0;
		drawScene();
	}

	private void drawScene() {
		drawField();
		for (Bullet bullet : enemyBullets) {
			bullet.draw(this);
		}
		for (Bullet bullet : playerBullets) {
			bullet.draw(this);
		}
		enemyFleet.draw(this);
		playerShip.draw(this);
	}

	private void drawField() {
		for (int x = 0; x < HEIGHT; x++) {
			for (int y = 0; y < WIDTH; y++) {
				setCellValueEx(x, y, Color.BLACK, "");
			}
		}
		for (Star star : stars) {
			star.draw(this);
		}
	}

	private void createStars() {
		stars = new ArrayList<>();
		for (int i = 0; i < STARS_COUNT; i++) {
			stars.add(new Star(getRandomNumber(WIDTH), getRandomNumber(HEIGHT)));
		}
	}

	private void stopGame(boolean isWin) {
		isGameStopped = true;
		stopTurnTimer();
		if (isWin) {
			showMessageDialog(Color.WHITE, "YOU WIN!!!", Color.GREEN, TEXT_SIZE);
		} else {
			showMessageDialog(Color.WHITE, "YOU LOOSE!!!", Color.RED, TEXT_SIZE);
		}
	}

	private void stopGameWithDelay() {
		animationsCount++;
		if (animationsCount >= 10) {
			stopGame(playerShip.isAlive);
		}
	}

	private void moveSpaceObjects() {
		enemyFleet.move();
		for (Bullet bullet : enemyBullets) {
			bullet.move();
		}
		for (Bullet bullet : playerBullets) {
			bullet.move();
		}
		playerShip.move();
	}

	private void removeDeadBullets() {
		playerBullets.removeIf(nextBullet -> !nextBullet.isAlive || nextBullet.y + nextBullet.height < 0);
		enemyBullets.removeIf(nextBullet -> !nextBullet.isAlive || nextBullet.y >= HEIGHT - 1);
	}

	private void check() {
		playerShip.verifyHit(enemyBullets);
		score += enemyFleet.verifyHit(playerBullets);
		enemyFleet.deleteHiddenShips();
		if (enemyFleet.getBottomBorder() >= playerShip.y) {
			playerShip.kill();
		}
		if (enemyFleet.getShipsCount() == 0) {
			playerShip.win();
			stopGameWithDelay();
		}
		removeDeadBullets();
		if (!playerShip.isAlive) {
			stopGameWithDelay();
		}
	}

	@Override
	public void setCellValueEx(int x, int y, Color cellColor, String value) {
		if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
			super.setCellValueEx(x, y, cellColor, value);
		}
	}

	@Override
	public void initialize() {
		setScreenSize(WIDTH, HEIGHT);
		createGame();
	}

	@Override
	public void onTurn(int step) {
		moveSpaceObjects();
		check();
		Bullet bullet = enemyFleet.fire(this);
		if (bullet != null) {
			enemyBullets.add(bullet);
		}
		setScore(score);
		drawScene();
	}

	@Override
	public void onKeyPress(Key key) {
		if (isGameStopped && key == Key.SPACE) {
			createGame();
		}
		if (key == Key.SPACE) {
			Bullet bullet = playerShip.fire();
			if (bullet != null && playerBullets.size() < PLAYER_BULLETS_MAX) {
				playerBullets.add(bullet);
			}
		}
		if (key == Key.LEFT) {
			playerShip.setDirection(Direction.LEFT);
		}
		if (key == Key.RIGHT) {
			playerShip.setDirection(Direction.RIGHT);
		}
	}

	@Override
	public void onKeyReleased(Key key) {
		if (key == Key.LEFT && playerShip.getDirection() == Direction.LEFT) {
			playerShip.setDirection(Direction.UP);
		}
		if (key == Key.RIGHT && playerShip.getDirection() == Direction.RIGHT) {
			playerShip.setDirection(Direction.UP);
		}
	}
}
