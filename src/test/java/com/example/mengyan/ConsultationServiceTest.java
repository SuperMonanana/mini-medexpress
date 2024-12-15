package com.example.mengyan;

import com.example.mengyan.consultation.ConsultationService;
import com.example.mengyan.consultation.PrescriptionEligibilityChecker;
import com.example.mengyan.consultation.requests.Answer;
import com.example.mengyan.consultation.requests.ConsultationAnswers;
import com.example.mengyan.consultation.response.Category;
import com.example.mengyan.consultation.response.ConsultationQuestions;
import com.example.mengyan.consultation.response.ConsultationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConsultationServiceTest {

    @Mock
    private PrescriptionEligibilityChecker prescriptionEligibilityChecker;

    private ConsultationService consultationService;

    @BeforeEach
    void setUp() {
        consultationService = new ConsultationService(prescriptionEligibilityChecker);
    }

    @Test
    void testGetQuestionsValidCondition() {
        ConsultationQuestions questions = consultationService.getQuestions("migraine");

        assertNotNull(questions);
        assertEquals("Migraine Consultation", questions.getTitle());
        assertEquals(3, questions.getSections().size());
        assertEquals("About You", questions.getSections().get(0).getTitle());
    }

    @Test
    void testGetQuestionsInvalidCondition() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                consultationService.getQuestions("unknown"));

        assertEquals("Condition unknown not supported", exception.getMessage());
    }

    @Test
    void testSubmitAnswersCanPrescribe() {
        // Arrange
        ConsultationAnswers answers = new ConsultationAnswers(List.of(Answer.builder().questionId("q1").category(Category.AGREEMENT).answer("Yes").build()));
        when(prescriptionEligibilityChecker.canPrescribe(answers)).thenReturn(true);

        // Act
        ConsultationResponse response = consultationService.submitAnswers(answers);

        // Assert
        assertNotNull(response);
        assertTrue(response.isCanPrescribe());
    }

    @Test
    void testSubmitAnswersCannotPrescribe() {
        // Arrange
        ConsultationAnswers answers = new ConsultationAnswers(List.of(Answer.builder().questionId("q1").category(Category.AGREEMENT).answer("No").build()));
        when(prescriptionEligibilityChecker.canPrescribe(answers)).thenReturn(false);

        // Act
        ConsultationResponse response = consultationService.submitAnswers(answers);

        // Assert
        assertNotNull(response, "Response should not be null");
        assertFalse(response.isCanPrescribe(), "Can prescribe should be false");
    }

}

