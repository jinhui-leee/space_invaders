package org.newdawn.spaceinvaders;

public class Life {
    private static Life life = new Life();

    public static Life get() {
        return life;
    }

    private int lifeCnt = 1;

    private boolean isLifeReduced = false;


    public int getLifeCnt() {
        return lifeCnt;
    }

    public void setLifeCnt(int lifeCnt) {
        this.lifeCnt = lifeCnt;
    }

    public boolean isLifeReduced() {
        return isLifeReduced;
    }

    public void setLifeReduced(boolean lifeReduced) {
        isLifeReduced = lifeReduced;
    }
}
