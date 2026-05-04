package com.hammer.internal.quiz.domain;

public record CorrectIndex(int value) {

    private static final int MIN = 0;
    private static final int MAX = 3;

    public CorrectIndex {
        if (value < MIN || value > MAX) {
            throw new IllegalArgumentException(
                    "correctIndex must be between " + MIN + " and " + MAX + ", got: " + value);
        }
    }
}
