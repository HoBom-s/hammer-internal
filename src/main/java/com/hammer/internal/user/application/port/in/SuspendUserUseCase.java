package com.hammer.internal.user.application.port.in;

import com.hammer.internal.user.application.dto.UserInfo;
import java.util.UUID;

public interface SuspendUserUseCase {

    UserInfo suspend(UUID id);
}
