package com.hammer.internal.user.application.port.out;

import com.hammer.internal.user.domain.User;

public interface SaveUserPort {

    User save(User user);
}
