package org.newdawn.spaceinvaders.entity;

import org.newdawn.spaceinvaders.Game;
import org.newdawn.spaceinvaders.Sprite;
import org.newdawn.spaceinvaders.SpriteStore;

public class BossAlienEntity extends Entity{

    /** The speed at which the alien moves horizontally */
    private double moveSpeed = 75;
    /** The game in which the entity exists */
    private Game game;
    /** The animation frames */
    private Sprite[] frames = new Sprite[4];
    /** The time since the last frame change took place */
    private long lastFrameChange;
    /** The frame duration in milliseconds, i.e. how long any given frame of animation lasts */
    private long frameDuration = 250;
    /** The current frame of animation being displayed */
    private int frameNumber;

    static public int life = 100;

    /**
     * Create a new alien entity
     *
     * @param game The game in which this entity is being created
     * @param x    The initial x location of this alien
     * @param y    The initial y location of this alien
     */
    public BossAlienEntity(Game game, int x, int y) {
        super("images/ufo_boss_alien.png", x, y);

        frames[0] = sprite;
        frames[1] = SpriteStore.get().getSprite("images/ufo_boss_alien2.png");
        frames[2] = sprite;
        frames[3] = SpriteStore.get().getSprite("images/ufo_boss_alien3.png");

        this.game = game;
        dx = -moveSpeed;

        if (game.getGameDifficulty() == 0) {
            this.life = 100;
        }
        else if (game.getGameDifficulty() == 1) {
            this.life = 200;
        }
        else {
            this.life = 300;
        }
    }

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
        if ((dx > 0) && (x > 750 - sprite.getWidth())) {
            game.updateLogic();
        }

        // proceed with normal move
        super.move(delta);
    }

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

    public int getLife() {
        return life;
    }

    @Override
    public void collidedWith(Entity other) {

        if (other instanceof ShotEntity) {

            life--;
            sprite = SpriteStore.get().getSprite("images/ufo_boss_alien4.png");

            if (life <= 0) {
                game.removeEntity(this);


                // notify the game that the alien has been killed
                game.notifyAlienKilled();
            }

        }


    }
}
