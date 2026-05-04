package com.hammer.internal.quiz.domain;

import java.time.OffsetDateTime;

public class Quiz {

    private final Long id;
    private String question;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
    private CorrectIndex correctIndex;
    private String explanation;
    private final OffsetDateTime createdAt;

    public Quiz(
            String question,
            String choice1,
            String choice2,
            String choice3,
            String choice4,
            int correctIndex,
            String explanation) {
        this.id = null;
        this.question = question;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.correctIndex = new CorrectIndex(correctIndex);
        this.explanation = explanation;
        this.createdAt = OffsetDateTime.now();
    }

    public Quiz(
            Long id,
            String question,
            String choice1,
            String choice2,
            String choice3,
            String choice4,
            int correctIndex,
            String explanation,
            OffsetDateTime createdAt) {
        this.id = id;
        this.question = question;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.correctIndex = new CorrectIndex(correctIndex);
        this.explanation = explanation;
        this.createdAt = createdAt;
    }

    public void update(
            String question,
            String choice1,
            String choice2,
            String choice3,
            String choice4,
            int correctIndex,
            String explanation) {
        this.question = question;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.correctIndex = new CorrectIndex(correctIndex);
        this.explanation = explanation;
    }

    public Long getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getChoice1() {
        return choice1;
    }

    public String getChoice2() {
        return choice2;
    }

    public String getChoice3() {
        return choice3;
    }

    public String getChoice4() {
        return choice4;
    }

    public int getCorrectIndex() {
        return correctIndex.value();
    }

    public String getExplanation() {
        return explanation;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
