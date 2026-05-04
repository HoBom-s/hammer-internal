package com.hammer.internal.errorlog.application.port.out;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.errorlog.domain.ErrorLog;

public interface LoadErrorLogPort {

    PagedResult<ErrorLog> findAll(int page, int size);

    PagedResult<ErrorLog> findByStatus(int status, int page, int size);
}
