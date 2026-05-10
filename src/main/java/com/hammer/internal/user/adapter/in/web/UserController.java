package com.hammer.internal.user.adapter.in.web;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.user.application.dto.UserInfo;
import com.hammer.internal.user.application.port.in.ActivateUserUseCase;
import com.hammer.internal.user.application.port.in.GetUserUseCase;
import com.hammer.internal.user.application.port.in.ListUsersUseCase;
import com.hammer.internal.user.application.port.in.SuspendUserUseCase;
import com.hammer.internal.user.domain.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Users", description = "사용자 조회 API")
@RestController
@RequestMapping("/internal/users")
class UserController {

    private final GetUserUseCase getUserUseCase;
    private final ListUsersUseCase listUsersUseCase;
    private final SuspendUserUseCase suspendUserUseCase;
    private final ActivateUserUseCase activateUserUseCase;

    UserController(
            GetUserUseCase getUserUseCase,
            ListUsersUseCase listUsersUseCase,
            SuspendUserUseCase suspendUserUseCase,
            ActivateUserUseCase activateUserUseCase) {
        this.getUserUseCase = getUserUseCase;
        this.listUsersUseCase = listUsersUseCase;
        this.suspendUserUseCase = suspendUserUseCase;
        this.activateUserUseCase = activateUserUseCase;
    }

    @Operation(summary = "사용자 목록 조회", description = "페이징 및 상태 필터를 적용하여 사용자 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public PagedResult<UserInfo> getUsers(
            @Parameter(description = "페이지 번호 (1부터 시작)", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "페이지 크기", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "상태 필터 (1: Active, 2: Suspended, 3: Deleted)") @RequestParam(required = false)
                    Short status) {
        UserStatus userStatus = (status != null) ? UserStatus.fromCode(status) : null;
        return listUsersUseCase.listUsers(page, size, userStatus);
    }

    @Operation(summary = "사용자 단건 조회", description = "UUID로 특정 사용자를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content)
    })
    @GetMapping("/{id}")
    public UserInfo getUser(
            @Parameter(description = "사용자 ID", example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable
                    UUID id) {
        return getUserUseCase.getUser(id);
    }

    @Operation(
            summary = "사용자 정지",
            description = "사용자 계정을 정지(Suspended) 상태로 전환합니다. 이미 정지된 계정은 그대로 두고, 삭제된 계정은 정지할 수 없습니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "정지 성공"),
        @ApiResponse(responseCode = "400", description = "삭제된 계정은 정지할 수 없음", content = @Content),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content)
    })
    @PostMapping("/{id}/suspend")
    public UserInfo suspendUser(
            @Parameter(description = "사용자 ID", example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable
                    UUID id) {
        return suspendUserUseCase.suspend(id);
    }

    @Operation(
            summary = "사용자 활성화",
            description = "사용자 계정을 활성(Active) 상태로 전환합니다. 이미 활성 상태인 계정은 그대로 두고, 삭제된 계정은 활성화할 수 없습니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "활성화 성공"),
        @ApiResponse(responseCode = "400", description = "삭제된 계정은 활성화할 수 없음", content = @Content),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content)
    })
    @PostMapping("/{id}/activate")
    public UserInfo activateUser(
            @Parameter(description = "사용자 ID", example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable
                    UUID id) {
        return activateUserUseCase.activate(id);
    }
}
