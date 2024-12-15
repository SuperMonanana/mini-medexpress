package com.example.mengyan;

import com.example.mengyan.consultation.ConsultationController;
import com.example.mengyan.consultation.ConsultationService;
import com.example.mengyan.consultation.requests.Answer;
import com.example.mengyan.consultation.requests.ConsultationAnswers;
import com.example.mengyan.consultation.response.ConsultationQuestions;
import com.example.mengyan.consultation.response.ConsultationResponse;
import com.example.mengyan.consultation.response.Question;
import com.example.mengyan.consultation.response.Section;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static com.example.mengyan.consultation.response.Category.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ConsultationControllerTest {
    private MockMvc mockMvc;

    @Mock
    private ConsultationService consultationService;

    @InjectMocks
    private ConsultationController consultationController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(consultationController).build();
    }

    @Test
    void testGetQuestionsValidCondition() throws Exception {
        String condition = "migraine";
        ConsultationQuestions expectedResponses = mockGetQuestionsResponse();
        when(consultationService.getQuestions(condition)).thenReturn(expectedResponses);

        mockMvc.perform(get("/consultation/questions/" + condition))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Migraine Consultation"))
                .andExpect(jsonPath("$.sections", hasSize(3)))
                .andExpect(jsonPath("$.sections[0].title").value("About You"))
                .andExpect(jsonPath("$.sections[0].questions", hasSize(1)))
                .andExpect(jsonPath("$.sections[0].questions[0].id").value("q1"))
                .andExpect(jsonPath("$.sections[1].title").value("Symptoms"))
                .andExpect(jsonPath("$.sections[1].questions", hasSize(2)))
                .andExpect(jsonPath("$.sections[2].title").value("Health"))
                .andExpect(jsonPath("$.sections[2].questions", hasSize(2)));

    }

    @Test
    void testGetQuestionsConditionNotFound() throws Exception {
        String condition = "unknown";
        when(consultationService.getQuestions(condition)).thenThrow(new IllegalArgumentException("Condition not found"));

        // Act & Assert
        mockMvc.perform(get("/consultation/questions/" + condition))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSubmitAnswers() throws Exception {
        ConsultationAnswers answers = mockSubmitAnswersRequest();
        ConsultationResponse expectedResponses = new ConsultationResponse(true);
        when(consultationService.submitAnswers(answers)).thenReturn(expectedResponses);

        mockMvc.perform(post("/consultation/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(answers)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.canPrescribe").value(true));
    }

    @Test
    void testSubmitAnswersNoAnswers() throws Exception {
        ConsultationAnswers answers = new ConsultationAnswers(List.of());
        when(consultationService.submitAnswers(answers)).thenThrow(new IllegalArgumentException("No answers provided"));

        mockMvc.perform(post("/consultation/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(answers)))
                .andExpect(status().isBadRequest());
    }


    private ConsultationQuestions mockGetQuestionsResponse() {
        Question q1 = Question.builder().id("q1").category(PESONAL_INFO).questionText("Are you aged between 18-65?").options(List.of("Yes", "No")).build();
        Question q2 = Question.builder().id("q2").category(SYMPTOMS).questionText("Do you experience migraines for more than 10 days a month?").options(List.of("Yes", "No")).build();
        Question q3 = Question.builder().id("q3").category(SYMPTOMS).questionText("Do your migraines last less than 4 hours without treatment or last longer than 24 hours?").options(List.of("Yes", "No")).build();
        Question q4 = Question.builder().id("q4").category(HEALTH).questionText("Do you have an allergy (hypersensitivity) to Imigran/Sumatriptan, Maxalt/rizatriptan, Zomig/zolmitriptan?").options(List.of("Yes", "No")).build();
        Question q5 = Question.builder().id("q5").category(HEALTH).questionText("Are you breastfeeding or pregnant or possibly pregnant?").options(List.of("Yes", "No")).build();

        Section aboutYou = new Section("About You", List.of(q1));
        Section symptoms = new Section("Symptoms", List.of(q2, q3));
        Section health = new Section("Health", List.of(q4, q5));

        ConsultationQuestions migraineConsultation = new ConsultationQuestions("Migraine Consultation", List.of(aboutYou, symptoms, health));
        return migraineConsultation;
    }

    private ConsultationAnswers mockSubmitAnswersRequest() {
        Answer a1 = Answer.builder().questionId("q1").category(PESONAL_INFO).answer("Yes").build();
        Answer a2 = Answer.builder().questionId("q1").category(PESONAL_INFO).answer("Yes").build();
        Answer a3 = Answer.builder().questionId("q1").category(PESONAL_INFO).answer("Yes").build();
        Answer a4 = Answer.builder().questionId("q1").category(PESONAL_INFO).answer("Yes").build();
        Answer a5 = Answer.builder().questionId("q1").category(PESONAL_INFO).answer("Yes").build();

        ConsultationAnswers answers = new ConsultationAnswers(List.of(a1, a2, a3, a4, a5));
        return answers;
    }

}
