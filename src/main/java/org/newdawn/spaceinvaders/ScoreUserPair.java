package org.newdawn.spaceinvaders;

class ScoreUserPair implements Comparable<ScoreUserPair> {
    private int score;
    private String user;

    public ScoreUserPair(int score, String user) {
        this.score = score;
        this.user = user;
    }

    public int getScore() {
        return score;
    }

    public String getUser() {
        return user;
    }

    @Override
    public int compareTo(ScoreUserPair other) {
        return Integer.compare(score, other.getScore());
    }
}