package com.hammer.internal.notification.adapter.out.persistence;

import com.hammer.internal.notification.application.port.out.DeleteTemplatePort;
import com.hammer.internal.notification.application.port.out.LoadTemplatePort;
import com.hammer.internal.notification.application.port.out.SaveTemplatePort;
import com.hammer.internal.notification.domain.NotificationTemplate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
class NotificationTemplatePersistenceAdapter implements LoadTemplatePort, SaveTemplatePort, DeleteTemplatePort {

    private final NotificationTemplateJpaRepository jpaRepository;

    NotificationTemplatePersistenceAdapter(NotificationTemplateJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<NotificationTemplate> findById(UUID id) {
        return jpaRepository.findById(id).map(NotificationTemplateMapper::toDomain);
    }

    @Override
    public List<NotificationTemplate> findAllOrderByTemplateKey() {
        return jpaRepository.findAllByOrderByTemplateKeyAsc().stream()
                .map(NotificationTemplateMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsByTemplateKey(String templateKey) {
        return jpaRepository.existsByTemplateKey(templateKey);
    }

    @Override
    public Optional<NotificationTemplate> findByTemplateKey(String templateKey) {
        return jpaRepository.findByTemplateKey(templateKey).map(NotificationTemplateMapper::toDomain);
    }

    @Override
    public NotificationTemplate save(NotificationTemplate template) {
        NotificationTemplateJpaEntity entity = NotificationTemplateMapper.toJpaEntity(template);
        return NotificationTemplateMapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
