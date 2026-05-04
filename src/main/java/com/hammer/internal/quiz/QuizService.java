package com.hammer.internal.quiz;

import com.hammer.internal.common.dto.PagedResponse;
import com.hammer.internal.common.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuizService {

    private final QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @Transactional(readOnly = true)
    public PagedResponse<QuizResponse> getQuizzes(int page, int size) {
        Page<Quiz> result = quizRepository.findAll(
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        return new PagedResponse<>(
                result.getContent().stream().map(QuizResponse::from).toList(),
                page,
                size,
                result.getTotalElements(),
                result.getTotalPages());
    }

    @Transactional(readOnly = true)
    public QuizResponse getQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Quiz", id));
        return QuizResponse.from(quiz);
    }

    @Transactional
    public QuizResponse createQuiz(CreateQuizRequest request) {
        var quiz = new Quiz(
                request.question(),
                request.choice1(),
                request.choice2(),
                request.choice3(),
                request.choice4(),
                request.correctIndex(),
                request.explanation());
        Quiz saved = quizRepository.save(quiz);
        return QuizResponse.from(saved);
    }

    @Transactional
    public QuizResponse updateQuiz(Long id, UpdateQuizRequest request) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Quiz", id));
        quiz.update(
                request.question(),
                request.choice1(),
                request.choice2(),
                request.choice3(),
                request.choice4(),
                request.correctIndex(),
                request.explanation());
        return QuizResponse.from(quiz);
    }

    @Transactional
    public void deleteQuiz(Long id) {
        if (!quizRepository.existsById(id)) {
            throw new NotFoundException("Quiz", id);
        }
        quizRepository.deleteById(id);
    }
}
