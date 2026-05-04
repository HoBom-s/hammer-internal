package com.hammer.internal.user.application.service;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.user.application.dto.UserInfo;
import com.hammer.internal.user.application.port.in.ListUsersUseCase;
import com.hammer.internal.user.application.port.out.LoadUserPort;
import com.hammer.internal.user.domain.User;
import com.hammer.internal.user.domain.UserStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
class ListUsersService implements ListUsersUseCase {

    private final LoadUserPort loadUserPort;

    ListUsersService(LoadUserPort loadUserPort) {
        this.loadUserPort = loadUserPort;
    }

    @Override
    public PagedResult<UserInfo> listUsers(int page, int size, UserStatus status) {
        PagedResult<User> result =
                (status != null) ? loadUserPort.findByStatus(status, page, size) : loadUserPort.findAll(page, size);

        return new PagedResult<>(
                result.items().stream().map(UserInfo::from).toList(),
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages());
    }
}
