package com.hammer.internal.user.application.service;

import com.hammer.internal.common.domain.NotFoundException;
import com.hammer.internal.user.application.dto.UserInfo;
import com.hammer.internal.user.application.port.in.SuspendUserUseCase;
import com.hammer.internal.user.application.port.out.LoadUserPort;
import com.hammer.internal.user.application.port.out.SaveUserPort;
import com.hammer.internal.user.domain.User;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class SuspendUserService implements SuspendUserUseCase {

    private final LoadUserPort loadUserPort;
    private final SaveUserPort saveUserPort;

    SuspendUserService(LoadUserPort loadUserPort, SaveUserPort saveUserPort) {
        this.loadUserPort = loadUserPort;
        this.saveUserPort = saveUserPort;
    }

    @Override
    public UserInfo suspend(UUID id) {
        User user = loadUserPort.findById(id).orElseThrow(() -> new NotFoundException("User", id));
        user.suspend();
        User saved = saveUserPort.save(user);
        return UserInfo.from(saved);
    }
}
