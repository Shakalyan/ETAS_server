package com.example.etas_server.dto;

public class TranslationResponse {

    private Data data;

    public TranslationResponse(Data data) {
        this.data = data;
    }

    public String getTranslation() {
        return data.translations.translatedText;
    }

    private class Data {

        private Translations translations;

        public Data(Translations translations) {
            this.translations = translations;
        }

        private class Translations {

            private String translatedText;

            public Translations(String translatedText) {
                this.translatedText = translatedText;
            }
        }

    }

}