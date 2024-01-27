package com.estsb.QuizIT.Entity;

import lombok.Data;

@Data
public class CheckAnswerResponse {
    private final boolean correct;
    private final Object correctAnswers;
    private final String message;

    public CheckAnswerResponse(boolean correct, Object correctAnswers, String message) {
        this.correct = correct;
        this.correctAnswers = correctAnswers;
        this.message = message;
    }
}