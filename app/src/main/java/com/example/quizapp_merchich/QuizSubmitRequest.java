package com.example.quizapp_merchich;

import com.google.gson.annotations.SerializedName;

public class QuizSubmitRequest {
    @SerializedName("userId")
    private String userId;

    @SerializedName("score")
    private int score;

    @SerializedName("totalQuestions")
    private int totalQuestions;

    @SerializedName("continent")
    private String continent;

    public QuizSubmitRequest(String userId, int score, int totalQuestions, String continent) {
        this.userId = userId;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.continent = continent != null ? continent : "Global";
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public String getContinent() { return continent; }
    public void setContinent(String continent) { this.continent = continent; }
}
