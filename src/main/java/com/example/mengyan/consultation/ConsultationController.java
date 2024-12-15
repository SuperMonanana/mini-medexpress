package com.example.mengyan.consultation;

import com.example.mengyan.consultation.requests.ConsultationAnswers;
import com.example.mengyan.consultation.response.ConsultationQuestions;
import com.example.mengyan.consultation.response.ConsultationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Consultation", description = "Endpoints for consultation")
@RequestMapping("/consultation")
public class ConsultationController {

    private final ConsultationService consultationService;

    @Operation(
            summary = "Get consultation questions",
            description = "Fetches the consultation questions based on the specified condition.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Consultation questions retrieved successfully",
                            content = @Content(schema = @Schema(implementation = ConsultationQuestions.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid condition",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content
                    )
            }
    )
    @GetMapping("/questions/{condition}")
    public ResponseEntity<ConsultationQuestions> getQuestions(@PathVariable() String condition) {
        try {
            ConsultationQuestions consultationQuestions = consultationService.getQuestions(condition);
            return ResponseEntity.ok(consultationQuestions);
        } catch (IllegalArgumentException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(
            summary = "Submit consultation answers",
            description = "Submits the consultation answers and returns the eligibility result.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "The answers to consultation questions",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ConsultationAnswers.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Answers submitted successfully and eligibility for prescription determined",
                            content = @Content(schema = @Schema(implementation = ConsultationResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input provided",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content
                    )
            }
    )
    @PostMapping("/answers")
    public ResponseEntity<ConsultationResponse> submitAnswers(@RequestBody ConsultationAnswers answers) {
        try {
            ConsultationResponse consultationResponse = consultationService.submitAnswers(answers);
            return ResponseEntity.ok(consultationResponse);
        } catch (IllegalArgumentException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
