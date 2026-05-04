package com.hammer.internal.common.adapter.in.web;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info =
                @Info(
                        title = "Hammer Internal Admin API",
                        version = "0.0.1",
                        description = "사용자 조회, 퀴즈 관리, 알림 템플릿 관리를 위한 내부 관리자 API"),
        servers = {@Server(url = "http://localhost:8090", description = "Local")})
class OpenApiConfig {}
