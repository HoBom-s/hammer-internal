package com.hammer.internal.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NotificationTemplateIntegrationTest extends IntegrationTestBase {

    @Autowired
    MockMvc mockMvc;

    static String createdId;

    @Test
    @Order(1)
    void create_template() throws Exception {
        MvcResult result = mockMvc.perform(
                        post("/internal/notification-templates")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                        {
                            "templateKey": "welcome_push",
                            "titleTemplate": "환영합니다",
                            "bodyTemplate": "{{name}}님 가입을 축하합니다",
                            "channel": "Push"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.templateKey").value("welcome_push"))
                .andExpect(jsonPath("$.channel").value("Push"))
                .andReturn();

        createdId = com.jayway.jsonpath.JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    }

    @Test
    @Order(2)
    void create_duplicate_key_returns_400() throws Exception {
        mockMvc.perform(
                        post("/internal/notification-templates")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                        {
                            "templateKey": "welcome_push",
                            "titleTemplate": "중복",
                            "bodyTemplate": "중복 본문",
                            "channel": "InApp"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.detail").value("Template key already exists: welcome_push"));
    }

    @Test
    @Order(3)
    void list_templates_with_pagination() throws Exception {
        mockMvc.perform(get("/internal/notification-templates")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.items[0].templateKey").value("welcome_push"))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @Order(4)
    void list_templates_filtered_by_channel() throws Exception {
        mockMvc.perform(get("/internal/notification-templates").param("channel", "Push"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(1));

        mockMvc.perform(get("/internal/notification-templates").param("channel", "InApp"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(0));
    }

    @Test
    @Order(5)
    void list_templates_filtered_by_keyword() throws Exception {
        mockMvc.perform(get("/internal/notification-templates").param("keyword", "welcome"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(1));

        mockMvc.perform(get("/internal/notification-templates").param("keyword", "nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(0));
    }

    @Test
    @Order(6)
    void get_template_by_id() throws Exception {
        mockMvc.perform(get("/internal/notification-templates/" + createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.templateKey").value("welcome_push"));
    }

    @Test
    @Order(7)
    void preview_template() throws Exception {
        mockMvc.perform(
                        post("/internal/notification-templates/preview")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                        {
                            "titleTemplate": "안녕하세요 {{name}}님",
                            "bodyTemplate": "{{name}}님, {{event}} 알림입니다",
                            "variables": {"name": "홍길동", "event": "결제"}
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.renderedTitle").value("안녕하세요 홍길동님"))
                .andExpect(jsonPath("$.renderedBody").value("홍길동님, 결제 알림입니다"));
    }

    @Test
    @Order(8)
    void update_template() throws Exception {
        mockMvc.perform(
                        put("/internal/notification-templates/" + createdId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                        {
                            "templateKey": "welcome_inapp",
                            "titleTemplate": "환영합니다 (수정)",
                            "bodyTemplate": "수정된 본문",
                            "channel": "Both"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.templateKey").value("welcome_inapp"))
                .andExpect(jsonPath("$.channel").value("Both"));
    }

    @Test
    @Order(9)
    void delete_template() throws Exception {
        mockMvc.perform(delete("/internal/notification-templates/" + createdId)).andExpect(status().isNoContent());

        mockMvc.perform(get("/internal/notification-templates/" + createdId)).andExpect(status().isNotFound());
    }

    @Test
    @Order(10)
    void create_with_invalid_channel_returns_400() throws Exception {
        mockMvc.perform(
                        post("/internal/notification-templates")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                        {
                            "templateKey": "test_key",
                            "titleTemplate": "제목",
                            "bodyTemplate": "본문",
                            "channel": "Email"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }
}
