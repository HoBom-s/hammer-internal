package com.hammer.internal.errorlog.application.port.in;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.errorlog.application.dto.ErrorLogInfo;

public interface ListErrorLogsUseCase {

    PagedResult<ErrorLogInfo> listErrorLogs(int page, int size, Integer status);
}
