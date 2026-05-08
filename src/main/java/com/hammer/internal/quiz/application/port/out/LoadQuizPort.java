package com.hammer.internal.quiz.application.port.out;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.quiz.domain.Quiz;
import java.util.List;
import java.util.Optional;

public interface LoadQuizPort {

    Optional<Quiz> findById(Long id);

    PagedResult<Quiz> findAll(int page, int size);

    PagedResult<Quiz> findByKeyword(String keyword, int page, int size);

    boolean existsById(Long id);

    List<Quiz> findRandom(int count);
}
