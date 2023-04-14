package org.newdawn.spaceinvaders.entity;

import org.newdawn.spaceinvaders.Game;

import java.util.ArrayList;

/**
 * An entity representing a shot fired by the player's ship
 *
 * @author Kevin Glass
 */
public class ItemShotEntity extends Entity {
    /** The vertical speed at which the players shot moves */
    private double moveSpeed = -300;
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
    public ItemShotEntity(Game game, String sprite, int x, int y) {
        super(sprite,x,y);

        this.game = game;
        dy = moveSpeed;
    }

    public void setMoveSpeed(int moveSpeed) {
        this.moveSpeed = moveSpeed;
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
        if (y < -100) {
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
        if (other instanceof AlienEntity) {
            ArrayList<AlienEntity> ailens=new ArrayList<>();
            ailens.add((AlienEntity)other);
            for(int i= -1;i<=1;i++){
                for(int j=-1;j<=2;j++){
                    int index = 0;
                    int targetIndex = index + i * Game.NUMBER_OF_ALIENS_TO_DESTROY + j;
                    if (targetIndex >= 0 && targetIndex < game.entities.size()) {
                        Entity target = game.entities.get(targetIndex);
                        if (target instanceof AlienEntity) {
                            aliens.add((AlienEntity) target);
                        }
                }
            }
            // remove the affected entities
            game.removeEntity(this);
            game.removeEntity(other);

            // notify the game that the alien has been killed
            game.notifyAlienKilled();
            used = true;
        }

        if (other instanceof BossAlienEntity) {
            // remove the affected entities
            game.removeEntity(this);
            used = true;
            other.collidedWith(this);
        }


    }
}