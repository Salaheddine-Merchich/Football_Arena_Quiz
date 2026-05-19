package com.example.quizapp_merchich;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("id")
    private Long id;

    @SerializedName("firebaseUid")
    private String firebaseUid;

    @SerializedName("email")
    private String email;

    @SerializedName("createdAt")
    private String createdAt;

    // Getters
    public Long getId() { return id; }
    public String getFirebaseUid() { return firebaseUid; }
    public String getEmail() { return email; }
    public String getCreatedAt() { return createdAt; }
}
