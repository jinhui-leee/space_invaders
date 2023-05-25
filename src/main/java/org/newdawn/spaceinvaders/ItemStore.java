package org.newdawn.spaceinvaders;

import java.util.Arrays;

public class ItemStore {

    Game game;

    private int itemPurchaseCnt[];

    private int totalItemPurchaseCnt = 0;


    public ItemStore(Game game) {
        this.game = game;
        itemPurchaseCnt = new int[4];
        Arrays.fill(itemPurchaseCnt, 0);
    }


    public int purchaseITem(int item) {
        int currentGoldCnt = Gold.get().getGoldCnt();
        //game.getGoldCnt();

        if (item == 0) {
            //아이템 구매 : ship +20 속도 증가, 20원
            if (currentGoldCnt >= 20) {
                Gold.get().setGoldCnt(currentGoldCnt-20);
                game.setMoveSpeed(game.getMoveSpeed()+20);
                itemPurchaseCnt[0]++;
                totalItemPurchaseCnt++;
                return 1;
            }
            else {
                return 0;
            }
        }
        //아이템 구매 : 총알 속도 -5 증가, 20원
        else if (item == 1) {
            if (currentGoldCnt >= 20) {
                Gold.get().setGoldCnt(currentGoldCnt-20);
                game.setShotSpeed(game.getShotSpeed()-5);
                itemPurchaseCnt[1]++;
                totalItemPurchaseCnt++;
                return 1;
            }
            else return 0;
        }
        //아이템 구매 : 총알 발사 간격 -10, 50원
        else if (item == 2) {
            if (currentGoldCnt >= 50) {
                Gold.get().setGoldCnt(currentGoldCnt-50);
                game.setFiringInterval(game.getFiringInterval()-10);
                game.setDefaultFiringInterval(game.getFiringInterval());

                itemPurchaseCnt[2]++;
                totalItemPurchaseCnt++;

                return 1;
            }
            else return 0;
        }
        //아이템 구매 : 생명 +1, 200원
        else if (item == 3) {
            if (currentGoldCnt >= 200) {
                Gold.get().setGoldCnt(currentGoldCnt-200);

                Life.get().setLifeCnt(Life.get().getLifeCnt()+1);

                itemPurchaseCnt[3]++;
                totalItemPurchaseCnt++;

                return 1;
            }
            else return 0;
        }
        return -1;
    }

    public int getItemPurchaseCnt(int item) {
        return itemPurchaseCnt[item];
    }

    public void setItemPurchaseCnt(int item, int itemPurchaseCnt) {
        this.itemPurchaseCnt[item] = itemPurchaseCnt;
    }

    public int getTotalItemPurchaseCnt() {
        return totalItemPurchaseCnt;
    }

    public void setTotalItemPurchaseCnt(int totalItemPurchaseCnt) {
        this.totalItemPurchaseCnt = totalItemPurchaseCnt;
    }
}
