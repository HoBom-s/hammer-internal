package com.hammer.internal.dashboard.adapter.out.persistence;

import com.hammer.internal.dashboard.application.dto.DashboardStats;
import com.hammer.internal.dashboard.application.port.out.LoadDashboardPort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
class DashboardPersistenceAdapter implements LoadDashboardPort {

    private final EntityManager em;

    DashboardPersistenceAdapter(EntityManager em) {
        this.em = em;
    }

    @Override
    public long countUsers() {
        return em.createQuery("SELECT COUNT(u) FROM UserJpaEntity u", Long.class)
                .getSingleResult();
    }

    @Override
    public long countUsersByStatus(short statusCode) {
        return em.createQuery("SELECT COUNT(u) FROM UserJpaEntity u WHERE u.status = :status", Long.class)
                .setParameter("status", statusCode)
                .getSingleResult();
    }

    @Override
    public long countQuizzes() {
        return em.createQuery("SELECT COUNT(q) FROM QuizJpaEntity q", Long.class)
                .getSingleResult();
    }

    @Override
    public long countErrorLogsAfter(OffsetDateTime after) {
        return em.createQuery("SELECT COUNT(e) FROM ErrorLogJpaEntity e WHERE e.createdAt >= :after", Long.class)
                .setParameter("after", after)
                .getSingleResult();
    }

    @Override
    public List<DashboardStats.ErrorCountByCode> findTopErrorCodes(OffsetDateTime after, int limit) {
        List<Tuple> results = em.createQuery(
                        "SELECT e.errorCode AS errorCode, COUNT(e) AS cnt "
                                + "FROM ErrorLogJpaEntity e "
                                + "WHERE e.createdAt >= :after AND e.errorCode IS NOT NULL "
                                + "GROUP BY e.errorCode ORDER BY cnt DESC",
                        Tuple.class)
                .setParameter("after", after)
                .setMaxResults(limit)
                .getResultList();

        return results.stream()
                .map(t ->
                        new DashboardStats.ErrorCountByCode(t.get("errorCode", String.class), t.get("cnt", Long.class)))
                .toList();
    }

    @Override
    public long countErrorLogs() {
        return em.createQuery("SELECT COUNT(e) FROM ErrorLogJpaEntity e", Long.class)
                .getSingleResult();
    }

    @Override
    public long countNotificationTemplates() {
        return em.createQuery("SELECT COUNT(t) FROM NotificationTemplateJpaEntity t", Long.class)
                .getSingleResult();
    }

    @Override
    public List<DashboardStats.DailyErrorCount> findDailyErrorCounts(OffsetDateTime after) {
        List<Tuple> results = em.createNativeQuery(
                        "SELECT CAST(e.created_at AS DATE) AS log_date, COUNT(*) AS cnt "
                                + "FROM hammer.error_logs e "
                                + "WHERE e.created_at >= :after "
                                + "GROUP BY log_date ORDER BY log_date ASC",
                        Tuple.class)
                .setParameter("after", after)
                .getResultList();

        return results.stream()
                .map(t -> {
                    Object dateObj = t.get("log_date");
                    LocalDate date;
                    if (dateObj instanceof java.sql.Date sqlDate) {
                        date = sqlDate.toLocalDate();
                    } else {
                        date = LocalDate.parse(dateObj.toString());
                    }
                    Object cntObj = t.get("cnt");
                    long count;
                    if (cntObj instanceof BigDecimal bd) {
                        count = bd.longValue();
                    } else {
                        count = ((Number) cntObj).longValue();
                    }
                    return new DashboardStats.DailyErrorCount(date, count);
                })
                .toList();
    }
}
