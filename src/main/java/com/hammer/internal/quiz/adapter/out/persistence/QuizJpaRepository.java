package com.hammer.internal.quiz.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface QuizJpaRepository extends JpaRepository<QuizJpaEntity, Long> {}
