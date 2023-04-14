package org.newdawn.spaceinvaders;

class BestTimeUserPair implements Comparable<BestTimeUserPair> {
    private String bestTime;
    private String user;

    public BestTimeUserPair(String bestTime, String user) {
        this.bestTime = bestTime;
        this.user = user;
    }

    public String getBestTime() {
        return bestTime;
    }

    public String getUser() {
        return user;
    }

    @Override
    public int compareTo(BestTimeUserPair other) {
        return bestTime.compareTo(other.getBestTime());

    }
}