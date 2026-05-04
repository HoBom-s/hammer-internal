package com.hammer.internal.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

class ActuatorIntegrationTest extends IntegrationTestBase {

    @Autowired
    MockMvc mockMvc;

    @Test
    void health_endpoint_returns_up() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.components.db").exists())
                .andExpect(jsonPath("$.components.diskSpace").exists());
    }

    @Test
    void info_endpoint_returns_app_info() throws Exception {
        mockMvc.perform(get("/actuator/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.app.name").value("hammer-internal"));
    }

    @Test
    void prometheus_endpoint_is_accessible() throws Exception {
        mockMvc.perform(get("/actuator/prometheus")).andExpect(status().isOk());
    }
}
