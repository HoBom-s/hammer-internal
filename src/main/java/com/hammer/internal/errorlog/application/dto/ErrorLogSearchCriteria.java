package com.hammer.internal.errorlog.application.dto;

import java.time.OffsetDateTime;

public record ErrorLogSearchCriteria(
        Integer status, String errorCode, OffsetDateTime from, OffsetDateTime to, String uri) {}
