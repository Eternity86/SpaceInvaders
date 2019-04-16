package com.aleks.game.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;
import com.aleks.game.spaceinvaders.Direction;
import com.aleks.game.spaceinvaders.ShapeMatrix;
import com.aleks.game.spaceinvaders.SpaceInvadersGame;

import java.util.ArrayList;
import java.util.List;

public class EnemyFleet {
  private static final int ROWS_COUNT = 3;
  private static final int COLUMNS_COUNT = 10;
  private static final int STEP = ShapeMatrix.ENEMY.length + 1;
  private List<EnemyShip> ships;
  private Direction direction = Direction.RIGHT;

  private void createShips() {
	ships = new ArrayList<>();
	ships.add(new Boss(STEP * COLUMNS_COUNT / 2 - ShapeMatrix.BOSS_ANIMATION_FIRST.length / 2 - 1, 5));
	for (int x = 0; x < COLUMNS_COUNT; x++) {
	  for (int y = 0; y < ROWS_COUNT; y++) {
		ships.add(new EnemyShip(x * STEP, y * STEP + 12));
	  }
	}
  }

  private double getSpeed() {
	return 2.0 > 3.0 / ships.size() ? 3.0 / ships.size() : 2.0;
  }

  private double getLeftBorder() {
	double min = ships.get(0).x;
	for (EnemyShip enemyShip : ships) {
	  if (enemyShip.x < min) {
		min = enemyShip.x;
	  }
	}
	return min;
  }

  private double getRightBorder() {
	double max = ships.get(0).x + ships.get(0).width;
	for (EnemyShip enemyShip : ships) {
	  if ((enemyShip.x + enemyShip.width) > max) {
		max = (enemyShip.x + enemyShip.width);
	  }
	}
	return max;
  }

  public EnemyFleet() {
	createShips();
  }

  public void draw(Game game) {
	for (EnemyShip enemyShip : ships) {
	  enemyShip.draw(game);
	}
  }

  public void move() {
	if (ships.size() != 0) {
	  if (direction == Direction.LEFT && getLeftBorder() < 0) {
		direction = Direction.RIGHT;
		for (EnemyShip enemyShip : ships) {
		  enemyShip.move(Direction.DOWN, getSpeed());
		}
	  }
	  if (direction == Direction.RIGHT && getRightBorder() > SpaceInvadersGame.WIDTH) {
		direction = Direction.LEFT;
		for (EnemyShip enemyShip : ships) {
		  enemyShip.move(Direction.DOWN, getSpeed());
		}
	  }
	  for (EnemyShip enemyShip : ships) {
		enemyShip.move(direction, getSpeed());
	  }
	}
  }

  public double getBottomBorder() {
	double max = ships.get(0).y + ships.get(0).height;
	for (EnemyShip enemyShip : ships) {
	  if (max < (enemyShip.y + enemyShip.height)) {
		max = enemyShip.y + enemyShip.height;
	  }
	}
	return max;
  }

  public int getShipsCount() {
	return ships.size();
  }

  public int verifyHit(List<Bullet> bullets) {
	int score = 0;
	if (bullets.size() == 0) {
	  return 0;
	}
	for (EnemyShip enemyShip : ships) {
	  for (Bullet bullet : bullets) {
		if (enemyShip.isAlive && bullet.isAlive && enemyShip.isCollision(bullet)) {
		  enemyShip.kill();
		  score += enemyShip.score;
		  bullet.kill();
		}
	  }
	}
	return score;
  }

  public void deleteHiddenShips() {
	ships.removeIf(nextEnemyShip -> !nextEnemyShip.isVisible());
  }

  public Bullet fire(Game game) {
	if (ships.isEmpty() || game.getRandomNumber(100 / SpaceInvadersGame.COMPLEXITY) > 0) {
	  return null;
	}
	return ships.get(game.getRandomNumber(ships.size())).fire();
  }

}
