package com.example.quizapp_merchich;

public class Question {
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
    private int imageResource; // Directly using R.drawable ID
    private String continent;

    public Question(String questionText, String optionA, String optionB, String optionC, String optionD, String correctAnswer, int imageResource, String continent) {
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
        this.imageResource = imageResource;
        this.continent = continent;
    }

    public String getQuestionText() { return questionText; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public String getCorrectAnswer() { return correctAnswer; }
    public int getImageResource() { return imageResource; }
    public String getContinent() { return continent; }
}
