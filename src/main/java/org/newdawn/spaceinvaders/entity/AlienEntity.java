package org.newdawn.spaceinvaders.entity;

import org.newdawn.spaceinvaders.Framework;
import org.newdawn.spaceinvaders.Game;
import org.newdawn.spaceinvaders.Sprite;
import org.newdawn.spaceinvaders.SpriteStore;

/**
 * An entity which represents one of our space invader aliens.
 * 
 * @author Kevin Glass
 */
public class AlienEntity extends Entity {
	/** The speed at which the alien moves horizontally */
	private double moveSpeed = 75;
	/** The game in which the entity exists */
	private Game game;
	/** The animation frames */
	private Sprite[] frames = new Sprite[2];
	/** The time since the last frame change took place */
	private long lastFrameChange;
	/** The frame duration in milliseconds, i.e. how long any given frame of animation lasts */
	private long frameDuration = 250;
	/** The current frame of animation being displayed */
	private int frameNumber;
	
	/**
	 * Create a new alien entity
	 * 
	 * @param game The game in which this entity is being created
	 * @param x The initial x location of this alien
	 * @param y The initial y location of this alien
	 */
	public AlienEntity(Game game, int x, int y) {
		super("images/ben10.png",x,y);

		if(Framework.themeChoice==0) {
			frames[0] = SpriteStore.get().getSprite("images/alien1A.png");
			frames[1] = SpriteStore.get().getSprite("images/alien1B.png");
		}
		else if(Framework.themeChoice==1) {
			frames[0] = SpriteStore.get().getSprite("images/alien2A.png");
			frames[1] = SpriteStore.get().getSprite("images/alien2B.png");
		}
		else if(Framework.themeChoice==2) {
			frames[0] = SpriteStore.get().getSprite("images/alien3A.png");
			frames[1] = SpriteStore.get().getSprite("images/alien3B.png");
		}
		else if(Framework.themeChoice==3) {
			frames[0] = SpriteStore.get().getSprite("images/alien4A.png");
			frames[1] = SpriteStore.get().getSprite("images/alien4B.png");
		}
		else if(Framework.themeChoice==4) {
			frames[0] = SpriteStore.get().getSprite("images/alien5A.png");
			frames[1] = SpriteStore.get().getSprite("images/alien5B.png");
		}
		else if(Framework.themeChoice==5) {
			frames[0] = SpriteStore.get().getSprite("images/alien6A.png");
			frames[1] = SpriteStore.get().getSprite("images/alien6B.png");
		}
		else if(Framework.themeChoice==6) {
			frames[0] = SpriteStore.get().getSprite("images/alien7A.png");
			frames[1] = SpriteStore.get().getSprite("images/alien7B.png");
		}
		else if(Framework.themeChoice==7) {
			frames[0] = SpriteStore.get().getSprite("images/alien8A.png");
			frames[1] = SpriteStore.get().getSprite("images/alien8B.png");
		}
		else if(Framework.themeChoice==8) {
			frames[0] = SpriteStore.get().getSprite("images/alien9A.png");
			frames[1] = SpriteStore.get().getSprite("images/alien9B.png");
		}

		this.game = game;
		dx = -moveSpeed;
	}

	/**
	 * Request that this alien moved based on time elapsed
	 * 
	 * @param delta The time that has elapsed since last move
	 */
	public void move(long delta) {
		// since the move tells us how much time has passed
		// by we can use it to drive the animation, however
		// it's not the prettiest solution
		lastFrameChange += delta;
		
		// if we need to change the frame, update the frame number
		// and flip over the sprite in use
		if (lastFrameChange > frameDuration) {
			// reset our frame change time counter
			lastFrameChange = 0;

			// update the frame
			frameNumber++;
			if (frameNumber >= frames.length) {
				frameNumber = 0;
			}
			sprite = frames[frameNumber];
		}

		// if we have reached the left hand side of the screen and
		// are moving left then request a logic update 
		if ((dx < 0) && (x < 10)) {
			game.updateLogic();
		}

		// and vice vesa, if we have reached the right hand side of 
		// the screen and are moving right, request a logic update
		if ((dx > 0) && (x > 750)) {
			game.updateLogic();
		}

		// proceed with normal move
		super.move(delta);
	}
	
	/**
	 * Update the game logic related to aliens
	 */
	public void doLogic() {
		//dx 변수의 값을 반전시키고, y 변수의 값을 10만큼 증가시킵니다.
		//이를 통해 스프라이트가 왼쪽과 오른쪽을 번갈아 이동하며, 점점 아래로 내려가는 동작을 구현합니다.
		dx = -dx;
		y += 10;
		
		// if we've reached the bottom of the screen then the player
		// dies
		if (y > 570) {
			game.notifyDeath();
		}
	}
	
	/**
	 * Notification that this alien has collided with another entity
	 * 
	 * @param other The other entity
	 */
	public void collidedWith(Entity other) {
		// collisions with aliens are handled elsewhere
	}
}