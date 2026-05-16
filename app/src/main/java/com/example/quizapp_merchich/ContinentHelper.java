package com.example.quizapp_merchich;

import java.util.HashMap;
import java.util.Map;

public class ContinentHelper {
    private static final Map<String, String> countryToContinent = new HashMap<>();

    static {
        // --- AFRICA ---
        countryToContinent.put("MA", "Africa"); // Morocco
        countryToContinent.put("DZ", "Africa"); // Algeria
        countryToContinent.put("TN", "Africa"); // Tunisia
        countryToContinent.put("EG", "Africa"); // Egypt
        countryToContinent.put("SN", "Africa"); // Senegal
        countryToContinent.put("NG", "Africa"); // Nigeria

        // --- EUROPE ---
        countryToContinent.put("ES", "Europe"); // Spain
        countryToContinent.put("FR", "Europe"); // France
        countryToContinent.put("GB", "Europe"); // England
        countryToContinent.put("DE", "Europe"); // Germany
        countryToContinent.put("IT", "Europe"); // Italy

        // --- SOUTH AMERICA ---
        countryToContinent.put("BR", "South America"); // Brazil
        countryToContinent.put("AR", "South America"); // Argentina
        countryToContinent.put("UY", "South America"); // Uruguay

        // --- ASIA ---
        countryToContinent.put("JP", "Asia"); // Japan
        countryToContinent.put("KR", "Asia"); // South Korea
        countryToContinent.put("SA", "Asia"); // Saudi Arabia
    }

    public static String getContinent(String countryCode) {
        if (countryCode == null) return "Global";
        String continent = countryToContinent.get(countryCode.toUpperCase());
        return (continent != null) ? continent : "Global";
    }
}
