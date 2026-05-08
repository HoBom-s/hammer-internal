package com.hammer.internal.notification.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.hammer.internal.common.application.PagedResult;
import com.hammer.internal.common.application.port.SaveErrorLogPort;
import com.hammer.internal.common.domain.NotFoundException;
import com.hammer.internal.notification.application.dto.CreateTemplateCommand;
import com.hammer.internal.notification.application.dto.PreviewTemplateResponse;
import com.hammer.internal.notification.application.dto.TemplateInfo;
import com.hammer.internal.notification.application.dto.UpdateTemplateCommand;
import com.hammer.internal.notification.application.port.in.CreateTemplateUseCase;
import com.hammer.internal.notification.application.port.in.DeleteTemplateUseCase;
import com.hammer.internal.notification.application.port.in.GetTemplateByKeyUseCase;
import com.hammer.internal.notification.application.port.in.GetTemplateUseCase;
import com.hammer.internal.notification.application.port.in.ListTemplatesUseCase;
import com.hammer.internal.notification.application.port.in.PreviewTemplateUseCase;
import com.hammer.internal.notification.application.port.in.UpdateTemplateUseCase;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(NotificationTemplateController.class)
class NotificationTemplateControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    SaveErrorLogPort saveErrorLogPort;

    @MockitoBean
    GetTemplateUseCase getTemplateUseCase;

    @MockitoBean
    GetTemplateByKeyUseCase getTemplateByKeyUseCase;

    @MockitoBean
    ListTemplatesUseCase listTemplatesUseCase;

    @MockitoBean
    CreateTemplateUseCase createTemplateUseCase;

    @MockitoBean
    UpdateTemplateUseCase updateTemplateUseCase;

    @MockitoBean
    DeleteTemplateUseCase deleteTemplateUseCase;

    @MockitoBean
    PreviewTemplateUseCase previewTemplateUseCase;

    private static final OffsetDateTime FIXED_TIME = OffsetDateTime.parse("2024-06-15T12:00:00+09:00");

    private static TemplateInfo sampleTemplateInfo(UUID id) {
        return new TemplateInfo(id, "welcome_push", "환영합니다", "{{name}}님 가입 축하", "Push", FIXED_TIME, FIXED_TIME);
    }

    private static final String VALID_TEMPLATE_JSON =
            """
            {
                "templateKey": "welcome_push",
                "titleTemplate": "환영합니다",
                "bodyTemplate": "가입 축하합니다",
                "channel": "Push"
            }
            """;

    @Nested
    class GetAllTemplates {

        @Test
        void returns_paged_templates() throws Exception {
            given(listTemplatesUseCase.listTemplates(1, 20, null, null))
                    .willReturn(new PagedResult<>(List.of(sampleTemplateInfo(UUID.randomUUID())), 1, 20, 1, 1));

            mockMvc.perform(get("/internal/notification-templates"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.items").isArray())
                    .andExpect(jsonPath("$.items[0].templateKey").value("welcome_push"))
                    .andExpect(jsonPath("$.items[0].channel").value("Push"));
        }

        @Test
        void returns_empty_page_when_none() throws Exception {
            given(listTemplatesUseCase.listTemplates(1, 20, null, null))
                    .willReturn(new PagedResult<>(List.of(), 1, 20, 0, 0));

            mockMvc.perform(get("/internal/notification-templates"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.items").isEmpty());
        }
    }

    @Nested
    class GetTemplateByKey {

        @Test
        void returns_template_by_key() throws Exception {
            given(getTemplateByKeyUseCase.getTemplateByKey("welcome_push"))
                    .willReturn(sampleTemplateInfo(UUID.randomUUID()));

            mockMvc.perform(get("/internal/notification-templates/by-key/welcome_push"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.templateKey").value("welcome_push"));
        }

        @Test
        void returns_404_when_key_not_found() throws Exception {
            given(getTemplateByKeyUseCase.getTemplateByKey("unknown"))
                    .willThrow(new NotFoundException("NotificationTemplate", "unknown"));

            mockMvc.perform(get("/internal/notification-templates/by-key/unknown"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class GetTemplate {

        @Test
        void returns_template_by_id() throws Exception {
            UUID id = UUID.randomUUID();
            given(getTemplateUseCase.getTemplate(id)).willReturn(sampleTemplateInfo(id));

            mockMvc.perform(get("/internal/notification-templates/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id.toString()))
                    .andExpect(jsonPath("$.templateKey").value("welcome_push"));
        }

        @Test
        void returns_404_when_not_found() throws Exception {
            UUID id = UUID.randomUUID();
            given(getTemplateUseCase.getTemplate(id)).willThrow(new NotFoundException("NotificationTemplate", id));

            mockMvc.perform(get("/internal/notification-templates/{id}", id)).andExpect(status().isNotFound());
        }
    }

    @Nested
    class CreateTemplate {

        @Test
        void returns_201_with_created_template() throws Exception {
            given(createTemplateUseCase.create(any(CreateTemplateCommand.class)))
                    .willReturn(sampleTemplateInfo(UUID.randomUUID()));

            mockMvc.perform(post("/internal/notification-templates")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(VALID_TEMPLATE_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.templateKey").value("welcome_push"));
        }

        @Test
        void returns_400_when_channel_invalid() throws Exception {
            mockMvc.perform(
                            post("/internal/notification-templates")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(
                                            """
                            {"templateKey":"key","titleTemplate":"t","bodyTemplate":"b","channel":"Email"}
                            """))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void returns_400_when_templateKey_is_blank() throws Exception {
            mockMvc.perform(
                            post("/internal/notification-templates")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(
                                            """
                            {"templateKey":"","titleTemplate":"t","bodyTemplate":"b","channel":"Push"}
                            """))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void returns_400_when_duplicate_key() throws Exception {
            given(createTemplateUseCase.create(any(CreateTemplateCommand.class)))
                    .willThrow(new IllegalArgumentException("Template key already exists: dup"));

            mockMvc.perform(post("/internal/notification-templates")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(VALID_TEMPLATE_JSON))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class PreviewTemplate {

        @Test
        void returns_rendered_preview() throws Exception {
            given(previewTemplateUseCase.preview(any(), any(), any()))
                    .willReturn(new PreviewTemplateResponse("안녕 홍길동님", "홍길동님 환영"));

            mockMvc.perform(
                            post("/internal/notification-templates/preview")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(
                                            """
                            {"titleTemplate":"안녕 {{name}}님","bodyTemplate":"{{name}}님 환영","variables":{"name":"홍길동"}}
                            """))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.renderedTitle").value("안녕 홍길동님"))
                    .andExpect(jsonPath("$.renderedBody").value("홍길동님 환영"));
        }
    }

    @Nested
    class UpdateTemplate {

        @Test
        void returns_200_with_updated_template() throws Exception {
            UUID id = UUID.randomUUID();
            given(updateTemplateUseCase.update(eq(id), any(UpdateTemplateCommand.class)))
                    .willReturn(new TemplateInfo(id, "updated", "t", "b", "Both", FIXED_TIME, FIXED_TIME));

            mockMvc.perform(
                            put("/internal/notification-templates/{id}", id)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(
                                            """
                            {"templateKey":"updated","titleTemplate":"t","bodyTemplate":"b","channel":"Both"}
                            """))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.templateKey").value("updated"))
                    .andExpect(jsonPath("$.channel").value("Both"));
        }

        @Test
        void returns_404_when_not_found() throws Exception {
            UUID id = UUID.randomUUID();
            given(updateTemplateUseCase.update(eq(id), any(UpdateTemplateCommand.class)))
                    .willThrow(new NotFoundException("NotificationTemplate", id));

            mockMvc.perform(put("/internal/notification-templates/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(VALID_TEMPLATE_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class DeleteTemplate {

        @Test
        void returns_204_on_success() throws Exception {
            UUID id = UUID.randomUUID();

            mockMvc.perform(delete("/internal/notification-templates/{id}", id)).andExpect(status().isNoContent());

            then(deleteTemplateUseCase).should().delete(id);
        }
    }
}
