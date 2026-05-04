package com.hammer.internal.quiz.application.port.out;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.quiz.domain.Quiz;
import java.util.Optional;

public interface LoadQuizPort {

    Optional<Quiz> findById(Long id);

    PagedResult<Quiz> findAll(int page, int size);

    boolean existsById(Long id);
}
