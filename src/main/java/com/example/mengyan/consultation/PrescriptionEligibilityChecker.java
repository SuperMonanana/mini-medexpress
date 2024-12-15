package com.example.mengyan.consultation;

import com.example.mengyan.consultation.requests.Answer;
import com.example.mengyan.consultation.requests.ConsultationAnswers;
import com.example.mengyan.consultation.response.Category;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionEligibilityChecker {

    /**
     * Determine eligibility by checking all answers
     */
    public boolean canPrescribe(ConsultationAnswers answers) {
        // Could build smarter rules in reality
        // For the test just simply return false if any answers are "Yes" for health or medication questions and "No" for agreement
        if (answers.getAnswers().isEmpty()) {
            throw new IllegalArgumentException("No answers provided!");
        }
        return answers.getAnswers().stream()
                .noneMatch(answer -> isHealthOrMedicationIssue(answer) || isAgreementIssue(answer));
    }


    private boolean isHealthOrMedicationIssue(Answer answer) {
        // Check if the category is HEALTH or MEDICATION and the answer is "Yes"
        return (answer.getCategory() == Category.HEALTH || answer.getCategory() == Category.MEDICATION)
                && "Yes".equalsIgnoreCase(answer.getAnswer());
    }

    private boolean isAgreementIssue(Answer answer) {
        // Check if the category is AGREEMENT and the answer is "No"
        return answer.getCategory() == Category.AGREEMENT
                && "No".equalsIgnoreCase(answer.getAnswer());
    }
}
