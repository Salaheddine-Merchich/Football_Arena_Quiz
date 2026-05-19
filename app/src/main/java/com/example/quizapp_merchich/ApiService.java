package com.example.quizapp_merchich;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    @GET("api/questions")
    Call<ApiResponse<List<QuestionResponse>>> getQuestions();

    @GET("api/questions")
    Call<ApiResponse<List<QuestionResponse>>> getQuestionsByContinent(@Query("continent") String continent);

    @POST("api/scores")
    Call<ApiResponse<ScoreResponse>> submitScore(@Body QuizSubmitRequest request);

    @GET("api/scores/{userId}")
    Call<ApiResponse<List<ScoreResponse>>> getScores(@Path("userId") String userId);

    @POST("api/users/sync")
    Call<ApiResponse<UserResponse>> syncUser(@Body UserSyncRequest request);

    @GET("api/users/{userId}/stats")
    Call<ApiResponse<UserStatsResponse>> getUserStats(@Path("userId") String userId);

    @GET("api/scores/leaderboard")
    Call<ApiResponse<List<LeaderboardEntryResponse>>> getLeaderboard(@Query("continent") String continent);
}
