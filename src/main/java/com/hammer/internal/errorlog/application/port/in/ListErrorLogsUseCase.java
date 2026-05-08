package com.hammer.internal.errorlog.application.port.in;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.errorlog.application.dto.ErrorLogInfo;
import com.hammer.internal.errorlog.application.dto.ErrorLogSearchCriteria;

public interface ListErrorLogsUseCase {

    PagedResult<ErrorLogInfo> listErrorLogs(int page, int size, ErrorLogSearchCriteria criteria);
}
