package com.example.quizapp_merchich;

import com.google.gson.annotations.SerializedName;

public class UserSyncRequest {
    @SerializedName("firebaseUid")
    private String firebaseUid;

    @SerializedName("email")
    private String email;

    public UserSyncRequest(String firebaseUid, String email) {
        this.firebaseUid = firebaseUid;
        this.email = email;
    }

    public String getFirebaseUid() { return firebaseUid; }
    public String getEmail() { return email; }
}
