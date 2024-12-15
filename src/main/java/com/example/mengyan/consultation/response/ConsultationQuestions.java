package com.example.mengyan.consultation.response;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@RequiredArgsConstructor
public class ConsultationQuestions {
    @NonNull
    String title;

    @NonNull
    List<Section> sections;
}
