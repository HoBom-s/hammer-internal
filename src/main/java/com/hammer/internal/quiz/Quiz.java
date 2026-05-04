package com.hammer.internal.quiz;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

@Entity
@Table(name = "quizzes", schema = "auction")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String question;

    @Column(nullable = false, length = 200)
    private String choice1;

    @Column(nullable = false, length = 200)
    private String choice2;

    @Column(nullable = false, length = 200)
    private String choice3;

    @Column(nullable = false, length = 200)
    private String choice4;

    @Column(name = "correct_index", nullable = false)
    private int correctIndex;

    @Column(nullable = false, length = 1000)
    private String explanation;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    protected Quiz() {
    }

    public Quiz(String question, String choice1, String choice2, String choice3,
                String choice4, int correctIndex, String explanation) {
        this.question = question;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.correctIndex = correctIndex;
        this.explanation = explanation;
        this.createdAt = OffsetDateTime.now();
    }

    public Long getId() { return id; }

    public String getQuestion() { return question; }

    public String getChoice1() { return choice1; }

    public String getChoice2() { return choice2; }

    public String getChoice3() { return choice3; }

    public String getChoice4() { return choice4; }

    public int getCorrectIndex() { return correctIndex; }

    public String getExplanation() { return explanation; }

    public OffsetDateTime getCreatedAt() { return createdAt; }

    public void update(String question, String choice1, String choice2, String choice3,
                       String choice4, int correctIndex, String explanation) {
        this.question = question;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.correctIndex = correctIndex;
        this.explanation = explanation;
    }
}
