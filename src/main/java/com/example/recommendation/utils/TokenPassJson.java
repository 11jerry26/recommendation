package com.example.recommendation.utils;

public class TokenPassJson {
    private int success;
    private String token;

    public TokenPassJson(){}

    public TokenPassJson(int success, String token) {
        this.success = success;
        this.token = token;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "TokenPassJson{" +
                "success=" + success +
                ", token='" + token + '\'' +
                '}';
    }
}
