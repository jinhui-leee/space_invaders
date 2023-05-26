package org.newdawn.spaceinvaders;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String email;
    private String password;
    private String name;
    private Integer gold;
    private String bestTime;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String password, String name, Integer gold, String bestTime) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.gold = gold;
        this.bestTime = bestTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGold() {
        return gold;
    }

    public void setGold(Integer gold) {
        this.gold = gold;

        String encodedEmail = Base64.getEncoder().encodeToString(getEmail().getBytes());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        DatabaseReference userRef = ref.child("Users").child(encodedEmail);

        Map<String, Object> updates = new HashMap<>();
        updates.put("/" + encodedEmail + "/gold", getGold());

        userRef.updateChildren(updates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
            }
        });

    }

    public String getBestTime() {
        return bestTime;
    }

    public void setBestTime(String bestTime) {
        this.bestTime = bestTime;
    }
}