package com.example.mengyan.consultation;

import com.example.mengyan.consultation.requests.ConsultationAnswers;
import com.example.mengyan.consultation.response.ConsultationQuestions;
import com.example.mengyan.consultation.response.ConsultationResponse;
import com.example.mengyan.consultation.response.Question;
import com.example.mengyan.consultation.response.Section;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.mengyan.consultation.response.Category.*;

@Service
@Slf4j
public class ConsultationService {

    // In reality, we may hold the questions in a database
    private Map<String, ConsultationQuestions> questionsByCondition;
    private PrescriptionEligibilityChecker prescriptionEligibilityChecker;

    public ConsultationService(PrescriptionEligibilityChecker prescriptionEligibilityChecker) {
        this.questionsByCondition =  new HashMap<>();
        loadQuestions();
        this.prescriptionEligibilityChecker = prescriptionEligibilityChecker;
    }

    /**
     * Get consultation questions for the condition
     */
    public ConsultationQuestions getQuestions(String condition) {
        if (!questionsByCondition.containsKey(condition)) {
            throw new IllegalArgumentException(String.format("Condition %s not supported", condition));
        }
        return questionsByCondition.get(condition);
    }

    public ConsultationResponse submitAnswers(ConsultationAnswers answers) {
        boolean canPrescribe = prescriptionEligibilityChecker.canPrescribe(answers);
        return new ConsultationResponse(canPrescribe);
    }

    private void loadQuestions() {
        // below are sample questions for migraines
        Question q1 = Question.builder().id("q1").category(PESONAL_INFO).questionText("Are you aged between 18-65?").options(List.of("Yes", "No")).build();
        Question q2 = Question.builder().id("q2").category(SYMPTOMS).questionText("Do you experience migraines for more than 10 days a month?").options(List.of("Yes", "No")).build();
        Question q3 = Question.builder().id("q3").category(SYMPTOMS).questionText("Do your migraines last less than 4 hours without treatment or last longer than 24 hours?").options(List.of("Yes", "No")).build();
        Question q4 = Question.builder().id("q4").category(HEALTH).questionText("Do you have an allergy (hypersensitivity) to Imigran/Sumatriptan, Maxalt/rizatriptan, Zomig/zolmitriptan?").options(List.of("Yes", "No")).build();
        Question q5 = Question.builder().id("q5").category(HEALTH).questionText("Are you breastfeeding or pregnant or possibly pregnant?").options(List.of("Yes", "No")).build();

        Section aboutYou = new Section("About You", List.of(q1));
        Section symptoms = new Section("Symptoms", List.of(q2, q3));
        Section health = new Section("Health", List.of(q4, q5));

        ConsultationQuestions migraineConsultation = new ConsultationQuestions("Migraine Consultation", List.of(aboutYou, symptoms, health));
        questionsByCondition.put("migraine", migraineConsultation);
    }
}
