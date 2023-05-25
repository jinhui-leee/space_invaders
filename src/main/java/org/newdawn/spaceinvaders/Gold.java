package org.newdawn.spaceinvaders;

public class Gold {
    private static Gold gold = new Gold();

    public static Gold get() {
        return gold;
    }

    private int goldCnt;

    public int getGoldCnt() {
        return goldCnt;
    }

    public void setGoldCnt(int goldCnt) {
        this.goldCnt = goldCnt;
    }
}
