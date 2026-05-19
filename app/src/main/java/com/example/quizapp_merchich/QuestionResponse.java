package com.example.quizapp_merchich;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class QuestionResponse {
    @SerializedName("id")
    private Long id;

    @SerializedName("question")
    private String question;

    @SerializedName("options")
    private List<String> options;

    @SerializedName("correctAnswer")
    private String correctAnswer;

    @SerializedName("continent")
    private String continent;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("difficulty")
    private String difficulty;

    @SerializedName("category")
    private String category;

    // Getters
    public Long getId() { return id; }
    public String getQuestion() { return question; }
    public List<String> getOptions() { return options; }
    public String getCorrectAnswer() { return correctAnswer; }
    public String getContinent() { return continent; }
    public String getImageUrl() { return imageUrl; }
    public String getDifficulty() { return difficulty; }
    public String getCategory() { return category; }
}
