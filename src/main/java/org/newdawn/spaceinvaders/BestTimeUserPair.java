package org.newdawn.spaceinvaders;

class BestTimeUserPair {
    private int bestTime;
    private String user;

    public BestTimeUserPair(int bestTime, String user) {
        this.bestTime = bestTime;
        this.user = user;
    }

    public int getBestTime() {
        return bestTime;
    }

    public String getUser() {
        return user;
    }

    public int compareTo(BestTimeUserPair other) {
        return this.bestTime - other.bestTime;
    }
}
