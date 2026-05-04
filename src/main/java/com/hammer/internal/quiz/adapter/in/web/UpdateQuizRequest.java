package com.hammer.internal.quiz.adapter.in.web;

import com.hammer.internal.quiz.application.dto.UpdateQuizCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "퀴즈 수정 요청")
record UpdateQuizRequest(
        @Schema(description = "퀴즈 질문", example = "대한민국의 수도는?") @NotBlank @Size(max = 500) String question,
        @Schema(description = "선택지 1", example = "서울") @NotBlank @Size(max = 200) String choice1,
        @Schema(description = "선택지 2", example = "부산") @NotBlank @Size(max = 200) String choice2,
        @Schema(description = "선택지 3", example = "대구") @NotBlank @Size(max = 200) String choice3,
        @Schema(description = "선택지 4", example = "인천") @NotBlank @Size(max = 200) String choice4,
        @Schema(description = "정답 인덱스 (0~3)", example = "0") @Min(0) @Max(3) int correctIndex,
        @Schema(description = "해설", example = "대한민국의 수도는 서울입니다.") @NotBlank @Size(max = 1000) String explanation) {

    UpdateQuizCommand toCommand() {
        return new UpdateQuizCommand(question, choice1, choice2, choice3, choice4, correctIndex, explanation);
    }
}
