package com.example.quizapp_merchich;

import com.google.gson.annotations.SerializedName;

public class LeaderboardEntryResponse {
    @SerializedName("rank")
    private int rank;

    @SerializedName("userId")
    private String userId;

    @SerializedName("score")
    private int score;

    @SerializedName("totalQuestions")
    private int totalQuestions;

    @SerializedName("percentage")
    private double percentage;

    @SerializedName("continent")
    private String continent;

    // Getters
    public int getRank() { return rank; }
    public String getUserId() { return userId; }
    public int getScore() { return score; }
    public int getTotalQuestions() { return totalQuestions; }
    public double getPercentage() { return percentage; }
    public String getContinent() { return continent; }
}
