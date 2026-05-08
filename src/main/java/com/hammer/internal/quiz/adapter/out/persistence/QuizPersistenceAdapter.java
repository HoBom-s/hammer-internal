package com.hammer.internal.quiz.adapter.out.persistence;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.quiz.application.port.out.DeleteQuizPort;
import com.hammer.internal.quiz.application.port.out.LoadQuizPort;
import com.hammer.internal.quiz.application.port.out.SaveQuizPort;
import com.hammer.internal.quiz.domain.Quiz;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
class QuizPersistenceAdapter implements LoadQuizPort, SaveQuizPort, DeleteQuizPort {

    private final QuizJpaRepository jpaRepository;

    QuizPersistenceAdapter(QuizJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Quiz> findById(Long id) {
        return jpaRepository.findById(id).map(QuizMapper::toDomain);
    }

    @Override
    public PagedResult<Quiz> findAll(int page, int size) {
        Page<QuizJpaEntity> result =
                jpaRepository.findAll(PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        return toPagedResult(result, page, size);
    }

    @Override
    public PagedResult<Quiz> findByKeyword(String keyword, int page, int size) {
        Page<QuizJpaEntity> result = jpaRepository.findByQuestionContainingIgnoreCase(
                keyword, PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        return toPagedResult(result, page, size);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public Quiz save(Quiz quiz) {
        QuizJpaEntity entity = QuizMapper.toJpaEntity(quiz);
        return QuizMapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public List<Quiz> findRandom(int count) {
        return jpaRepository.findRandom(count).stream()
                .map(QuizMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    private PagedResult<Quiz> toPagedResult(Page<QuizJpaEntity> result, int page, int size) {
        return new PagedResult<>(
                result.getContent().stream().map(QuizMapper::toDomain).toList(),
                page,
                size,
                result.getTotalElements(),
                result.getTotalPages());
    }
}
