package com.example.quizapp_merchich;

import android.util.Log;

public class ApiLogger {
    public static final String TAG_REQUEST = "API_REQUEST";
    public static final String TAG_RESPONSE = "API_RESPONSE";
    public static final String TAG_ERROR = "API_ERROR";
    public static final String TAG_DEBUG = "RETROFIT_DEBUG";

    public static void logRequest(String endpoint, String payload) {
        Log.i(TAG_REQUEST, ">>>> SENDING REQUEST: " + endpoint);
        if (payload != null) {
            Log.i(TAG_REQUEST, "Payload: " + payload);
        }
    }

    public static void logResponse(String endpoint, int code, String body) {
        Log.i(TAG_RESPONSE, "<<<< RECEIVED RESPONSE: " + endpoint + " | CODE: " + code);
        if (body != null) {
            Log.i(TAG_RESPONSE, "Body: " + body);
        }
    }

    public static void logError(String endpoint, String message, Throwable t) {
        Log.e(TAG_ERROR, "!!!! API FAILURE: " + endpoint);
        Log.e(TAG_ERROR, "Reason: " + message);
        if (t != null) {
            Log.e(TAG_ERROR, "Exception: " + t.getClass().getSimpleName() + " - " + t.getMessage());
        }
    }

    public static void logDebug(String message) {
        Log.i(TAG_DEBUG, "DEBUG: " + message);
    }
}
