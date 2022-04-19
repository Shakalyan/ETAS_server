package com.example.etas_server.service;

import com.example.etas_server.configuration.TranslationAPIConfig;
import com.example.etas_server.dto.Response;
import com.example.etas_server.dto.TranslationRequest;
import com.example.etas_server.dto.TranslationResponse;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Service
public class TranslationService {

    TranslationAPIConfig translationAPIConfig;

    @Autowired
    public TranslationService(TranslationAPIConfig translationAPIConfig) {
        this.translationAPIConfig = translationAPIConfig;
    }

    public Response getTranslation(TranslationRequest translationRequest) {
        String json = new Gson().toJson(translationRequest);
        String domain = translationAPIConfig.getDomain();
        String APIHost = translationAPIConfig.getAPIHost();
        String APIKey = System.getenv("TRANSLATION_API_KEY");

        HttpURLConnection connection = null;
        try {
            URL url = new URL(domain);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("X-RapidAPI-Host", APIHost);
            connection.setRequestProperty("X-RapidAPI-Key", APIKey);
            connection.setDoOutput(true);
            connection.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            TranslationResponse translationResponse = new Gson().fromJson(response.toString(), TranslationResponse.class);
            return new Response(200, translationResponse.getTranslation());
        } catch (IOException e) {
            return new Response(-1, e.getMessage());
        } finally {
            if(connection != null)
                connection.disconnect();
        }
    }

}
