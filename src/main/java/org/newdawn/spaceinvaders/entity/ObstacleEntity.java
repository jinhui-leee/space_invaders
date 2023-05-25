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
        dx = moveSpeed;
    }

    /**
     * Request that the ship move itself based on an elapsed ammount of
     * time
     *
     * @param delta The time that has elapsed since last move (ms)
     */
    public void move(long delta) {

        lastFrameChange += delta;

        if (lastFrameChange > frameDuration) {
            lastFrameChange = 0;
        }

        super.move(delta);

    }
    public void doLogic() {
        dx = -dx;
        x -= 10;
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
        if (other instanceof ShipEntity) {
            game.removeEntity(this);
            game.removeEntity(other);
            game.notifyDeath();
            used = true;
        }
    }
}