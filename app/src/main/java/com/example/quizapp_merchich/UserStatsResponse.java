package com.example.quizapp_merchich;

import com.google.gson.annotations.SerializedName;

public class UserStatsResponse {
    @SerializedName("firebaseUid")
    private String firebaseUid;

    @SerializedName("totalQuizzesTaken")
    private int totalQuizzesTaken;

    @SerializedName("totalScore")
    private int totalScore;

    @SerializedName("totalQuestions")
    private int totalQuestions;

    @SerializedName("averagePercentage")
    private double averagePercentage;

    @SerializedName("bestScore")
    private int bestScore;

    @SerializedName("worstScore")
    private int worstScore;

    // Getters
    public String getFirebaseUid() { return firebaseUid; }
    public int getTotalQuizzesTaken() { return totalQuizzesTaken; }
    public int getTotalScore() { return totalScore; }
    public int getTotalQuestions() { return totalQuestions; }
    public double getAveragePercentage() { return averagePercentage; }
    public int getBestScore() { return bestScore; }
    public int getWorstScore() { return worstScore; }
}
