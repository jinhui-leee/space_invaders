package org.newdawn.spaceinvaders.entity;

import org.newdawn.spaceinvaders.Game;

/**
 * The entity that represents the players ship
 *
 * @author Kevin Glass
 */
public class ObstacleEntity extends Entity {
    /** The game in which the ship exists */
    private Game game;
    private double moveSpeed = 500;

    /** The time since the last frame change took place */
    private long lastFrameChange;
    /** The frame duration in milliseconds, i.e. how long any given frame of animation lasts */
    private long frameDuration = 250;

    private boolean used = false;


    /**
     * Create a new entity to represent the players ship
     *
     * @param game The game in which the ship is being created
     * @param ref The reference to the sprite to show for the ship
     * @param x The initial x location of the player's ship
     * @param y The initial y location of the player's ship
     */
    public ObstacleEntity(Game game, String ref, int x, int y) {
        super(ref,x,y);
        this.game = game;
        dy = moveSpeed;
    }

    /**
     * Request that the ship move itself based on an elapsed ammount of
     * time
     *
     * @param delta The time that has elapsed since last move (ms)
     */
    public void move(long delta) {
        // since the move tells us how much time has passed
        // by we can use it to drive the animation, however
        // its the not the prettiest solution
        lastFrameChange += delta;
        // if we need to change the frame, update the frame number
        // and flip over the sprite in use
        if (lastFrameChange > frameDuration) {
            // reset our frame change time counter
            lastFrameChange = 0;
        }

        // proceed with normal move
        super.move(delta);

    }
    public void doLogic() {
        // swap over horizontal movement and move down the
        // screen a bit
        dx = -dx;
        y += 10;

        // if we've reached the bottom of the screen then the player
        // dies
        if (y > 570) {
            game.removeEntity(this);
        }
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
        // if we've hit an alien, kill it!
        if (other instanceof ShipEntity) {
            // remove the affected entities
            game.removeEntity(this);
            used =true;
        }
    }
}