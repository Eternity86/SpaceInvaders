package ru.eternity074.game.spaceinvaders.gameobjects;

import ru.eternity074.game.spaceinvaders.Direction;
import ru.eternity074.game.spaceinvaders.ShapeMatrix;
import ru.eternity074.game.spaceinvaders.SpaceInvadersGame;

import java.util.List;

public class PlayerShip extends Ship {
	private Direction direction = Direction.UP;

	public void setDirection(Direction newDirection) {
		if (newDirection != Direction.DOWN) {
			this.direction = newDirection;
		}
	}

	public Direction getDirection() {
		return direction;
	}

	public void move() {
		if (isAlive) {
			if (direction == Direction.LEFT) {
				x--;
			} else if (direction == Direction.RIGHT) {
				x++;
			}
			if (x < 0) {
				x = 0;
			}
			if (x + width > SpaceInvadersGame.WIDTH) {
				x = SpaceInvadersGame.WIDTH - width;
			}
		}
	}

	public PlayerShip() {
		super(SpaceInvadersGame.WIDTH / 2.0, SpaceInvadersGame.HEIGHT - ShapeMatrix.PLAYER.length - 1);
		setStaticView(ShapeMatrix.PLAYER);
	}

	public void win() {
		setStaticView(ShapeMatrix.WIN_PLAYER);
	}

	@Override
	public void kill() {
		if (isAlive) {
			isAlive = false;
			setAnimatedView(false, ShapeMatrix.KILL_PLAYER_ANIMATION_FIRST, ShapeMatrix.KILL_PLAYER_ANIMATION_SECOND,
					ShapeMatrix.KILL_PLAYER_ANIMATION_THIRD, ShapeMatrix.DEAD_PLAYER);
		}
	}

	@Override
	public Bullet fire() {
		if (!isAlive) {
			return null;
		}
		return new Bullet(x + 2, y - ShapeMatrix.BULLET.length, Direction.UP);
	}

	public void verifyHit(List<Bullet> bullets) {
		if (!bullets.isEmpty() && isAlive) {
			for (Bullet bullet : bullets) {
				if (bullet.isAlive && isCollision(bullet)) {
					kill();
					bullet.kill();
				}
			}
		}
	}
}
