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
    void list_templates() throws Exception {
        mockMvc.perform(get("/internal/notification-templates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].templateKey").value("welcome_push"));
    }

    @Test
    @Order(4)
    void get_template_by_id() throws Exception {
        mockMvc.perform(get("/internal/notification-templates/" + createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.templateKey").value("welcome_push"));
    }

    @Test
    @Order(5)
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
    @Order(6)
    void delete_template() throws Exception {
        mockMvc.perform(delete("/internal/notification-templates/" + createdId)).andExpect(status().isNoContent());

        mockMvc.perform(get("/internal/notification-templates/" + createdId)).andExpect(status().isNotFound());
    }

    @Test
    @Order(7)
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
