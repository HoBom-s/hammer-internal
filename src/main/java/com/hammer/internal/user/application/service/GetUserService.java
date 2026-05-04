package com.hammer.internal.user.application.service;

import com.hammer.internal.common.domain.NotFoundException;
import com.hammer.internal.user.application.dto.UserInfo;
import com.hammer.internal.user.application.port.in.GetUserUseCase;
import com.hammer.internal.user.application.port.out.LoadUserPort;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
class GetUserService implements GetUserUseCase {

    private final LoadUserPort loadUserPort;

    GetUserService(LoadUserPort loadUserPort) {
        this.loadUserPort = loadUserPort;
    }

    @Override
    public UserInfo getUser(UUID id) {
        return loadUserPort.findById(id).map(UserInfo::from).orElseThrow(() -> new NotFoundException("User", id));
    }
}
