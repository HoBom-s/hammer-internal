package com.hammer.internal.notification.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface NotificationTemplateJpaRepository extends JpaRepository<NotificationTemplateJpaEntity, UUID> {

    List<NotificationTemplateJpaEntity> findAllByOrderByTemplateKeyAsc();

    boolean existsByTemplateKey(String templateKey);

    Optional<NotificationTemplateJpaEntity> findByTemplateKey(String templateKey);

    @Query("SELECT t FROM NotificationTemplateJpaEntity t WHERE "
            + "(:channel IS NULL OR t.channel = :channel) AND "
            + "(:keyword IS NULL OR LOWER(t.templateKey) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "OR :keyword IS NULL OR LOWER(t.titleTemplate) LIKE LOWER(CONCAT('%', :keyword, '%')))"
            + " ORDER BY t.templateKey ASC")
    Page<NotificationTemplateJpaEntity> search(
            @Param("channel") String channel, @Param("keyword") String keyword, Pageable pageable);
}
