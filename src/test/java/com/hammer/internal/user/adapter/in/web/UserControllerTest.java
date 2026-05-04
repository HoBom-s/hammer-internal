package com.hammer.internal.user.adapter.in.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.common.application.port.SaveErrorLogPort;
import com.hammer.internal.common.domain.NotFoundException;
import com.hammer.internal.user.application.dto.UserInfo;
import com.hammer.internal.user.application.port.in.GetUserUseCase;
import com.hammer.internal.user.application.port.in.ListUsersUseCase;
import com.hammer.internal.user.domain.UserStatus;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    SaveErrorLogPort saveErrorLogPort;

    @MockitoBean
    GetUserUseCase getUserUseCase;

    @MockitoBean
    ListUsersUseCase listUsersUseCase;

    private static final OffsetDateTime FIXED_TIME = OffsetDateTime.parse("2024-06-15T12:00:00+09:00");

    @Nested
    class GetUsers {

        @Test
        void returns_paged_users_with_defaults() throws Exception {
            var user = new UserInfo(UUID.randomUUID(), "a@b.com", "nick", "Active", "1.0", FIXED_TIME, null);
            given(listUsersUseCase.listUsers(1, 20, null)).willReturn(new PagedResult<>(List.of(user), 1, 20, 1, 1));

            mockMvc.perform(get("/internal/users"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.items").isArray())
                    .andExpect(jsonPath("$.items[0].email").value("a@b.com"))
                    .andExpect(jsonPath("$.items[0].status").value("Active"))
                    .andExpect(jsonPath("$.page").value(1))
                    .andExpect(jsonPath("$.totalElements").value(1));
        }

        @Test
        void passes_status_filter_to_use_case() throws Exception {
            given(listUsersUseCase.listUsers(1, 10, UserStatus.Suspended))
                    .willReturn(new PagedResult<>(List.of(), 1, 10, 0, 0));

            mockMvc.perform(get("/internal/users")
                            .param("page", "1")
                            .param("size", "10")
                            .param("status", "2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.items").isEmpty());
        }
    }

    @Nested
    class GetUser {

        @Test
        void returns_user_by_id() throws Exception {
            UUID id = UUID.randomUUID();
            var user = new UserInfo(id, "a@b.com", "nick", "Active", "1.0", FIXED_TIME, null);
            given(getUserUseCase.getUser(id)).willReturn(user);

            mockMvc.perform(get("/internal/users/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id.toString()))
                    .andExpect(jsonPath("$.nickname").value("nick"));
        }

        @Test
        void returns_404_when_not_found() throws Exception {
            UUID id = UUID.randomUUID();
            given(getUserUseCase.getUser(id)).willThrow(new NotFoundException("User", id));

            mockMvc.perform(get("/internal/users/{id}", id)).andExpect(status().isNotFound());
        }
    }
}
