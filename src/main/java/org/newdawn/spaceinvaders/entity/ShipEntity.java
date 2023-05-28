package org.newdawn.spaceinvaders.entity;

import org.newdawn.spaceinvaders.Game;
import org.newdawn.spaceinvaders.Life;
import org.newdawn.spaceinvaders.Time;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The entity that represents the players ship
 *
 * @author Kevin Glass
 */
public class ShipEntity extends Entity {
	/** The game in which the ship exists */
	private Game game;

	private boolean used = false;

	/**
	 * Create a new entity to represent the players ship
	 *
	 * @param game The game in which the ship is being created
	 * @param ref The reference to the sprite to show for the ship
	 * @param x The initial x location of the player's ship
	 * @param y The initial y location of the player's ship
	 */
	public ShipEntity(Game game, String ref, int x, int y) {
		super(ref,x,y);

		this.game = game;
	}


	/**
	 * Request that the ship move itself based on an elapsed ammount of
	 * time
	 *
	 * @param delta The time that has elapsed since last move (ms)
	 */
	public void move(long delta) {
		// if we're moving left and have reached the left hand side
		// of the screen, don't move
		if ((dx < 0) && (x < 10)) {
			return;
		}
		// if we're moving right and have reached the right hand side
		// of the screen, don't move
		if ((dx > 0) && (x > 750)) {
			return;
		}

		super.move(delta);
	}

	/**
	 * Notification that the player's ship has collided with something
	 *
	 * @param other The entity with which the ship has collided
	 */
	public void collidedWith(Entity other) {
		if (used) {
			return;
		}
		// 충돌 처리
		if (other instanceof AlienEntity || other instanceof BossAlienEntity || other instanceof BossShotEntity || other instanceof ObstacleEntity) {
			if (game.isItem3Activated()) {
				// 무적 아이템을 사용 중인 경우
				used = true;
				return;
			}

			if (Life.get().getLifeCnt() > 1){

				Life.get().setLifeCnt(Life.get().getLifeCnt()-1);
				Life.get().setLifeReduced(true);
				used = true;
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						used = false;
					}
				}, 10000); //10초간 무적
			}
			else {
				game.notifyDeath();
			}
		}
	}
}