package org.newdawn.spaceinvaders.entity;
import org.newdawn.spaceinvaders.Game;
import org.newdawn.spaceinvaders.Sprite;
import org.newdawn.spaceinvaders.SpriteStore;

import static java.lang.Math.random;

public class ItemEntity extends Entity{

    public boolean ItemActivity=false;

    private Game game;


    public ItemEntity(Game game,String ref,int x,int y){
        super(ref,x,y);
        this.game=game;

        //int x=400;//(int)(random()*500)+100;
        //int y=300;//(int)(random()*300)+200;
        //setX(x);
        //setY(y);

    }


    @Override
    public void collidedWith(Entity other) {
        ItemActivity=true;
    }
}