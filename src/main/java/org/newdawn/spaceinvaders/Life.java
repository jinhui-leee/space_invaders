package org.newdawn.spaceinvaders;

public class Life {
    private static Life life = new Life();

    public static Life get() {
        return life;
    }

    private int lifeCnt = 1;


    public int getLifeCnt() {
        return lifeCnt;
    }

    public void setLifeCnt(int lifeCnt) {
        this.lifeCnt = lifeCnt;
    }
}
