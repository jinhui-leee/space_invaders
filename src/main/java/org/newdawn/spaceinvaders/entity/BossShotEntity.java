package org.newdawn.spaceinvaders.entity;

import org.newdawn.spaceinvaders.Game;

public class BossShotEntity extends Entity{
    /** The vertical speed at which the players shot moves */
    private double moveSpeed = 300;
    /** The game in which this entity exists */
    private Game game;
    /** True if this shot has been "used", i.e. its hit something */
    private boolean used = false;


    /**
     * Create a new shot from the player
     *
     * @param game The game in which the shot has been created
     * @param sprite The sprite representing this shot
     * @param x The initial x location of the shot
     * @param y The initial y location of the shot
     */
    public BossShotEntity(Game game, String sprite, int x, int y) {
        super(sprite,x,y);

        this.game = game;

        dy = moveSpeed;
    }

    /**
     * Request that this shot moved based on time elapsed
     *
     * @param delta The time that has elapsed since last move
     */
    public void move(long delta) {
        // proceed with normal move
        super.move(delta);

        // if we shot off the screen, remove ourselfs
        if (y > 750) {
            game.removeEntity(this);
        }
    }

    /**
     * Notification that this shot has collided with another
     * entity
     *
     * @parma other The other entity with which we've collided
     */
    public void collidedWith(Entity other) {
        // prevents double kills, if we've already hit something,
        // don't collide
        if (used) {
            return;
        }

        // if we've hit an alien, kill it!
        if (other instanceof ShipEntity) {
            // remove the affected entities
            game.removeEntity(this);
            game.removeEntity(other);

            // notify the game that the alien has been killed
            used = true;
        }

    }
}
