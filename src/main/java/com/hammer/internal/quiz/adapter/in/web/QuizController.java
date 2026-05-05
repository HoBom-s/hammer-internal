package com.hammer.internal.quiz.adapter.in.web;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.quiz.application.dto.QuizInfo;
import com.hammer.internal.quiz.application.port.in.CreateQuizUseCase;
import com.hammer.internal.quiz.application.port.in.DeleteQuizUseCase;
import com.hammer.internal.quiz.application.port.in.GetQuizUseCase;
import com.hammer.internal.quiz.application.port.in.GetRandomQuizzesUseCase;
import com.hammer.internal.quiz.application.port.in.ListQuizzesUseCase;
import com.hammer.internal.quiz.application.port.in.UpdateQuizUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Quizzes", description = "퀴즈 관리 API")
@RestController
@RequestMapping("/internal/quizzes")
class QuizController {

    private final GetQuizUseCase getQuizUseCase;
    private final ListQuizzesUseCase listQuizzesUseCase;
    private final GetRandomQuizzesUseCase getRandomQuizzesUseCase;
    private final CreateQuizUseCase createQuizUseCase;
    private final UpdateQuizUseCase updateQuizUseCase;
    private final DeleteQuizUseCase deleteQuizUseCase;

    QuizController(
            GetQuizUseCase getQuizUseCase,
            ListQuizzesUseCase listQuizzesUseCase,
            GetRandomQuizzesUseCase getRandomQuizzesUseCase,
            CreateQuizUseCase createQuizUseCase,
            UpdateQuizUseCase updateQuizUseCase,
            DeleteQuizUseCase deleteQuizUseCase) {
        this.getQuizUseCase = getQuizUseCase;
        this.listQuizzesUseCase = listQuizzesUseCase;
        this.getRandomQuizzesUseCase = getRandomQuizzesUseCase;
        this.createQuizUseCase = createQuizUseCase;
        this.updateQuizUseCase = updateQuizUseCase;
        this.deleteQuizUseCase = deleteQuizUseCase;
    }

    @Operation(summary = "퀴즈 목록 조회", description = "페이징을 적용하여 퀴즈 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public PagedResult<QuizInfo> getQuizzes(
            @Parameter(description = "페이지 번호 (1부터 시작)", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "페이지 크기", example = "20") @RequestParam(defaultValue = "20") int size) {
        return listQuizzesUseCase.listQuizzes(page, size);
    }

    @Operation(summary = "랜덤 퀴즈 조회", description = "지정된 개수만큼 랜덤으로 퀴즈를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/random")
    public List<QuizInfo> getRandomQuizzes(
            @Parameter(description = "조회할 퀴즈 수", example = "3") @RequestParam(defaultValue = "3") int count) {
        return getRandomQuizzesUseCase.getRandomQuizzes(count);
    }

    @Operation(summary = "퀴즈 단건 조회", description = "ID로 특정 퀴즈를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "퀴즈를 찾을 수 없음", content = @Content)
    })
    @GetMapping("/{id}")
    public QuizInfo getQuiz(@Parameter(description = "퀴즈 ID", example = "1") @PathVariable Long id) {
        return getQuizUseCase.getQuiz(id);
    }

    @Operation(summary = "퀴즈 생성", description = "새로운 퀴즈를 생성합니다. 4지선다 중 정답 인덱스(0~3)를 지정합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "생성 성공"),
        @ApiResponse(responseCode = "400", description = "유효성 검증 실패", content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuizInfo createQuiz(@Valid @RequestBody CreateQuizRequest request) {
        return createQuizUseCase.create(request.toCommand());
    }

    @Operation(summary = "퀴즈 수정", description = "기존 퀴즈의 내용을 수정합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공"),
        @ApiResponse(responseCode = "400", description = "유효성 검증 실패", content = @Content),
        @ApiResponse(responseCode = "404", description = "퀴즈를 찾을 수 없음", content = @Content)
    })
    @PutMapping("/{id}")
    public QuizInfo updateQuiz(
            @Parameter(description = "퀴즈 ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody UpdateQuizRequest request) {
        return updateQuizUseCase.update(id, request.toCommand());
    }

    @Operation(summary = "퀴즈 삭제", description = "퀴즈를 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "삭제 성공"),
        @ApiResponse(responseCode = "404", description = "퀴즈를 찾을 수 없음", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuiz(@Parameter(description = "퀴즈 ID", example = "1") @PathVariable Long id) {
        deleteQuizUseCase.delete(id);
    }
}
