package com.hammer.internal.quiz;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateQuizRequest(
        @NotBlank @Size(max = 500) String question,
        @NotBlank @Size(max = 200) String choice1,
        @NotBlank @Size(max = 200) String choice2,
        @NotBlank @Size(max = 200) String choice3,
        @NotBlank @Size(max = 200) String choice4,
        @Min(0) @Max(3) int correctIndex,
        @NotBlank @Size(max = 1000) String explanation
) {
}
