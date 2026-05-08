package com.hammer.internal.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

class DashboardIntegrationTest extends IntegrationTestBase {

    @Autowired
    MockMvc mockMvc;

    @Test
    void get_dashboard_stats() throws Exception {
        mockMvc.perform(get("/internal/dashboard/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users").exists())
                .andExpect(jsonPath("$.users.total").isNumber())
                .andExpect(jsonPath("$.users.active").isNumber())
                .andExpect(jsonPath("$.users.suspended").isNumber())
                .andExpect(jsonPath("$.users.deleted").isNumber())
                .andExpect(jsonPath("$.totalQuizzes").isNumber())
                .andExpect(jsonPath("$.errorLogs").exists())
                .andExpect(jsonPath("$.errorLogs.totalToday").isNumber())
                .andExpect(jsonPath("$.errorLogs.topErrorCodes").isArray())
                .andExpect(jsonPath("$.errorLogs.dailyTrend").isArray());
    }

    @Test
    void get_dashboard_stats_with_custom_trend_days() throws Exception {
        mockMvc.perform(get("/internal/dashboard/stats").param("trendDays", "30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users").exists())
                .andExpect(jsonPath("$.totalQuizzes").isNumber())
                .andExpect(jsonPath("$.errorLogs").exists());
    }
}
