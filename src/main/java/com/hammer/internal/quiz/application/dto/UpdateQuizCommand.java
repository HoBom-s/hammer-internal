package com.hammer.internal.quiz.application.dto;

public record UpdateQuizCommand(
        String question,
        String choice1,
        String choice2,
        String choice3,
        String choice4,
        int correctIndex,
        String explanation) {}
