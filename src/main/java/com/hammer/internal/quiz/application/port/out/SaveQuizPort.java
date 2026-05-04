package com.hammer.internal.quiz.application.port.out;

import com.hammer.internal.quiz.domain.Quiz;

public interface SaveQuizPort {

    Quiz save(Quiz quiz);
}
