package com.example.etas_server.dto;

public class TranslationRequest {

    String q;
    String source;
    String target;

    public TranslationRequest(String q, String source, String target) {
        this.q = q;
        this.source = source;
        this.target = target;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

}

