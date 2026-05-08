package com.hammer.internal.quiz.adapter.in.web;

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
import com.hammer.internal.quiz.application.dto.CreateQuizCommand;
import com.hammer.internal.quiz.application.dto.QuizInfo;
import com.hammer.internal.quiz.application.dto.UpdateQuizCommand;
import com.hammer.internal.quiz.application.port.in.CreateQuizUseCase;
import com.hammer.internal.quiz.application.port.in.DeleteQuizUseCase;
import com.hammer.internal.quiz.application.port.in.GetQuizUseCase;
import com.hammer.internal.quiz.application.port.in.GetRandomQuizzesUseCase;
import com.hammer.internal.quiz.application.port.in.ListQuizzesUseCase;
import com.hammer.internal.quiz.application.port.in.UpdateQuizUseCase;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(QuizController.class)
class QuizControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    SaveErrorLogPort saveErrorLogPort;

    @MockitoBean
    GetQuizUseCase getQuizUseCase;

    @MockitoBean
    ListQuizzesUseCase listQuizzesUseCase;

    @MockitoBean
    GetRandomQuizzesUseCase getRandomQuizzesUseCase;

    @MockitoBean
    CreateQuizUseCase createQuizUseCase;

    @MockitoBean
    UpdateQuizUseCase updateQuizUseCase;

    @MockitoBean
    DeleteQuizUseCase deleteQuizUseCase;

    private static final OffsetDateTime FIXED_TIME = OffsetDateTime.parse("2024-06-15T12:00:00+09:00");

    private static QuizInfo sampleQuizInfo(Long id) {
        return new QuizInfo(id, "2+2는?", List.of("3", "4", "5", "6"), 1, "2+2=4", FIXED_TIME);
    }

    private static final String VALID_QUIZ_JSON =
            """
            {
                "question": "2+2는?",
                "choice1": "3",
                "choice2": "4",
                "choice3": "5",
                "choice4": "6",
                "correctIndex": 1,
                "explanation": "2+2=4"
            }
            """;

    @Nested
    class GetQuizzes {

        @Test
        void returns_paged_result() throws Exception {
            given(listQuizzesUseCase.listQuizzes(1, 20, null))
                    .willReturn(new PagedResult<>(List.of(sampleQuizInfo(1L)), 1, 20, 1, 1));

            mockMvc.perform(get("/internal/quizzes"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.items[0].question").value("2+2는?"))
                    .andExpect(jsonPath("$.items[0].choices").isArray())
                    .andExpect(jsonPath("$.items[0].choices.length()").value(4));
        }

        @Test
        void respects_page_and_size_params() throws Exception {
            given(listQuizzesUseCase.listQuizzes(2, 5, null)).willReturn(new PagedResult<>(List.of(), 2, 5, 10, 2));

            mockMvc.perform(get("/internal/quizzes").param("page", "2").param("size", "5"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.page").value(2))
                    .andExpect(jsonPath("$.size").value(5));
        }
    }

    @Nested
    class GetRandomQuizzes {

        @Test
        void returns_random_quizzes() throws Exception {
            given(getRandomQuizzesUseCase.getRandomQuizzes(3))
                    .willReturn(List.of(sampleQuizInfo(1L), sampleQuizInfo(2L), sampleQuizInfo(3L)));

            mockMvc.perform(get("/internal/quizzes/random").param("count", "3"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(3))
                    .andExpect(jsonPath("$[0].id").value(1));
        }

        @Test
        void uses_default_count_of_3() throws Exception {
            given(getRandomQuizzesUseCase.getRandomQuizzes(3)).willReturn(List.of(sampleQuizInfo(1L)));

            mockMvc.perform(get("/internal/quizzes/random"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }
    }

    @Nested
    class GetQuiz {

        @Test
        void returns_quiz_by_id() throws Exception {
            given(getQuizUseCase.getQuiz(1L)).willReturn(sampleQuizInfo(1L));

            mockMvc.perform(get("/internal/quizzes/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.correctIndex").value(1));
        }

        @Test
        void returns_404_when_not_found() throws Exception {
            given(getQuizUseCase.getQuiz(999L)).willThrow(new NotFoundException("Quiz", 999L));

            mockMvc.perform(get("/internal/quizzes/999")).andExpect(status().isNotFound());
        }
    }

    @Nested
    class CreateQuiz {

        @Test
        void returns_201_with_created_quiz() throws Exception {
            given(createQuizUseCase.create(any(CreateQuizCommand.class))).willReturn(sampleQuizInfo(1L));

            mockMvc.perform(post("/internal/quizzes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(VALID_QUIZ_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1));
        }

        @Test
        void returns_400_when_question_is_blank() throws Exception {
            mockMvc.perform(
                            post("/internal/quizzes")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(
                                            """
                            {"question":"","choice1":"A","choice2":"B","choice3":"C","choice4":"D","correctIndex":0,"explanation":"E"}
                            """))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void returns_400_when_correctIndex_out_of_range() throws Exception {
            mockMvc.perform(
                            post("/internal/quizzes")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(
                                            """
                            {"question":"Q","choice1":"A","choice2":"B","choice3":"C","choice4":"D","correctIndex":5,"explanation":"E"}
                            """))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class UpdateQuiz {

        @Test
        void returns_200_with_updated_quiz() throws Exception {
            given(updateQuizUseCase.update(eq(1L), any(UpdateQuizCommand.class)))
                    .willReturn(sampleQuizInfo(1L));

            mockMvc.perform(put("/internal/quizzes/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(VALID_QUIZ_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1));
        }

        @Test
        void returns_404_when_quiz_not_found() throws Exception {
            given(updateQuizUseCase.update(eq(999L), any(UpdateQuizCommand.class)))
                    .willThrow(new NotFoundException("Quiz", 999L));

            mockMvc.perform(put("/internal/quizzes/999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(VALID_QUIZ_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class DeleteQuiz {

        @Test
        void returns_204_on_success() throws Exception {
            mockMvc.perform(delete("/internal/quizzes/1")).andExpect(status().isNoContent());

            then(deleteQuizUseCase).should().delete(1L);
        }
    }
}
