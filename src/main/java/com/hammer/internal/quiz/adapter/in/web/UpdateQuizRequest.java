package com.hammer.internal.quiz.adapter.in.web;

import com.hammer.internal.quiz.application.dto.UpdateQuizCommand;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

record UpdateQuizRequest(
        @NotBlank @Size(max = 500) String question,
        @NotBlank @Size(max = 200) String choice1,
        @NotBlank @Size(max = 200) String choice2,
        @NotBlank @Size(max = 200) String choice3,
        @NotBlank @Size(max = 200) String choice4,
        @Min(0) @Max(3) int correctIndex,
        @NotBlank @Size(max = 1000) String explanation) {

    UpdateQuizCommand toCommand() {
        return new UpdateQuizCommand(question, choice1, choice2, choice3, choice4, correctIndex, explanation);
    }
}
