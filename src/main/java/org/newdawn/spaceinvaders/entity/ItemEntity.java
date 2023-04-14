package org.newdawn.spaceinvaders.entity;
import org.newdawn.spaceinvaders.Game;

public class ItemEntity extends Entity{

    public boolean ItemActivity=false;

    private Game game;


    public ItemEntity(Game game, String ref){
        super(ref,0,0);
        this.game=game;

        int x = (int)(Math.random() * (game.getWidth() - sprite.getWidth()));
        int y = (int)(Math.random() * (game.getHeight()- sprite.getHeight()));
        super.setLocation(x, y);


    }


    @Override
    public void collidedWith(Entity other) {
        ItemActivity=true;

        if(other instanceof ShipEntity){
            game.removeEntity(this);
            game.useItem();

        }
    }
}