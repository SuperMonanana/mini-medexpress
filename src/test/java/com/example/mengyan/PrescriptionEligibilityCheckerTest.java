package com.example.mengyan;

import com.example.mengyan.consultation.PrescriptionEligibilityChecker;
import com.example.mengyan.consultation.requests.Answer;
import com.example.mengyan.consultation.requests.ConsultationAnswers;
import com.example.mengyan.consultation.response.Category;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PrescriptionEligibilityCheckerTest {
    private final PrescriptionEligibilityChecker eligibilityChecker = new PrescriptionEligibilityChecker();

    @Test
    void testCanPrescribe_AllAnswersCompliant() {
        // All answers are compliant
        ConsultationAnswers answers = new ConsultationAnswers(List.of(
                new Answer("q1", "Yes", Category.PESONAL_INFO),
                new Answer("q2", "No", Category.SYMPTOMS),
                new Answer("q3", "Yes", Category.AGREEMENT)
        ));

        boolean result = eligibilityChecker.canPrescribe(answers);

        assertTrue(result);
    }

    @Test
    void testCanPrescribe_HealthIssue() {
        // A health-related answer is "Yes"
        ConsultationAnswers answers = new ConsultationAnswers(List.of(
                new Answer("q1", "Yes", Category.PESONAL_INFO),
                new Answer("q2", "Yes", Category.HEALTH),
                new Answer("q3", "Yes", Category.AGREEMENT)
        ));

        boolean result = eligibilityChecker.canPrescribe(answers);

        assertFalse(result);
    }

    @Test
    void testCanPrescribe_MedicationIssue() {
        // A medication-related answer is "Yes"
        ConsultationAnswers answers = new ConsultationAnswers(List.of(
                new Answer("q1", "Yes", Category.PESONAL_INFO),
                new Answer("q2", "Yes", Category.MEDICATION),
                new Answer("q3", "Yes", Category.AGREEMENT)
        ));

        boolean result = eligibilityChecker.canPrescribe(answers);

        assertFalse(result);
    }

    @Test
    void testCanPrescribe_AgreementIssue() {
        // An agreement answer is "No"
        ConsultationAnswers answers = new ConsultationAnswers(List.of(
                new Answer("q1", "Yes", Category.PESONAL_INFO),
                new Answer("q2", "No", Category.SYMPTOMS),
                new Answer("q3", "No", Category.AGREEMENT)
        ));

        boolean result = eligibilityChecker.canPrescribe(answers);

        assertFalse(result);
    }

    @Test
    void testCanPrescribe_MultipleIssues() {
        // Multiple issues are present
        ConsultationAnswers answers = new ConsultationAnswers(List.of(
                new Answer("q1", "Yes", Category.HEALTH),
                new Answer("q2", "Yes", Category.SYMPTOMS),
                new Answer("q3", "No", Category.AGREEMENT)
        ));

        boolean result = eligibilityChecker.canPrescribe(answers);

        assertFalse(result);
    }

    @Test
    void testCanPrescribe_NoAnswers() {
        ConsultationAnswers answers = new ConsultationAnswers(List.of());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                eligibilityChecker.canPrescribe(answers)
        );

        assertEquals("No answers provided!", exception.getMessage());
    }
}
