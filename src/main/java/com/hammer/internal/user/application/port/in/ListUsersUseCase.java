package com.hammer.internal.user.application.port.in;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.user.application.dto.UserInfo;
import com.hammer.internal.user.domain.UserStatus;

public interface ListUsersUseCase {

    PagedResult<UserInfo> listUsers(int page, int size, UserStatus status);
}
