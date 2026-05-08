package com.hammer.internal.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ErrorLogIntegrationTest extends IntegrationTestBase {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Order(1)
    void trigger_error_to_create_log() throws Exception {
        // Trigger a 404 to generate an error log entry
        mockMvc.perform(get("/internal/quizzes/99999")).andExpect(status().isNotFound());
    }

    @Test
    @Order(2)
    void list_error_logs_without_filters() throws Exception {
        mockMvc.perform(get("/internal/error-logs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.page").value(1));
    }

    @Test
    @Order(3)
    void list_error_logs_filtered_by_status() throws Exception {
        mockMvc.perform(get("/internal/error-logs").param("status", "404"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray());
    }

    @Test
    @Order(4)
    void list_error_logs_filtered_by_error_code() throws Exception {
        mockMvc.perform(get("/internal/error-logs").param("errorCode", "NOT_FOUND"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray());
    }

    @Test
    @Order(5)
    void list_error_logs_filtered_by_uri() throws Exception {
        mockMvc.perform(get("/internal/error-logs").param("uri", "quizzes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray());
    }

    @Test
    @Order(6)
    void list_error_logs_with_no_results() throws Exception {
        mockMvc.perform(get("/internal/error-logs").param("status", "999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(0));
    }
}
