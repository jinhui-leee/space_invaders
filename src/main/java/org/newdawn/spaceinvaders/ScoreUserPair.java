package org.newdawn.spaceinvaders;

class ScoreUserPair implements Comparable<ScoreUserPair> {
    private String bestTime;
    private String user;

    public ScoreUserPair(String bestTime, String user) {
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
    public int compareTo(ScoreUserPair other) {
        return bestTime.compareTo(other.getBestTime());

    }
}