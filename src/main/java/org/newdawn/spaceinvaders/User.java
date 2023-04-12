package org.newdawn.spaceinvaders;

public class User {
    public String email;
    public String password;
    public String name;
    public Integer gold;
    public Integer score;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String password, String name, Integer gold, Integer score) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.gold = gold;
        this.score = score;
    }
}
