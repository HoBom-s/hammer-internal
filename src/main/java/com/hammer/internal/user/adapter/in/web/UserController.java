package com.hammer.internal.user.adapter.in.web;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.user.application.dto.UserInfo;
import com.hammer.internal.user.application.port.in.GetUserUseCase;
import com.hammer.internal.user.application.port.in.ListUsersUseCase;
import com.hammer.internal.user.domain.UserStatus;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/users")
class UserController {

    private final GetUserUseCase getUserUseCase;
    private final ListUsersUseCase listUsersUseCase;

    UserController(GetUserUseCase getUserUseCase, ListUsersUseCase listUsersUseCase) {
        this.getUserUseCase = getUserUseCase;
        this.listUsersUseCase = listUsersUseCase;
    }

    @GetMapping
    public PagedResult<UserInfo> getUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Short status) {
        UserStatus userStatus = (status != null) ? UserStatus.fromCode(status) : null;
        return listUsersUseCase.listUsers(page, size, userStatus);
    }

    @GetMapping("/{id}")
    public UserInfo getUser(@PathVariable UUID id) {
        return getUserUseCase.getUser(id);
    }
}
