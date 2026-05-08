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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QuizIntegrationTest extends IntegrationTestBase {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Order(1)
    void create_quiz() throws Exception {
        mockMvc.perform(
                        post("/internal/quizzes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                        {
                            "question": "2+2는?",
                            "choice1": "3",
                            "choice2": "4",
                            "choice3": "5",
                            "choice4": "6",
                            "correctIndex": 1,
                            "explanation": "2+2=4 입니다"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.question").value("2+2는?"))
                .andExpect(jsonPath("$.choices").isArray())
                .andExpect(jsonPath("$.choices.length()").value(4))
                .andExpect(jsonPath("$.correctIndex").value(1));
    }

    @Test
    @Order(2)
    void list_quizzes() throws Exception {
        mockMvc.perform(get("/internal/quizzes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @Order(3)
    void search_quizzes_by_keyword() throws Exception {
        mockMvc.perform(get("/internal/quizzes").param("keyword", "2+2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.items[0].question").value("2+2는?"));
    }

    @Test
    @Order(4)
    void search_quizzes_by_keyword_no_match() throws Exception {
        mockMvc.perform(get("/internal/quizzes").param("keyword", "nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    @Order(5)
    void get_quiz_by_id() throws Exception {
        mockMvc.perform(get("/internal/quizzes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.question").value("2+2는?"));
    }

    @Test
    @Order(6)
    void update_quiz() throws Exception {
        mockMvc.perform(
                        put("/internal/quizzes/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                        {
                            "question": "3+3은?",
                            "choice1": "5",
                            "choice2": "6",
                            "choice3": "7",
                            "choice4": "8",
                            "correctIndex": 1,
                            "explanation": "3+3=6 입니다"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value("3+3은?"))
                .andExpect(jsonPath("$.correctIndex").value(1));
    }

    @Test
    @Order(7)
    void delete_quiz() throws Exception {
        mockMvc.perform(delete("/internal/quizzes/1")).andExpect(status().isNoContent());

        mockMvc.perform(get("/internal/quizzes/1")).andExpect(status().isNotFound());
    }

    @Test
    @Order(8)
    void get_nonexistent_quiz_returns_404_with_structured_error() throws Exception {
        mockMvc.perform(get("/internal/quizzes/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.detail").value("Quiz not found: 999"));
    }

    @Test
    @Order(9)
    void create_quiz_with_invalid_body_returns_400() throws Exception {
        mockMvc.perform(
                        post("/internal/quizzes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                        {
                            "question": "",
                            "choice1": "A",
                            "choice2": "B",
                            "choice3": "C",
                            "choice4": "D",
                            "correctIndex": 5,
                            "explanation": "E"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.errors").isArray());
    }
}
