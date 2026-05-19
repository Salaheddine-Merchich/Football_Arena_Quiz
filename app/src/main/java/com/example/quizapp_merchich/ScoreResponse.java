package com.example.quizapp_merchich;

import com.google.gson.annotations.SerializedName;

public class ScoreResponse {
    @SerializedName("id")
    private Long id;

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

    @SerializedName("playedAt")
    private String playedAt;

    // Getters
    public Long getId() { return id; }
    public String getUserId() { return userId; }
    public int getScore() { return score; }
    public int getTotalQuestions() { return totalQuestions; }
    public double getPercentage() { return percentage; }
    public String getContinent() { return continent; }
    public String getPlayedAt() { return playedAt; }
}
