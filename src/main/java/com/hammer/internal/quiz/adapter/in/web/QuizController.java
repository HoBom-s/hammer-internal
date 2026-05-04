package com.hammer.internal.quiz.adapter.in.web;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.quiz.application.dto.QuizInfo;
import com.hammer.internal.quiz.application.port.in.CreateQuizUseCase;
import com.hammer.internal.quiz.application.port.in.DeleteQuizUseCase;
import com.hammer.internal.quiz.application.port.in.GetQuizUseCase;
import com.hammer.internal.quiz.application.port.in.ListQuizzesUseCase;
import com.hammer.internal.quiz.application.port.in.UpdateQuizUseCase;
import jakarta.validation.Valid;
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

@RestController
@RequestMapping("/internal/quizzes")
class QuizController {

    private final GetQuizUseCase getQuizUseCase;
    private final ListQuizzesUseCase listQuizzesUseCase;
    private final CreateQuizUseCase createQuizUseCase;
    private final UpdateQuizUseCase updateQuizUseCase;
    private final DeleteQuizUseCase deleteQuizUseCase;

    QuizController(
            GetQuizUseCase getQuizUseCase,
            ListQuizzesUseCase listQuizzesUseCase,
            CreateQuizUseCase createQuizUseCase,
            UpdateQuizUseCase updateQuizUseCase,
            DeleteQuizUseCase deleteQuizUseCase) {
        this.getQuizUseCase = getQuizUseCase;
        this.listQuizzesUseCase = listQuizzesUseCase;
        this.createQuizUseCase = createQuizUseCase;
        this.updateQuizUseCase = updateQuizUseCase;
        this.deleteQuizUseCase = deleteQuizUseCase;
    }

    @GetMapping
    public PagedResult<QuizInfo> getQuizzes(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        return listQuizzesUseCase.listQuizzes(page, size);
    }

    @GetMapping("/{id}")
    public QuizInfo getQuiz(@PathVariable Long id) {
        return getQuizUseCase.getQuiz(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuizInfo createQuiz(@Valid @RequestBody CreateQuizRequest request) {
        return createQuizUseCase.create(request.toCommand());
    }

    @PutMapping("/{id}")
    public QuizInfo updateQuiz(@PathVariable Long id, @Valid @RequestBody UpdateQuizRequest request) {
        return updateQuizUseCase.update(id, request.toCommand());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuiz(@PathVariable Long id) {
        deleteQuizUseCase.delete(id);
    }
}
