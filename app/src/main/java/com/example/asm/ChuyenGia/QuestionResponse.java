package com.example.asm.ChuyenGia;

public class QuestionResponse {

    private String topic;
    private String question;
    private String response;

    public QuestionResponse() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public QuestionResponse(String topic, String question, String response) {
        this.topic = topic;
        this.question = question;
        this.response = response;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}