package com.hammer.internal.quiz.application.dto;

import com.hammer.internal.quiz.domain.Quiz;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import java.util.List;

@Schema(description = "퀴즈 정보")
public record QuizInfo(
        @Schema(description = "퀴즈 ID", example = "1") Long id,
        @Schema(description = "퀴즈 질문", example = "대한민국의 수도는?") String question,
        @Schema(description = "선택지 목록 (4개)", example = "[\"서울\",\"부산\",\"대구\",\"인천\"]") List<String> choices,
        @Schema(description = "정답 인덱스 (0~3)", example = "0") int correctIndex,
        @Schema(description = "해설", example = "대한민국의 수도는 서울입니다.") String explanation,
        @Schema(description = "생성 일시") OffsetDateTime createdAt) {

    public static QuizInfo from(Quiz quiz) {
        return new QuizInfo(
                quiz.getId(),
                quiz.getQuestion(),
                List.of(quiz.getChoice1(), quiz.getChoice2(), quiz.getChoice3(), quiz.getChoice4()),
                quiz.getCorrectIndex(),
                quiz.getExplanation(),
                quiz.getCreatedAt());
    }
}
