package com.hammer.internal.user.application.port.out;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.user.domain.User;
import com.hammer.internal.user.domain.UserStatus;
import java.util.Optional;
import java.util.UUID;

public interface LoadUserPort {

    Optional<User> findById(UUID id);

    PagedResult<User> findAll(int page, int size);

    PagedResult<User> findByStatus(UserStatus status, int page, int size);
}
