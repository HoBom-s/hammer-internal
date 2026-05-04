package com.hammer.internal.errorlog.application.service;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.errorlog.application.dto.ErrorLogInfo;
import com.hammer.internal.errorlog.application.port.in.ListErrorLogsUseCase;
import com.hammer.internal.errorlog.application.port.out.LoadErrorLogPort;
import com.hammer.internal.errorlog.domain.ErrorLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
class ListErrorLogsService implements ListErrorLogsUseCase {

    private final LoadErrorLogPort loadErrorLogPort;

    ListErrorLogsService(LoadErrorLogPort loadErrorLogPort) {
        this.loadErrorLogPort = loadErrorLogPort;
    }

    @Override
    public PagedResult<ErrorLogInfo> listErrorLogs(int page, int size, Integer status) {
        PagedResult<ErrorLog> result = (status != null)
                ? loadErrorLogPort.findByStatus(status, page, size)
                : loadErrorLogPort.findAll(page, size);

        return new PagedResult<>(
                result.items().stream().map(ErrorLogInfo::from).toList(),
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages());
    }
}
