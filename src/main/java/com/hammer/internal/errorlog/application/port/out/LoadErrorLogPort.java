package com.hammer.internal.errorlog.application.port.out;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.errorlog.application.dto.ErrorLogSearchCriteria;
import com.hammer.internal.errorlog.domain.ErrorLog;

public interface LoadErrorLogPort {

    PagedResult<ErrorLog> search(ErrorLogSearchCriteria criteria, int page, int size);
}
