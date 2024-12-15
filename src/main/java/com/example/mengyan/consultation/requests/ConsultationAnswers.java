package com.example.mengyan.consultation.requests;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class ConsultationAnswers {
    @NonNull
    List<Answer> answers;
}
