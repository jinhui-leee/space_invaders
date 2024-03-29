package org.newdawn.spaceinvaders.entity;
import org.newdawn.spaceinvaders.Game;

import java.util.Timer;
import java.util.TimerTask;

public class ItemEntity extends Entity{

    private boolean isItemBoxAcquired = false;

    private Game game;

    private boolean isItem1Activated = false;
    private boolean isItem2Activated = false;
    private boolean isItem3Activated = false;


    public ItemEntity(Game game,String ref){
        super(ref,0,0);
        this.game=game;

        int x = (int)(Math.random() * (game.getWidth() - sprite.getWidth()));
        int y = (int)(Math.random() * 200)+200;
        super.setLocation(x, y);
    }

    @Override
    public void collidedWith(Entity other) {
        isItemBoxAcquired = true;
        if(other instanceof ShipEntity){
            game.removeEntity(this);
            useItem();
        }
    }

    public void useItem(){

        int itemRandomNum=(int)(Math.random()*3)+1;
        if (itemRandomNum==1){
            isItem1Activated =true;
        }
        else if(itemRandomNum==2){
            game.setDefaultFiringInterval(game.getFiringInterval());
            game.setFiringInterval(game.getFiringInterval()-250);
            isItem2Activated =true;
        }
        else if(itemRandomNum==3){
            isItem3Activated = true;

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isItem3Activated = false;}
            }, 10000); //10초간 무적
        }
    }

    public void resetItem(){
        game.setFiringInterval(game.getDefaultFiringInterval());
        isItem1Activated = false;
        isItem2Activated = false;
        isItem3Activated = false;
    }

    public int isItemActivated() {
        if (isItem1Activated) return 1;
        else if (isItem2Activated) return  2;
        else return 0;
    }

    public void setItemActivated(boolean itemActivated) {
        isItem1Activated = itemActivated;
    }

    public void setItem2Activated(boolean item2Activated) {
        isItem2Activated = item2Activated;
    }

    public boolean isItem3Activated() {
        return isItem3Activated;
    }

    public void setItem3Activated(boolean item3Activated) {
        isItem3Activated = item3Activated;
    }
}