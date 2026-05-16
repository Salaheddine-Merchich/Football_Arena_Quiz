package com.example.quizapp_merchich;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OllamaApi {
    @POST("/api/generate")
    Call<OllamaResponse> generate(@Body OllamaRequest request);
}
