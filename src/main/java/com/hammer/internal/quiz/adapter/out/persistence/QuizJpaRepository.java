package com.hammer.internal.quiz.adapter.out.persistence;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface QuizJpaRepository extends JpaRepository<QuizJpaEntity, Long> {

    @Query(value = "SELECT * FROM auction.quizzes ORDER BY RANDOM() LIMIT :count", nativeQuery = true)
    List<QuizJpaEntity> findRandom(@Param("count") int count);

    Page<QuizJpaEntity> findByQuestionContainingIgnoreCase(String keyword, Pageable pageable);
}
