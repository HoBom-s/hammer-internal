package com.hammer.internal.user.adapter.out.persistence;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.user.application.port.out.LoadUserPort;
import com.hammer.internal.user.application.port.out.SaveUserPort;
import com.hammer.internal.user.domain.User;
import com.hammer.internal.user.domain.UserStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
class UserPersistenceAdapter implements LoadUserPort, SaveUserPort {

    private final UserJpaRepository jpaRepository;

    UserPersistenceAdapter(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id).map(UserMapper::toDomain);
    }

    @Override
    public PagedResult<User> findAll(int page, int size) {
        Page<UserJpaEntity> result =
                jpaRepository.findAll(PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        return toPagedResult(result, page, size);
    }

    @Override
    public PagedResult<User> findByStatus(UserStatus status, int page, int size) {
        Page<UserJpaEntity> result = jpaRepository.findByStatus(
                status.getCode(), PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        return toPagedResult(result, page, size);
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = UserMapper.toJpaEntity(user);
        return UserMapper.toDomain(jpaRepository.save(entity));
    }

    private PagedResult<User> toPagedResult(Page<UserJpaEntity> result, int page, int size) {
        return new PagedResult<>(
                result.getContent().stream().map(UserMapper::toDomain).toList(),
                page,
                size,
                result.getTotalElements(),
                result.getTotalPages());
    }
}
