package com.example.mengyan.consultation.response;

import lombok.*;

@RequiredArgsConstructor
@Value
public class ConsultationResponse {
    @NonNull
    boolean canPrescribe;
}
